package com.sequenceiq.datalake.service.upgrade;

import static com.sequenceiq.common.api.type.InstanceGroupName.AUXILIARY;
import static com.sequenceiq.common.api.type.InstanceGroupName.CORE;
import static com.sequenceiq.common.api.type.InstanceGroupName.GATEWAY;
import static com.sequenceiq.common.api.type.InstanceGroupName.IDBROKER;
import static com.sequenceiq.common.api.type.InstanceGroupName.MASTER;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sequenceiq.cloudbreak.api.endpoint.v4.stacks.request.osupgrade.OrderedOSUpgradeSetRequest;
import com.sequenceiq.cloudbreak.api.endpoint.v4.stacks.response.StackV4Response;
import com.sequenceiq.cloudbreak.api.endpoint.v4.stacks.response.instancegroup.InstanceGroupV4Response;
import com.sequenceiq.cloudbreak.api.endpoint.v4.stacks.response.instancegroup.instancemetadata.InstanceMetaDataV4Response;
import com.sequenceiq.cloudbreak.common.exception.CloudbreakServiceException;
import com.sequenceiq.common.api.type.InstanceGroupName;

@ExtendWith(MockitoExtension.class)
class OrderedOSUpgradeRequestProviderTest {

    @InjectMocks
    private OrderedOSUpgradeRequestProvider underTest;

    @Test
    void testCreateOrderedOSUpgradeSetRequest() {
        List<InstanceGroupV4Response> instanceGroups = new ArrayList<>();
        instanceGroups.add(createInstanceGroup(Set.of(
                createInstanceMetadata(CORE, 0),
                createInstanceMetadata(CORE, 1),
                createInstanceMetadata(CORE, 2)
        )));
        instanceGroups.add(createInstanceGroup(Set.of(
                createInstanceMetadata(MASTER, 0),
                createInstanceMetadata(MASTER, 1)
        )));
        instanceGroups.add(createInstanceGroup(Set.of(
                createInstanceMetadata(IDBROKER, 0),
                createInstanceMetadata(IDBROKER, 1)
        )));
        instanceGroups.add(createInstanceGroup(Set.of(
                createInstanceMetadata(GATEWAY, 0),
                createInstanceMetadata(GATEWAY, 1)
        )));
        instanceGroups.add(createInstanceGroup(Set.of(createInstanceMetadata(AUXILIARY, 0))));

        OrderedOSUpgradeSetRequest actual = underTest.createMediumDutyOrderedOSUpgradeSetRequest(createStackV4Response(instanceGroups));

        assertEquals(0, actual.getOrderedOsUpgradeSets().get(0).getOrder());
        assertEquals(1, actual.getOrderedOsUpgradeSets().get(1).getOrder());
        assertEquals(2, actual.getOrderedOsUpgradeSets().get(2).getOrder());
        assertThat(actual.getOrderedOsUpgradeSets().get(0).getInstanceIds(),
                containsInAnyOrder(Arrays.asList("i-master0", "i-core0", "i-auxiliary0", "i-idbroker0").toArray()));
        assertThat(actual.getOrderedOsUpgradeSets().get(1).getInstanceIds(),
                containsInAnyOrder(Set.of("i-master1", "i-core1", "i-gateway0", "i-idbroker1").toArray()));
        assertThat(actual.getOrderedOsUpgradeSets().get(2).getInstanceIds(),
                containsInAnyOrder(Set.of("i-core2", "i-gateway1").toArray()));
    }

    @Test
    void testCreateOrderedOSUpgradeSetRequestShouldThrowExceptionWhenThereAreMissingInstancesFromTheRequest() {
        List<InstanceGroupV4Response> instanceGroups = new ArrayList<>();
        instanceGroups.add(createInstanceGroup(Set.of(
                createInstanceMetadata(CORE, 0),
                createInstanceMetadata(CORE, 1),
                createInstanceMetadata(CORE, 2)
        )));
        instanceGroups.add(createInstanceGroup(Set.of(
                createInstanceMetadata(MASTER, 0),
                createInstanceMetadata(MASTER, 1)
        )));
        instanceGroups.add(createInstanceGroup(Set.of(
                createInstanceMetadata(IDBROKER, 0),
                createInstanceMetadata(IDBROKER, 1)
        )));
        instanceGroups.add(createInstanceGroup(Set.of(
                createInstanceMetadata(GATEWAY, 2),
                createInstanceMetadata(GATEWAY, 0),
                createInstanceMetadata(GATEWAY, 1)
        )));
        instanceGroups.add(createInstanceGroup(Set.of(createInstanceMetadata(AUXILIARY, 0))));

        Exception exception = assertThrows(CloudbreakServiceException.class,
                () -> underTest.createMediumDutyOrderedOSUpgradeSetRequest(createStackV4Response(instanceGroups)));

        assertEquals("The following instances are missing from the ordered OS upgrade request: [i-gateway2]", exception.getMessage());
    }

    @Test
    void testCreateOrderedOSUpgradeSetRequestShouldThrowExceptionWhenThereAreMissingInstances() {
        List<InstanceGroupV4Response> instanceGroups = new ArrayList<>();
        instanceGroups.add(createInstanceGroup(Set.of(
                createInstanceMetadata(CORE, 0),
                createInstanceMetadata(CORE, 2)
        )));
        instanceGroups.add(createInstanceGroup(Set.of(
                createInstanceMetadata(MASTER, 0),
                createInstanceMetadata(MASTER, 1)
        )));
        instanceGroups.add(createInstanceGroup(Set.of(
                createInstanceMetadata(IDBROKER, 0),
                createInstanceMetadata(IDBROKER, 1)
        )));
        instanceGroups.add(createInstanceGroup(Set.of(
                createInstanceMetadata(GATEWAY, 0),
                createInstanceMetadata(GATEWAY, 1)
        )));
        instanceGroups.add(createInstanceGroup(Set.of(createInstanceMetadata(AUXILIARY, 0))));

        assertThrows(CloudbreakServiceException.class, () -> underTest.createMediumDutyOrderedOSUpgradeSetRequest(createStackV4Response(instanceGroups)));
    }

    private InstanceGroupV4Response createInstanceGroup(Set<InstanceMetaDataV4Response> instanceMetaDataV4Responses) {
        InstanceGroupV4Response instanceGroupV4Response = new InstanceGroupV4Response();
        instanceGroupV4Response.setName(instanceMetaDataV4Responses.iterator().next().getInstanceGroup());
        instanceGroupV4Response.setMetadata(instanceMetaDataV4Responses);
        return instanceGroupV4Response;
    }

    private InstanceMetaDataV4Response createInstanceMetadata(InstanceGroupName instanceGroup, int order) {
        InstanceMetaDataV4Response instanceMetaDataV4Response = new InstanceMetaDataV4Response();
        instanceMetaDataV4Response.setInstanceGroup(instanceGroup.getName());
        instanceMetaDataV4Response.setInstanceId("i-" + instanceGroup.getName() + order);
        instanceMetaDataV4Response.setDiscoveryFQDN("test-dl-" + instanceGroup.getName() + order + ".test-env.cloudera.site");
        return instanceMetaDataV4Response;
    }

    private StackV4Response createStackV4Response(List<InstanceGroupV4Response> instanceGroups) {
        StackV4Response stackV4Response = new StackV4Response();
        stackV4Response.setInstanceGroups(instanceGroups);
        return stackV4Response;
    }
}