package com.sequenceiq.externalizedcompute.service;

import jakarta.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.cloudera.thunderhead.service.liftiepublic.LiftiePublicProto.CreateClusterRequest;
import com.cloudera.thunderhead.service.liftiepublic.LiftiePublicProto.CreateClusterResponse;
import com.sequenceiq.cloudbreak.common.exception.CloudbreakServiceException;
import com.sequenceiq.environment.api.v1.environment.endpoint.EnvironmentEndpoint;
import com.sequenceiq.environment.api.v1.environment.model.response.DetailedEnvironmentResponse;
import com.sequenceiq.externalizedcompute.entity.ExternalizedComputeCluster;
import com.sequenceiq.externalizedcompute.repository.ExternalizedComputeClusterRepository;
import com.sequenceiq.externalizedcompute.util.LiftieValidationResponseUtil;
import com.sequenceiq.externalizedcompute.util.TagUtil;
import com.sequenceiq.liftie.client.LiftieGrpcClient;

@Service
public class ExternalizedComputeClusterCreateService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExternalizedComputeClusterCreateService.class);

    @Inject
    private LiftieGrpcClient liftieGrpcClient;

    @Inject
    private EnvironmentEndpoint environmentEndpoint;

    @Inject
    private ExternalizedComputeClusterRepository externalizedComputeClusterRepository;

    @Inject
    private LiftieValidationResponseUtil liftieValidationResponseUtil;

    public void initiateCreation(Long id, String userCrn) {
        ExternalizedComputeCluster externalizedComputeCluster = externalizedComputeClusterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Can't find externalized compute cluster in DB"));
        if (externalizedComputeCluster.getLiftieName() == null) {
            createLiftieCluster(userCrn, externalizedComputeCluster);
        }
    }

    private void createLiftieCluster(String userCrn, ExternalizedComputeCluster externalizedComputeCluster) {
        try {
            CreateClusterRequest cluster = setupLiftieCluster(externalizedComputeCluster);
            LOGGER.info("Send request to liftie: {}", cluster);
            CreateClusterResponse clusterResponse = liftieGrpcClient.createCluster(cluster, userCrn);
            if (clusterResponse.hasValidationResponse()) {
                liftieValidationResponseUtil.throwException(clusterResponse.getValidationResponse());
            }
            if (StringUtils.isEmpty(clusterResponse.getClusterId())) {
                LOGGER.warn("Liftie name cannot be empty!");
                throw new CloudbreakServiceException("Externalized compute cluster creation failed, cluster id cannot be empty!");
            }
            externalizedComputeCluster.setLiftieName(clusterResponse.getClusterId());
            externalizedComputeClusterRepository.save(externalizedComputeCluster);
            LOGGER.info("Liftie create response: {}", clusterResponse);
        } catch (Exception e) {
            LOGGER.error("Externalized compute cluster creation failed", e);
            throw new RuntimeException(e);
        }
    }

    private CreateClusterRequest setupLiftieCluster(ExternalizedComputeCluster externalizedComputeCluster) {
        DetailedEnvironmentResponse environment = environmentEndpoint.getByCrn(externalizedComputeCluster.getEnvironmentCrn());
        CreateClusterRequest.Builder createClusterBuilder = CreateClusterRequest.newBuilder();
        createClusterBuilder.setEnvironment(externalizedComputeCluster.getEnvironmentCrn())
                .setName(externalizedComputeCluster.getName())
                .setDescription("Common compute cluster")
                .putAllTags(TagUtil.getTags(externalizedComputeCluster.getTags()))
                .setIsDefault(externalizedComputeCluster.isDefaultCluster())
                .getNetworkBuilder().addAllSubnets(environment.getNetwork().getLiftieSubnets().keySet());
        return createClusterBuilder.build();
    }
}
