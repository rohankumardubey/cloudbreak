package com.sequenceiq.cloudbreak.cloud.aws.common.resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import com.amazonaws.services.ec2.model.BlockDeviceMapping;
import com.amazonaws.services.ec2.model.DescribeImagesRequest;
import com.amazonaws.services.ec2.model.DescribeImagesResult;
import com.amazonaws.services.ec2.model.EbsBlockDevice;
import com.sequenceiq.cloudbreak.auth.altus.EntitlementService;
import com.sequenceiq.cloudbreak.cloud.aws.common.client.AmazonEc2Client;
import com.sequenceiq.cloudbreak.cloud.aws.common.resource.volume.AwsVolumeIopsAndThroughputCalculator;
import com.sequenceiq.cloudbreak.cloud.aws.common.view.AuthenticatedContextView;
import com.sequenceiq.cloudbreak.cloud.aws.common.view.AwsInstanceView;
import com.sequenceiq.cloudbreak.cloud.context.AuthenticatedContext;
import com.sequenceiq.cloudbreak.cloud.exception.CloudConnectorException;
import com.sequenceiq.cloudbreak.cloud.model.CloudStack;
import com.sequenceiq.cloudbreak.cloud.model.Group;
import com.sequenceiq.cloudbreak.cloud.model.Volume;

@Component
public class VolumeBuilderUtil {

    @Inject
    private AwsVolumeIopsAndThroughputCalculator awsVolumeIopsAndThroughputCalculator;

    @Inject
    private EntitlementService entitlementService;

    public List<BlockDeviceMapping> getEphemeral(AwsInstanceView awsInstanceView) {
        Long ephemeralCount = getEphemeralCount(awsInstanceView);
        List<BlockDeviceMapping> ephemeralBlockDeviceMappings = new ArrayList<>();
        if (ephemeralCount != 0) {
            List<String> seq = List.of("b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x",
                    "y", "z");
            int xIndex = 0;
            while (xIndex < seq.size() && ephemeralBlockDeviceMappings.size() < ephemeralCount) {
                String blockDeviceNameIndex = seq.get(xIndex);
                ephemeralBlockDeviceMappings.add(new BlockDeviceMapping()
                        .withDeviceName("/dev/xvd" + blockDeviceNameIndex)
                        .withVirtualName("ephemeral" + xIndex));
                xIndex++;
            }
        }
        return ephemeralBlockDeviceMappings;
    }

    private Long getEphemeralCount(AwsInstanceView awsInstanceView) {
        Map<String, Long> volumes = awsInstanceView.getVolumes().stream().collect(Collectors.groupingBy(Volume::getType, Collectors.counting()));
        Long ephemeralCount = volumes.getOrDefault("ephemeral", 0L);
        if (ephemeralCount.equals(0L)) {
            return Optional.ofNullable(awsInstanceView.getTemporaryStorageCount()).orElse(0L);
        } else {
            return ephemeralCount;
        }
    }

    public BlockDeviceMapping getRootVolume(AwsInstanceView awsInstanceView, Group group, CloudStack cloudStack, AuthenticatedContext ac) {
        return new BlockDeviceMapping()
                .withDeviceName(getRootDeviceName(ac, cloudStack))
                .withEbs(getRootEbs(awsInstanceView, group, ac.getCloudCredential().getAccountId()));
    }

    public EbsBlockDevice getRootEbs(AwsInstanceView awsInstanceView, Group group, String accountId) {

        String volumeType = entitlementService.isAwsGp3SupportEnabled(accountId) ? "gp3" : "gp2";
        int rootVolumeSize = group.getRootVolumeSize();

        EbsBlockDevice ebsBlockDevice = new EbsBlockDevice()
                .withDeleteOnTermination(true)
                .withVolumeType(volumeType)
                .withIops(awsVolumeIopsAndThroughputCalculator.getIops(volumeType, rootVolumeSize))
                .withThroughput(awsVolumeIopsAndThroughputCalculator.getThroughput(volumeType, rootVolumeSize))
                .withVolumeSize(rootVolumeSize);

        if (awsInstanceView.isEncryptedVolumes()) {
            ebsBlockDevice.withEncrypted(true);
        }

        if (awsInstanceView.isKmsCustom()) {
            ebsBlockDevice.withKmsKeyId(awsInstanceView.getKmsKey());
        }
        return ebsBlockDevice;
    }

    public String getRootDeviceName(AuthenticatedContext ac, CloudStack cloudStack) {
        AmazonEc2Client ec2Client = new AuthenticatedContextView(ac).getAmazonEC2Client();
        DescribeImagesResult images = ec2Client.describeImages(new DescribeImagesRequest().withImageIds(cloudStack.getImage().getImageName()));
        if (images.getImages().isEmpty()) {
            throw new CloudConnectorException(String.format("AMI is not available: '%s'.", cloudStack.getImage().getImageName()));
        }
        com.amazonaws.services.ec2.model.Image image = images.getImages().get(0);
        if (image == null) {
            throw new CloudConnectorException(String.format("Couldn't describe AMI '%s'.", cloudStack.getImage().getImageName()));
        }
        return image.getRootDeviceName();
    }
}
