package com.sequenceiq.cloudbreak.core.flow2.cluster.provision;

import com.sequenceiq.cloudbreak.reactor.api.event.cluster.InstallClusterFailed;
import com.sequenceiq.cloudbreak.reactor.api.event.cluster.InstallClusterSuccess;
import com.sequenceiq.cloudbreak.reactor.api.event.cluster.StartClusterFailed;
import com.sequenceiq.cloudbreak.reactor.api.event.cluster.StartClusterSuccess;
import com.sequenceiq.cloudbreak.reactor.api.event.cluster.install.ExecutePostClusterManagerStartRecipesFailed;
import com.sequenceiq.cloudbreak.reactor.api.event.cluster.install.ExecutePostClusterManagerStartRecipesSuccess;
import com.sequenceiq.cloudbreak.reactor.api.event.cluster.install.ExecutePostInstallRecipesFailed;
import com.sequenceiq.cloudbreak.reactor.api.event.cluster.install.ExecutePostInstallRecipesSuccess;
import com.sequenceiq.cloudbreak.reactor.api.event.cluster.install.HandleClusterCreationSuccessFailed;
import com.sequenceiq.cloudbreak.reactor.api.event.cluster.install.HandleClusterCreationSuccessSuccess;
import com.sequenceiq.cloudbreak.reactor.api.event.cluster.install.PrepareDatalakeResourceFailed;
import com.sequenceiq.cloudbreak.reactor.api.event.cluster.install.PrepareDatalakeResourceSuccess;
import com.sequenceiq.cloudbreak.reactor.api.event.cluster.install.PrepareExtendedTemplateFailed;
import com.sequenceiq.cloudbreak.reactor.api.event.cluster.install.PrepareExtendedTemplateSuccess;
import com.sequenceiq.cloudbreak.reactor.api.event.cluster.install.PrepareProxyConfigFailed;
import com.sequenceiq.cloudbreak.reactor.api.event.cluster.install.PrepareProxyConfigSuccess;
import com.sequenceiq.cloudbreak.reactor.api.event.cluster.install.SetupMonitoringFailed;
import com.sequenceiq.cloudbreak.reactor.api.event.cluster.install.SetupMonitoringSuccess;
import com.sequenceiq.cloudbreak.reactor.api.event.cluster.install.WaitForClusterManagerFailed;
import com.sequenceiq.cloudbreak.reactor.api.event.cluster.install.WaitForClusterManagerSuccess;
import com.sequenceiq.cloudbreak.reactor.api.event.kerberos.KeytabConfigurationFailed;
import com.sequenceiq.cloudbreak.reactor.api.event.kerberos.KeytabConfigurationSuccess;
import com.sequenceiq.cloudbreak.reactor.api.event.ldap.LdapSSOConfigurationFailed;
import com.sequenceiq.cloudbreak.reactor.api.event.ldap.LdapSSOConfigurationSuccess;
import com.sequenceiq.cloudbreak.reactor.api.event.orchestration.BootstrapFreeIPAEndpointSuccess;
import com.sequenceiq.cloudbreak.reactor.api.event.orchestration.BootstrapMachinesFailed;
import com.sequenceiq.cloudbreak.reactor.api.event.orchestration.BootstrapMachinesSuccess;
import com.sequenceiq.cloudbreak.reactor.api.event.orchestration.BootstrapPublicEndpointSuccess;
import com.sequenceiq.cloudbreak.reactor.api.event.orchestration.ClusterProxyGatewayRegistrationFailed;
import com.sequenceiq.cloudbreak.reactor.api.event.orchestration.ClusterProxyGatewayRegistrationSuccess;
import com.sequenceiq.cloudbreak.reactor.api.event.orchestration.ClusterProxyRegistrationFailed;
import com.sequenceiq.cloudbreak.reactor.api.event.orchestration.ClusterProxyRegistrationSuccess;
import com.sequenceiq.cloudbreak.reactor.api.event.orchestration.HostMetadataSetupFailed;
import com.sequenceiq.cloudbreak.reactor.api.event.orchestration.HostMetadataSetupSuccess;
import com.sequenceiq.cloudbreak.reactor.api.event.orchestration.StartAmbariServicesFailed;
import com.sequenceiq.cloudbreak.reactor.api.event.orchestration.StartClusterManagerServicesSuccess;
import com.sequenceiq.cloudbreak.reactor.api.event.recipe.UploadRecipesFailed;
import com.sequenceiq.cloudbreak.reactor.api.event.recipe.UploadRecipesSuccess;
import com.sequenceiq.flow.core.FlowEvent;
import com.sequenceiq.flow.event.EventSelectorUtil;

