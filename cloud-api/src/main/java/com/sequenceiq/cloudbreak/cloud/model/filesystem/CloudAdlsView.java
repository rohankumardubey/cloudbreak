package com.sequenceiq.cloudbreak.cloud.model.filesystem;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sequenceiq.common.model.CloudIdentityType;

public class CloudAdlsView extends CloudFileSystemView {

    public static final String CREDENTIAL_SECRET_KEY = "secretKey";

    public static final String SUBSCRIPTION_ID = "subscriptionId";

    public static final String ACCESS_KEY = "accessKey";

    public static final String TENANT_ID = "tenantId";

    private String accountName;

    private String clientId;

    private String credential;

    private String tenantId;

    private String adlsTrackingClusterNameKey;

    private String resourceGroupName;

    private String adlsTrackingClusterTypeKey;

    @JsonCreator
    public CloudAdlsView(@JsonProperty("cloudIdentityType") CloudIdentityType cloudIdentityType) {
        super(cloudIdentityType);
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getCredential() {
        return credential;
    }

    public void setCredential(String credential) {
        this.credential = credential;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getAdlsTrackingClusterNameKey() {
        return adlsTrackingClusterNameKey;
    }

    public void setAdlsTrackingClusterNameKey(String adlsTrackingClusterNameKey) {
        this.adlsTrackingClusterNameKey = adlsTrackingClusterNameKey;
    }

    public String getResourceGroupName() {
        return resourceGroupName;
    }

    public void setResourceGroupName(String resourceGroupName) {
        this.resourceGroupName = resourceGroupName;
    }

    public String getAdlsTrackingClusterTypeKey() {
        return adlsTrackingClusterTypeKey;
    }

    public void setAdlsTrackingClusterTypeKey(String adlsTrackingClusterTypeKey) {
        this.adlsTrackingClusterTypeKey = adlsTrackingClusterTypeKey;
    }
}
