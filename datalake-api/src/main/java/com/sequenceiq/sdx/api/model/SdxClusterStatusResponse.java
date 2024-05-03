package com.sequenceiq.sdx.api.model;

public enum SdxClusterStatusResponse {

    REQUESTED,
    WAIT_FOR_ENVIRONMENT,
    ENVIRONMENT_CREATED,
    STACK_CREATION_IN_PROGRESS,
    STACK_CREATION_FINISHED,
    STACK_DELETED,
    STACK_DELETION_IN_PROGRESS,
    EXTERNAL_DATABASE_CREATION_IN_PROGRESS,
    EXTERNAL_DATABASE_DELETION_IN_PROGRESS,
    EXTERNAL_DATABASE_START_IN_PROGRESS,
    EXTERNAL_DATABASE_STARTED,
    EXTERNAL_DATABASE_STOP_IN_PROGRESS,
    EXTERNAL_DATABASE_STOPPED,
    EXTERNAL_DATABASE_CREATED,
    RUNNING,
    PROVISIONING_FAILED,
    REPAIR_IN_PROGRESS,
    REPAIR_FAILED,
    CHANGE_IMAGE_IN_PROGRESS,
    DATALAKE_UPGRADE_IN_PROGRESS,
    DATALAKE_ROLLING_UPGRADE_IN_PROGRESS,
    DATALAKE_UPGRADE_FAILED,
    RECOVERY_IN_PROGRESS,
    RECOVERY_FAILED,
    DELETE_REQUESTED,
    DELETED,
    DELETE_FAILED,
    DELETED_ON_PROVIDER_SIDE,
    START_IN_PROGRESS,
    START_FAILED,
    STOP_IN_PROGRESS,
    STOP_FAILED,
    STOPPED,
    CLUSTER_AMBIGUOUS,
    CLUSTER_UNREACHABLE,
    NODE_FAILURE,
    SYNC_FAILED,
    CERT_ROTATION_IN_PROGRESS,
    CERT_ROTATION_FAILED,
    CERT_ROTATION_FINISHED,
    CERT_RENEWAL_IN_PROGRESS,
    CERT_RENEWAL_FAILED,
    CERT_RENEWAL_FINISHED,
    DATALAKE_BACKUP_INPROGRESS,
    DATALAKE_BACKUP_VALIDATION_INPROGRESS,
    DATALAKE_RESTORE_INPROGRESS,
    DATALAKE_RESTORE_VALIDATION_INPROGRESS,
    DATALAKE_RESTORE_FAILED,
    DATALAKE_DETACHED,
    DATALAKE_UPGRADE_CCM_IN_PROGRESS,
    DATALAKE_UPGRADE_CCM_FAILED,
    DATALAKE_PROXY_CONFIG_MODIFICATION_IN_PROGRESS,
    DATALAKE_PROXY_CONFIG_MODIFICATION_FAILED,
    DATAHUB_REFRESH_IN_PROGRESS,
    DATAHUB_REFRESH_FAILED,
    SALT_PASSWORD_ROTATION_IN_PROGRESS,
    SALT_PASSWORD_ROTATION_FAILED,
    SALT_PASSWORD_ROTATION_FINISHED,
    DATALAKE_UPGRADE_PREPARATION_IN_PROGRESS,
    DATALAKE_UPGRADE_PREPARATION_FAILED,
    DATALAKE_VERTICAL_SCALE_VALIDATION_IN_PROGRESS,
    DATALAKE_VERTICAL_SCALE_ON_DATALAKE_IN_PROGRESS,
    DATALAKE_VERTICAL_SCALE_ON_DATALAKE_FAILED,
    DATALAKE_VERTICAL_SCALE_FAILED,
    DATALAKE_VERTICAL_SCALE_VALIDATION_FAILED,
    DATALAKE_HORIZONTAL_SCALE_VALIDATION_IN_PROGRESS,
    DATALAKE_HORIZONTAL_SCALE_VALIDATION_FAILED,
    DATALAKE_HORIZONTAL_SCALE_IN_PROGRESS,
    DATALAKE_HORIZONTAL_SCALE_SERVICES_RESTART_IN_PROGRESS,
    DATALAKE_HORIZONTAL_SCALE_FINISHED,
    DATALAKE_HORIZONTAL_SCALE_FAILED,
    DATALAKE_UPGRADE_DATABASE_SERVER_REQUESTED,
    DATALAKE_UPGRADE_DATABASE_SERVER_IN_PROGRESS,
    DATALAKE_UPGRADE_DATABASE_SERVER_FAILED,
    DATALAKE_UPGRADE_DATABASE_SERVER_FINISHED,
    DATALAKE_DISK_UPDATE_VALIDATION_IN_PROGRESS,
    DATALAKE_DISK_UPDATE_VALIDATION_FAILED,
    DATALAKE_DISK_UPDATE_IN_PROGRESS,
    DATALAKE_DISK_UPDATE_FAILED,
    SALT_UPDATE_IN_PROGRESS,
    SALT_UPDATE_FAILED,
    SALT_UPDATE_FINISHED,
    DATALAKE_SECRET_ROTATION_IN_PROGRESS,
    DATALAKE_SECRET_ROTATION_FINISHED,
    DATALAKE_SECRET_ROTATION_FAILED,
    DATALAKE_SECRET_ROTATION_ROLLBACK_IN_PROGRESS,
    DATALAKE_SECRET_ROTATION_ROLLBACK_FINISHED,
    DATALAKE_SECRET_ROTATION_ROLLBACK_FAILED,
    DATALAKE_SECRET_ROTATION_FINALIZE_IN_PROGRESS,
    DATALAKE_SECRET_ROTATION_FINALIZE_FAILED,
    DATALAKE_ADD_VOLUMES_IN_PROGRESS,
    DATALAKE_ADD_VOLUMES_FAILED,
    DATALAKE_IMD_UPDATE_IN_PROGRESS,
    DATALAKE_IMD_UPDATE_FAILED,
    DATALAKE_IMD_UPDATE_FINISHED;

    public boolean isRunning() {
        return RUNNING.equals(this);
    }

    public boolean isAvailable() {
        return RUNNING.equals(this) || DATALAKE_BACKUP_INPROGRESS.equals(this);
    }

    public boolean isRollingUpgradeInProgress() {
        return DATALAKE_ROLLING_UPGRADE_IN_PROGRESS.equals(this) || DATALAKE_UPGRADE_PREPARATION_IN_PROGRESS.equals(this);
    }
}
