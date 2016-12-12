package com.sequenceiq.it.cloudbreak.mock;

import static com.sequenceiq.it.spark.ITResponse.AMBARI_API_ROOT;
import static com.sequenceiq.it.spark.ITResponse.MOCK_ROOT;
import static com.sequenceiq.it.spark.ITResponse.SALT_API_ROOT;
import static com.sequenceiq.it.spark.ITResponse.SALT_BOOT_ROOT;
import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sequenceiq.cloudbreak.api.model.HostGroupAdjustmentJson;
import com.sequenceiq.cloudbreak.api.model.InstanceGroupAdjustmentJson;
import com.sequenceiq.cloudbreak.api.model.UpdateClusterJson;
import com.sequenceiq.cloudbreak.api.model.UpdateStackJson;
import com.sequenceiq.cloudbreak.client.CloudbreakClient;
import com.sequenceiq.cloudbreak.cloud.model.CloudInstance;
import com.sequenceiq.cloudbreak.cloud.model.CloudVmMetaDataStatus;
import com.sequenceiq.cloudbreak.orchestrator.model.GenericResponse;
import com.sequenceiq.cloudbreak.orchestrator.model.GenericResponses;
import com.sequenceiq.it.IntegrationTestContext;
import com.sequenceiq.it.cloudbreak.AbstractMockIntegrationTest;
import com.sequenceiq.it.cloudbreak.CloudbreakITContextConstants;
import com.sequenceiq.it.cloudbreak.CloudbreakUtil;
import com.sequenceiq.it.cloudbreak.InstanceGroup;
import com.sequenceiq.it.spark.ambari.AmbariCheckResponse;
import com.sequenceiq.it.spark.ambari.AmbariClusterRequestsResponse;
import com.sequenceiq.it.spark.ambari.AmbariClusterResponse;
import com.sequenceiq.it.spark.ambari.AmbariClustersHostsResponse;
import com.sequenceiq.it.spark.ambari.AmbariHostsResponse;
import com.sequenceiq.it.spark.ambari.AmbariServiceConfigResponse;
import com.sequenceiq.it.spark.ambari.AmbariStatusResponse;
import com.sequenceiq.it.spark.ambari.EmptyAmbariResponse;
import com.sequenceiq.it.spark.salt.SaltApiRunPostResponse;
import com.sequenceiq.it.spark.spi.CloudMetaDataStatuses;
import com.sequenceiq.it.spark.spi.CloudVmInstanceStatuses;