public enum ClusterCreationEvent implements FlowEvent {
    CLUSTER_CREATION_EVENT("CLUSTER_PROVISION_TRIGGER_EVENT"),
    CLUSTER_INSTALL_EVENT("CLUSTER_INSTALL_TRIGGER_EVENT"),
    BOOTSTRAP_MACHINES_FINISHED_EVENT(EventSelectorUtil.selector(BootstrapMachinesSuccess.class)),
    BOOTSTRAP_MACHINES_FAILED_EVENT(EventSelectorUtil.selector(BootstrapMachinesFailed.class)),
    CLUSTER_PROXY_REGISTRATION_SUCCEEDED_EVENT(EventSelectorUtil.selector(ClusterProxyRegistrationSuccess.class)),
    CLUSTER_PROXY_GATEWAY_REGISTRATION_SUCCEEDED_EVENT(EventSelectorUtil.selector(ClusterProxyGatewayRegistrationSuccess.class)),
    CLUSTER_PROXY_REGISTRATION_FAILED_EVENT(EventSelectorUtil.selector(ClusterProxyRegistrationFailed.class)),
    CLUSTER_PROXY_GATEWAY_REGISTRATION_FAILED_EVENT(EventSelectorUtil.selector(ClusterProxyGatewayRegistrationFailed.class)),
    HOST_METADATASETUP_FINISHED_EVENT(EventSelectorUtil.selector(HostMetadataSetupSuccess.class)),
    HOST_METADATASETUP_FAILED_EVENT(EventSelectorUtil.selector(HostMetadataSetupFailed.class)),
    CLEANUP_FREEIPA_FINISHED_EVENT("CLEANUP_FREEIPA_FINISHED"),
    CLEANUP_FREEIPA_FAILED_EVENT("CLEANUP_FREEIPA_FAILED_EVENT"),
    BOOTSTRAP_PUBLIC_ENDPOINT_FINISHED_EVENT(EventSelectorUtil.selector(BootstrapPublicEndpointSuccess.class)),
    BOOTSTRAP_FREEIPA_ENDPOINT_FINISHED_EVENT(EventSelectorUtil.selector(BootstrapFreeIPAEndpointSuccess.class)),
    UPLOAD_RECIPES_FINISHED_EVENT(EventSelectorUtil.selector(UploadRecipesSuccess.class)),
    UPLOAD_RECIPES_FAILED_EVENT(EventSelectorUtil.selector(UploadRecipesFailed.class)),
    CONFIGURE_KEYTABS_FINISHED_EVENT(EventSelectorUtil.selector(KeytabConfigurationSuccess.class)),
    CONFIGURE_KEYTABS_FAILED_EVENT(EventSelectorUtil.selector(KeytabConfigurationFailed.class)),
    START_AMBARI_SERVICES_FINISHED_EVENT(EventSelectorUtil.selector(StartClusterManagerServicesSuccess.class)),
    START_AMBARI_SERVICES_FAILED_EVENT(EventSelectorUtil.selector(StartAmbariServicesFailed.class)),
    LDAP_SSO_CONFIGURATION_FINISHED_EVENT(EventSelectorUtil.selector(LdapSSOConfigurationSuccess.class)),
    LDAP_SSO_CONFIGURATION_FAILED_EVENT(EventSelectorUtil.selector(LdapSSOConfigurationFailed.class)),
    WAIT_FOR_CLUSTER_MANAGER_FINISHED_EVENT(EventSelectorUtil.selector(WaitForClusterManagerSuccess.class)),
    WAIT_FOR_CLUSTER_MANAGER_FAILED_EVENT(EventSelectorUtil.selector(WaitForClusterManagerFailed.class)),
    EXECUTE_POST_CLUSTER_MANAGER_START_RECIPES_FINISHED_EVENT(EventSelectorUtil.selector(ExecutePostClusterManagerStartRecipesSuccess.class)),
    EXECUTE_POST_CLUSTER_MANAGER_START_RECIPES_FAILED_EVENT(EventSelectorUtil.selector(ExecutePostClusterManagerStartRecipesFailed.class)),
    PREPARE_PROXY_CONFIG_FINISHED_EVENT(EventSelectorUtil.selector(PrepareProxyConfigSuccess.class)),
    PREPARE_PROXY_CONFIG_FAILED_EVENT(EventSelectorUtil.selector(PrepareProxyConfigFailed.class)),
    SETUP_MONITORING_FINISHED_EVENT(EventSelectorUtil.selector(SetupMonitoringSuccess.class)),
    SETUP_MONITORING_FAILED_EVENT(EventSelectorUtil.selector(SetupMonitoringFailed.class)),
    PREPARE_EXTENDED_TEMPLATE_FINISHED_EVENT(EventSelectorUtil.selector(PrepareExtendedTemplateSuccess.class)),
    PREPARE_EXTENDED_TEMPLATE_FAILED_EVENT(EventSelectorUtil.selector(PrepareExtendedTemplateFailed.class)),
    START_AMBARI_FINISHED_EVENT(EventSelectorUtil.selector(StartClusterSuccess.class)),
    START_AMBARI_FAILED_EVENT(EventSelectorUtil.selector(StartClusterFailed.class)),
    EXECUTE_POST_INSTALL_RECIPES_FINISHED_EVENT(EventSelectorUtil.selector(ExecutePostInstallRecipesSuccess.class)),
    EXECUTE_POST_INSTALL_RECIPES_FAILED_EVENT(EventSelectorUtil.selector(ExecutePostInstallRecipesFailed.class)),
    HANDLE_CLUSTER_CREATION_SUCCESS_FINISHED_EVENT(EventSelectorUtil.selector(HandleClusterCreationSuccessSuccess.class)),
    HANDLE_CLUSTER_CREATION_SUCCESS_FAILED_EVENT(EventSelectorUtil.selector(HandleClusterCreationSuccessFailed.class)),
    PREPARE_DATALAKE_RESOURCE_FINISHED_EVENT(EventSelectorUtil.selector(PrepareDatalakeResourceSuccess.class)),
    PREPARE_DATALAKE_RESOURCE_FAILED_EVENT(EventSelectorUtil.selector(PrepareDatalakeResourceFailed.class)),
    INSTALL_CLUSTER_FINISHED_EVENT(EventSelectorUtil.selector(InstallClusterSuccess.class)),
    INSTALL_CLUSTER_FAILED_EVENT(EventSelectorUtil.selector(InstallClusterFailed.class)),
    CLUSTER_CREATION_FAILED_EVENT("CLUSTER_CREATION_FAILED"),
    CLUSTER_CREATION_FINISHED_EVENT("CLUSTER_CREATION_FINISHED"),
    CLUSTER_CREATION_FAILURE_HANDLED_EVENT("CLUSTER_CREATION_FAILHANDLED");

    private final String event;

    ClusterCreationEvent(String event) {
        this.event = event;
    }

    @Override
    public String event() {
        return event;
    }
}
