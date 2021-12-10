package com.sequenceiq.cloudbreak.auth.altus.model;

public enum Entitlement {

    DATAHUB_FLOW_SCALING,
    DATAHUB_STREAMING_SCALING,
    DATAHUB_DEFAULT_SCALING,
    CDP_AZURE,
    CDP_GCP,
    AUDIT_ARCHIVING_GCP,
    CDP_CB_AWS_NATIVE,
    CDP_CB_AWS_NATIVE_DATALAKE,
    CDP_CB_AWS_NATIVE_FREEIPA,
    CDP_CB_AWS_VARIANT_MIGRATION,
    CDP_BASE_IMAGE,
    CDP_AUTOMATIC_USERSYNC_POLLER,
    CDP_FREEIPA_HA_REPAIR,
    CDP_FREEIPA_REBUILD,
    CLOUDERA_INTERNAL_ACCOUNT,
    CDP_FMS_CLUSTER_PROXY,
    CDP_CLOUD_STORAGE_VALIDATION_ON_VM,
    CDP_CLOUD_STORAGE_VALIDATION,
    CDP_CLOUD_STORAGE_VALIDATION_AWS,
    CDP_CLOUD_STORAGE_VALIDATION_AZURE,
    CDP_CLOUD_STORAGE_VALIDATION_GCP,
    CDP_CM_HA,
    CDP_CM_DISABLE_AUTO_BUNDLE_COLLECTION,
    CDP_RAZ,
    CDP_RAW_S3,
    CDP_MICRO_DUTY_SDX,
    CDP_RUNTIME_UPGRADE,
    CDP_RUNTIME_UPGRADE_DATAHUB,
    CDP_OS_UPGRADE_DATAHUB,
    LOCAL_DEV,
    CDP_AZURE_SINGLE_RESOURCE_GROUP,
    CDP_AZURE_SINGLE_RESOURCE_GROUP_DEDICATED_STORAGE_ACCOUNT,
    CDP_AZURE_IMAGE_MARKETPLACE,
    CDP_CLOUD_IDENTITY_MAPPING,
    CDP_ALLOW_INTERNAL_REPOSITORY_FOR_UPGRADE,
    CDP_SDX_HBASE_CLOUD_STORAGE,
    CDP_DATA_LAKE_AWS_EFS,
    CB_AUTHZ_POWER_USERS,
    CDP_ALLOW_DIFFERENT_DATAHUB_VERSION_THAN_DATALAKE,
    DATAHUB_AWS_AUTOSCALING,
    DATAHUB_AWS_STOP_START_SCALING,
    DATAHUB_AZURE_AUTOSCALING,
    DATAHUB_AZURE_STOP_START_SCALING,
    DATAHUB_GCP_AUTOSCALING,
    DATAHUB_GCP_STOP_START_SCALING,
    CDP_CCM_V2,
    CDP_CCM_V2_JUMPGATE,
    CDP_CCM_V2_USE_ONE_WAY_TLS,
    CDP_CCM_V1_TO_V2_JUMPGATE_UPGRADE,
    CDP_CCM_V2_TO_V2_JUMPGATE_UPGRADE,
    CDP_CB_DATABASE_WIRE_ENCRYPTION,
    CDP_ENABLE_DISTROX_INSTANCE_TYPES,
    CDP_SHOW_CLI,
    PERSONAL_VIEW_CB_BY_RIGHT,
    CDP_DATA_LAKE_LOAD_BALANCER,
    CDP_EXPERIENCE_DELETION_BY_ENVIRONMENT,
    CDP_USE_DATABUS_CNAME_ENDPOINT,
    CDP_USE_CM_SYNC_COMMAND_POLLER,
    CDP_DATAHUB_NODESTATUS_CHECK,
    CDP_DATAHUB_METRICS_DATABUS_PROCESSING,
    CDP_DATALAKE_METRICS_DATABUS_PROCESSING,
    CDP_NODESTATUS_ENABLE_SALT_PING,
    CDP_VM_DIAGNOSTICS,
    CDP_FREEIPA_DATABUS_ENDPOINT_VALIDATION,
    CDP_DATAHUB_DATABUS_ENDPOINT_VALIDATION,
    CDP_DATALAKE_BACKUP_ON_UPGRADE,
    CDP_DATALAKE_BACKUP_ON_RESIZE,
    CDP_DATALAKE_RESIZE_RECOVERY,
    DATA_LAKE_LIGHT_TO_MEDIUM_MIGRATION,
    CDP_PUBLIC_ENDPOINT_ACCESS_GATEWAY_AZURE,
    CDP_PUBLIC_ENDPOINT_ACCESS_GATEWAY_GCP,
    FMS_FREEIPA_BATCH_CALL,
    CDP_CB_AZURE_DISK_SSE_WITH_CMK,
    CDP_USER_SYNC_CREDENTIALS_UPDATE_OPTIMIZATION,
    OJDBC_TOKEN_DH,
    OJDBC_TOKEN_DH_ONE_HOUR_TOKEN,
    CDP_ENDPOINT_GATEWAY_SKIP_VALIDATION,
    CDP_AWS_RESTRICTED_POLICY,
    CDP_CONCLUSION_CHECKER_SEND_USER_EVENT,
    CDP_ALLOW_HA_UPGRADE,
    CDP_ALLOW_HA_REPAIR,
    CDP_ALLOW_HA_DOWNSCALE,
    EPHEMERAL_DISKS_FOR_TEMP_DATA,
    CDP_CB_AWS_DISK_ENCRYPTION_WITH_CMK,
    CDP_FREEIPA_UPGRADE,
    CDP_CB_GCP_DISK_ENCRYPTION_WITH_CMEK,
    UI_EDP_PROGRESS_BAR,
    CDP_DATAHUB_CUSTOM_CONFIGS,
    CDP_DATA_LAKE_MEDIUM_DUTY_WITH_PROFILER,
    CDP_UNBOUND_ELIMINATION,
    CDP_CM_BULK_HOSTS_REMOVAL,
    E2E_TEST_ONLY;
}