public class MockClusterScalingTest extends AbstractMockIntegrationTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(MockClusterScalingTest.class);

    @Value("${mock.server.address:localhost}")
    private String mockServerAddress;

    private MockInstanceUtil mockInstanceUtil;

    @BeforeMethod
    public void setContextParameters() {
        Assert.assertNotNull(getItContext().getContextParam(CloudbreakITContextConstants.STACK_ID), "Stack id is mandatory.");
    }

    @SuppressWarnings("Duplicates")
    @Test
    @Parameters({"instanceGroup", "scalingAdjustment", "mockPort"})
    public void testScaling(@Optional("slave_1") String instanceGroup, @Optional("1") int scalingAdjustment, @Optional("9443") int mockPort) throws Exception {
        mockInstanceUtil = new MockInstanceUtil(mockServerAddress, mockPort);

        // GIVEN
        IntegrationTestContext itContext = getItContext();
        String stackId = itContext.getContextParam(CloudbreakITContextConstants.STACK_ID);
        int stackIntId = Integer.valueOf(stackId);
        initSpark();

        Map<String, CloudVmMetaDataStatus> instanceMap = itContext.getContextParam(CloudbreakITContextConstants.MOCK_INSTANCE_MAP, Map.class);
        if (instanceMap == null || instanceMap.size() == 0) {
            throw new IllegalStateException("instance map should not be empty!");
        }

        addSPIEndpoints(instanceMap);
        addMockEndpoints(instanceMap);
        addAmbariMappings(instanceMap, mockPort);

        // WHEN
        if (scalingAdjustment < 0) {
            UpdateClusterJson updateClusterJson = new UpdateClusterJson();
            HostGroupAdjustmentJson hostGroupAdjustmentJson = new HostGroupAdjustmentJson();
            hostGroupAdjustmentJson.setHostGroup(instanceGroup);
            hostGroupAdjustmentJson.setWithStackUpdate(false);
            hostGroupAdjustmentJson.setScalingAdjustment(scalingAdjustment);
            updateClusterJson.setHostGroupAdjustment(hostGroupAdjustmentJson);
            CloudbreakUtil.checkResponse("DownscaleCluster", getCloudbreakClient().clusterEndpoint().put((long) stackIntId, updateClusterJson));
            CloudbreakUtil.waitAndCheckClusterStatus(getCloudbreakClient(), stackId, "AVAILABLE");

            UpdateStackJson updateStackJson = new UpdateStackJson();
            InstanceGroupAdjustmentJson instanceGroupAdjustmentJson = new InstanceGroupAdjustmentJson();
            instanceGroupAdjustmentJson.setInstanceGroup(instanceGroup);
            instanceGroupAdjustmentJson.setScalingAdjustment(scalingAdjustment);
            instanceGroupAdjustmentJson.setWithClusterEvent(false);
            updateStackJson.setInstanceGroupAdjustment(instanceGroupAdjustmentJson);
            CloudbreakUtil.checkResponse("DownscaleStack", getCloudbreakClient().stackEndpoint().put((long) stackIntId, updateStackJson));
            CloudbreakUtil.waitAndCheckStackStatus(getCloudbreakClient(), stackId, "AVAILABLE");
        } else {
            mockInstanceUtil.addInstance(instanceMap, scalingAdjustment);
            UpdateStackJson updateStackJson = new UpdateStackJson();
            InstanceGroupAdjustmentJson instanceGroupAdjustmentJson = new InstanceGroupAdjustmentJson();
            instanceGroupAdjustmentJson.setInstanceGroup(instanceGroup);
            instanceGroupAdjustmentJson.setScalingAdjustment(scalingAdjustment);
            instanceGroupAdjustmentJson.setWithClusterEvent(false);
            updateStackJson.setInstanceGroupAdjustment(instanceGroupAdjustmentJson);
            CloudbreakUtil.checkResponse("UpscaleStack", getCloudbreakClient().stackEndpoint().put((long) stackIntId, updateStackJson));
            CloudbreakUtil.waitAndCheckStackStatus(getCloudbreakClient(), stackId, "AVAILABLE");

            UpdateClusterJson updateClusterJson = new UpdateClusterJson();
            HostGroupAdjustmentJson hostGroupAdjustmentJson = new HostGroupAdjustmentJson();
            hostGroupAdjustmentJson.setHostGroup(instanceGroup);
            hostGroupAdjustmentJson.setWithStackUpdate(false);
            hostGroupAdjustmentJson.setScalingAdjustment(scalingAdjustment);
            updateClusterJson.setHostGroupAdjustment(hostGroupAdjustmentJson);
            CloudbreakUtil.checkResponse("UpscaleCluster", getCloudbreakClient().clusterEndpoint().put((long) stackIntId, updateClusterJson));
            CloudbreakUtil.waitAndCheckClusterStatus(getCloudbreakClient(), stackId, "AVAILABLE");
        }
        itContext.putContextParam(CloudbreakITContextConstants.MOCK_INSTANCE_MAP, instanceMap);
        // THEN
        CloudbreakUtil.checkClusterAvailability(
                itContext.getContextParam(CloudbreakITContextConstants.CLOUDBREAK_CLIENT, CloudbreakClient.class).stackEndpoint(),
                "8080", stackId, itContext.getContextParam(CloudbreakITContextConstants.AMBARI_USER_ID),
                itContext.getContextParam(CloudbreakITContextConstants.AMBARI_PASSWORD_ID), false);
    }

    private void addMockEndpoints(Map<String, CloudVmMetaDataStatus> instanceMap) {
        get(SALT_BOOT_ROOT + "/health", (request, response) -> {
            GenericResponse genericResponse = new GenericResponse();
            genericResponse.setStatusCode(HttpStatus.OK.value());
            return genericResponse;
        }, gson()::toJson);
        post(SALT_BOOT_ROOT + "/salt/server/pillar", (request, response) -> {
            GenericResponse genericResponse = new GenericResponse();
            genericResponse.setStatusCode(HttpStatus.OK.value());
            return genericResponse;
        }, gson()::toJson);
        post(SALT_BOOT_ROOT + "/salt/action/distribute", (request, response) -> {
            GenericResponses genericResponses = new GenericResponses();
            genericResponses.setResponses(new ArrayList<>());
            return genericResponses;
        }, gson()::toJson);
        post(SALT_BOOT_ROOT + "/hostname/distribute", (request, response) -> {
            GenericResponses genericResponses = new GenericResponses();
            ArrayList<GenericResponse> responses = new ArrayList<>();
            JsonObject parsedRequest = new JsonParser().parse(request.body()).getAsJsonObject();
            JsonArray nodeArray = parsedRequest.getAsJsonArray("clients");

            for (int i = 0; i < nodeArray.size(); i++) {
                String address = nodeArray.get(i).getAsString();
                GenericResponse genericResponse = new GenericResponse();
                genericResponse.setAddress(address);
                genericResponse.setStatus("host-" + address.replace(".", "-") + ".example.com");
                genericResponse.setStatusCode(200);
                responses.add(genericResponse);
            }
            genericResponses.setResponses(responses);
            return genericResponses;
        }, gson()::toJson);
        post(SALT_API_ROOT + "/run", new SaltApiRunPostResponse(instanceMap));

        get("/ws/v1/cluster/apps", (request, response) -> {
            ObjectNode rootNode = JsonNodeFactory.instance.objectNode();
            ArrayNode appNode = rootNode.putObject("apps").putArray("app");
            appNode.addObject().put("amHostHttpAddress", "192.168.1.1");
            return rootNode;
        });
    }

    private void addSPIEndpoints(Map<String, CloudVmMetaDataStatus> instanceMap) {
        post(MOCK_ROOT + "/cloud_metadata_statuses", new CloudMetaDataStatuses(instanceMap), gson()::toJson);
        post(MOCK_ROOT + "/cloud_instance_statuses", new CloudVmInstanceStatuses(instanceMap), gson()::toJson);
        post(MOCK_ROOT + "/terminate_instances", (request, response) -> {
            List<CloudInstance> cloudInstances = new Gson().fromJson(request.body(), new TypeToken<List<CloudInstance>>() { }.getType());
            cloudInstances.forEach(cloudInstance -> {
                mockInstanceUtil.terminateInstance(instanceMap, cloudInstance.getInstanceId());
            });
            return null;
        }, gson()::toJson);
    }

    private int getServerCount(List<InstanceGroup> instanceGroups) {
        int numberOfServers = 0;
        for (InstanceGroup instanceGroup : instanceGroups) {
            numberOfServers += instanceGroup.getNodeCount();
        }
        return numberOfServers;
    }

    private InstanceGroup getInstanceGroup(List<InstanceGroup> instanceGroups, String instanceGroupName) {
        for (InstanceGroup instanceGroup : instanceGroups) {
            if (instanceGroup.getName().equals(instanceGroupName)) {
                return instanceGroup;
            }
        }
        throw new IllegalStateException("There is no instance group with this name");
    }

    private void addAmbariMappings(Map<String, CloudVmMetaDataStatus> instanceMap, int port) {
        get(AMBARI_API_ROOT + "/check", new AmbariCheckResponse());
        get(AMBARI_API_ROOT + "/clusters/:cluster/requests/:request", new AmbariStatusResponse());
        get(AMBARI_API_ROOT + "/clusters", new AmbariClusterResponse(instanceMap));
        post(AMBARI_API_ROOT + "/clusters/:cluster/requests", new AmbariClusterRequestsResponse());
        post(AMBARI_API_ROOT + "/clusters/:cluster", new EmptyAmbariResponse());
        get(AMBARI_API_ROOT + "/clusters/:cluster", new AmbariClusterResponse(instanceMap));
        get(AMBARI_API_ROOT + "/hosts", new AmbariHostsResponse(instanceMap), gson()::toJson);
        get(AMBARI_API_ROOT + "/clusters/:cluster/hosts", new AmbariClustersHostsResponse(instanceMap));
        post(AMBARI_API_ROOT + "/clusters/:cluster/hosts", new AmbariClusterRequestsResponse());
        get(AMBARI_API_ROOT + "/clusters/ambari_cluster/configurations/service_config_versions", new AmbariServiceConfigResponse(mockServerAddress, port),
                gson()::toJson);
        get(AMBARI_API_ROOT + "/blueprints/*", (request, response) -> {
            response.type("text/plain");
            return responseFromJsonFile("blueprint/hdp-small-default.bp");
        });
        get(AMBARI_API_ROOT + "/clusters/:clusterName/hosts/:internalhostname", (request, response) -> {
            response.type("text/plain");
            ObjectNode rootNode = JsonNodeFactory.instance.objectNode();
            rootNode.putObject("Hosts").put("public_host_name", request.params("internalhostname"));
            return rootNode;
        });
        get(AMBARI_API_ROOT + "/clusters/:clusterName/services/HDFS/components/NAMENODE", (request, response) -> {
            response.type("text/plain");
            ObjectNode rootNode = JsonNodeFactory.instance.objectNode();
            ObjectNode nameNode = rootNode.putObject("metrics").putObject("dfs").putObject("namenode");
            ObjectNode liveNodesRoot = JsonNodeFactory.instance.objectNode();

            for (String instanceId : instanceMap.keySet()) {
                CloudVmMetaDataStatus cloudVmMetaDataStatus = instanceMap.get(instanceId);
                ObjectNode node = liveNodesRoot.putObject("host-" + cloudVmMetaDataStatus.getMetaData().getPrivateIp().replace(".", "-") + ".example.com");
                node.put("remaining", "10000000");
                node.put("usedSpace", Integer.valueOf(100000).toString());
                node.put("adminState", "In Service");
            }

            nameNode.put("LiveNodes", liveNodesRoot.toString());
            nameNode.put("DecomNodes", "{}");
            return rootNode;
        });
        put(AMBARI_API_ROOT + "/clusters/:cluster/host_components", new AmbariClusterRequestsResponse());
        delete(AMBARI_API_ROOT + "/clusters/:cluster/hosts/:hostname", new AmbariClusterRequestsResponse());
        post(AMBARI_API_ROOT + "/users", new EmptyAmbariResponse());
    }

}