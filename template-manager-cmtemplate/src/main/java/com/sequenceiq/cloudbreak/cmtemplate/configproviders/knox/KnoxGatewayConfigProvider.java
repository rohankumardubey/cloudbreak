package com.sequenceiq.cloudbreak.cmtemplate.configproviders.knox;

import static com.sequenceiq.cloudbreak.cmtemplate.configproviders.ConfigUtils.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import com.cloudera.api.swagger.model.ApiClusterTemplateConfig;
import com.cloudera.api.swagger.model.ApiClusterTemplateRoleConfigGroup;
import com.cloudera.api.swagger.model.ApiClusterTemplateService;
import com.google.common.annotations.VisibleForTesting;
import com.sequenceiq.cloudbreak.auth.altus.UmsRight;
import com.sequenceiq.cloudbreak.auth.altus.VirtualGroupService;
import com.sequenceiq.cloudbreak.cmtemplate.CmTemplateProcessor;
import com.sequenceiq.cloudbreak.cmtemplate.configproviders.AbstractRoleConfigProvider;
import com.sequenceiq.cloudbreak.dto.KerberosConfig;
import com.sequenceiq.cloudbreak.template.TemplatePreparationObject;
import com.sequenceiq.cloudbreak.auth.altus.VirtualGroupRequest;
import com.sequenceiq.cloudbreak.template.model.GeneralClusterConfigs;
import com.sequenceiq.cloudbreak.template.views.GatewayView;
import com.sequenceiq.cloudbreak.template.views.HostgroupView;
import com.sequenceiq.common.api.type.InstanceGroupType;

@Component
public class KnoxGatewayConfigProvider extends AbstractRoleConfigProvider {

    private static final String KNOX_SERVICE_REF_NAME = "knox";

    private static final String KNOX_GATEWAY_REF_NAME = "knox-KNOX_GATEWAY-BASE";

    private static final String KNOX_MASTER_SECRET = "gateway_master_secret";

    private static final String IDBROKER_MASTER_SECRET = "idbroker_master_secret";

    private static final String GATEWAY_PATH = "gateway_path";

    private static final String SIGNING_KEYSTORE_NAME = "gateway_signing_keystore_name";

    private static final String SIGNING_KEYSTORE_TYPE = "gateway_signing_keystore_type";

    private static final String SIGNING_KEY_ALIAS = "gateway_signing_key_alias";

    private static final String SIGNING_JKS = "signing.jks";

    private static final String JKS = "JKS";

    private static final String SIGNING_IDENTITY = "signing-identity";

    private static final String GATEWAY_ADMIN_GROUPS = "gateway_knox_admin_groups";

    private static final String IDBROKER_GATEWAY_ADMIN_GROUPS = "idbroker_gateway_knox_admin_groups";

    private static final String GATEWAY_WHITELIST = "gateway_dispatch_whitelist";

    private static final String GATEWAY_DEFAULT_TOPOLOGY_NAME = "gateway_default_topology_name";

    private static final String DEFAULT_TOPOLOGY = "cdp-proxy";

    private static final String GATEWAY_CM_AUTO_DISCOVERY_ENABLED = "gateway_auto_discovery_enabled";

    @Inject
    private VirtualGroupService virtualGroupService;

    @Override
    protected List<ApiClusterTemplateConfig> getRoleConfigs(String roleType, TemplatePreparationObject source) {
        GatewayView gateway = source.getGatewayView();
        GeneralClusterConfigs generalClusterConfigs = source.getGeneralClusterConfigs();
        String masterSecret = gateway != null ? gateway.getMasterSecret() : generalClusterConfigs.getPassword();
        String topologyName = gateway != null && gateway.getExposedServices() != null ? gateway.getTopologyName() : DEFAULT_TOPOLOGY;

        VirtualGroupRequest virtualGroupRequest = source.getVirtualGroupRequest();
        String adminGroup = virtualGroupService.getVirtualGroup(virtualGroupRequest, UmsRight.KNOX_ADMIN.getRight());

        switch (roleType) {
            case KnoxRoles.KNOX_GATEWAY:
                List<ApiClusterTemplateConfig> config = new ArrayList<>();
                config.add(config(KNOX_MASTER_SECRET, masterSecret));
                config.add(config(GATEWAY_DEFAULT_TOPOLOGY_NAME, topologyName));
                config.add(config(GATEWAY_ADMIN_GROUPS, adminGroup));
                config.add(config(GATEWAY_CM_AUTO_DISCOVERY_ENABLED, "false"));
                if (gateway != null) {
                    config.add(config(GATEWAY_PATH, gateway.getPath()));
                    config.add(config(SIGNING_KEYSTORE_NAME, SIGNING_JKS));
                    config.add(config(SIGNING_KEYSTORE_TYPE, JKS));
                    config.add(config(SIGNING_KEY_ALIAS, SIGNING_IDENTITY));
                    config.add(getGatewayWhitelistConfig(source));
                }
                return config;
            case KnoxRoles.IDBROKER:
                return List.of(
                    config(IDBROKER_MASTER_SECRET, masterSecret),
                    config(IDBROKER_GATEWAY_ADMIN_GROUPS, adminGroup)
                );
            default:
                return List.of();
        }
    }

    @VisibleForTesting
    ApiClusterTemplateConfig getGatewayWhitelistConfig(TemplatePreparationObject source) {
        String whitelist;
        Optional<KerberosConfig> kerberosConfig = source.getKerberosConfig();
        if (kerberosConfig.isPresent()) {
            String domain = kerberosConfig.get().getDomain();
            if (source.getGeneralClusterConfigs().getAutoTlsEnabled()) {
                // HTTPS only whitelist when AutoTLS enabled
                whitelist = "^/.*$;^https://(.+." + domain + "):[0-9]+/?.*$";
            } else {
                // HTTP or HTTPS whitelist when AutoTLS disabled
                whitelist = "^/.*$;^https?://(.+." + domain + "):[0-9]+/?.*$";
            }
        } else {
            // Allow all when Kerberos isn't used
            whitelist = "^*.*$";
        }
        return config(GATEWAY_WHITELIST, whitelist);
    }

    @Override
    public Map<String, ApiClusterTemplateService> getAdditionalServices(CmTemplateProcessor cmTemplateProcessor, TemplatePreparationObject source) {
        if (source.getGatewayView() != null && cmTemplateProcessor.getServiceByType(KnoxRoles.KNOX).isEmpty()) {
            ApiClusterTemplateService knox = createBaseKnoxService();
            Set<HostgroupView> hostgroupViews = source.getHostgroupViews();
            return hostgroupViews.stream()
                    .filter(hg -> InstanceGroupType.GATEWAY.equals(hg.getInstanceGroupType()))
                    .collect(Collectors.toMap(HostgroupView::getName, v -> knox));
        }
        return Map.of();
    }

    private ApiClusterTemplateService createBaseKnoxService() {
        ApiClusterTemplateService knox = new ApiClusterTemplateService().serviceType(KnoxRoles.KNOX).refName(KNOX_SERVICE_REF_NAME);
        ApiClusterTemplateRoleConfigGroup knoxGateway = new ApiClusterTemplateRoleConfigGroup()
                .roleType(KnoxRoles.KNOX_GATEWAY).base(true).refName(KNOX_GATEWAY_REF_NAME);
        knox.roleConfigGroups(List.of(knoxGateway));
        return knox;
    }

    @Override
    public String getServiceType() {
        return KnoxRoles.KNOX;
    }

    @Override
    public List<String> getRoleTypes() {
        return List.of(KnoxRoles.KNOX_GATEWAY, KnoxRoles.IDBROKER);
    }
}
