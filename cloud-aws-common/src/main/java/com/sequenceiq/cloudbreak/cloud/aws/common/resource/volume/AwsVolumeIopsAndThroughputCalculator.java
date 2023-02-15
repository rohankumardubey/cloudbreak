package com.sequenceiq.cloudbreak.cloud.aws.common.resource.volume;

import org.springframework.stereotype.Component;

/**
 * This class calculates the IOPS and throughput values for GP3 volumes to be equivalent to the IOPS and throughput values for GP2 volumes in the same size.
 * For other volume types it returns with null
 * <p>
 * Calcualtion is based on the followings: https://aws.amazon.com/blogs/storage/migrate-your-amazon-ebs-volumes-from-gp2-to-gp3-and-save-up-to-20-on-costs/
 */
@Component
public class AwsVolumeIopsAndThroughputCalculator {

    private static final int MIN_GP3_IOPS = 3000;

    private static final int MAX_GP3_IOPS = 16000;

    // It is a magic constant from the AWS documentation
    private static final int IOPS_MULTIPLIER = 3;

    private static final int MIN_GP3_THROUGHPUT = 125;

    private static final int GP3_SIZE_500GB = 500;

    private static final int GP3_THROUGHPUT_OVER_500GB = 250;

    public Integer getIops(String type, Integer size) {
        if (!"gp3".equalsIgnoreCase(type)) {
            return null;
        }
        int iops = IOPS_MULTIPLIER * size;
        // IOPS must be between MIN_GP3_IOPS and MAX_GP3_IOPS
        return Math.max(MIN_GP3_IOPS, Math.min(MAX_GP3_IOPS, iops));
    }

    public Integer getThroughput(String type, int size) {
        if (!"gp3".equalsIgnoreCase(type)) {
            return null;
        }
        int throughput = MIN_GP3_THROUGHPUT;
        if (size >= GP3_SIZE_500GB) {
            throughput = GP3_THROUGHPUT_OVER_500GB;
        }
        return throughput;
    }
}
