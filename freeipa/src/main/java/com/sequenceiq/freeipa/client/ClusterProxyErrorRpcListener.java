package com.sequenceiq.freeipa.client;

import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.googlecode.jsonrpc4j.JsonRpcClient;
import com.sequenceiq.cloudbreak.clusterproxy.ClusterProxyError;
import com.sequenceiq.cloudbreak.clusterproxy.ClusterProxyException;

public class ClusterProxyErrorRpcListener implements JsonRpcClient.RequestListener {

    private Optional<ClusterProxyError> deserializeAsClusterProxyError(ObjectNode objectNode) {
        ObjectMapper mapper = new ObjectMapper();
        ClusterProxyError clusterProxyError;
        try {
            clusterProxyError = mapper.treeToValue(objectNode, ClusterProxyError.class);
            if (!clusterProxyError.getCode().contains("cluster-proxy")) {
                return Optional.empty();
            }
        } catch (Exception ex) {
            return Optional.empty();
        }
        return Optional.of(clusterProxyError);
    }

    private void throwIfClusterProxyError(ObjectNode node) {
        Optional<ClusterProxyError> clusterProxyError = deserializeAsClusterProxyError(node);
        if (clusterProxyError.isPresent()) {
            throw new ClusterProxyException(String.format("Cluster proxy service returned error: %s", clusterProxyError.get()), clusterProxyError);
        }
    }

    @Override
    public void onBeforeRequestSent(JsonRpcClient client, ObjectNode request) {
        // no op
    }

    @Override
    public void onBeforeResponseProcessed(JsonRpcClient client, ObjectNode response) {
        throwIfClusterProxyError(response);
    }

}
