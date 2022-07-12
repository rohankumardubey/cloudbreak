package com.sequenceiq.cloudbreak.event;

public enum ResourceEvent {
    SDX_CLUSTER_CREATED("resource.sdx.created"),
    SDX_CLUSTER_PROVISION_STARTED("resource.sdx.provisionstarted"),
    SDX_CLUSTER_PROVISION_FINISHED("resource.sdx.provisionfinished"),
    SDX_CLUSTER_DELETED("resource.sdx.deleted"),
    SDX_CLUSTER_DELETION_STARTED("resource.sdx.deletionstarted"),
    SDX_CLUSTER_DELETION_FINISHED("resource.sdx.deletionfinished"),
    SDX_CLUSTER_DELETION_FAILED("resource.sdx.deletionfailed"),
    SDX_CLUSTER_DELETED_ON_PROVIDER_SIDE("resource.sdx.deletedonproviderside"),
    SDX_CLUSTER_CREATION_FAILED("resource.sdx.failed"),
    SDX_RDS_DELETION_STARTED("resource.sdx.rdsdeletionstarted"),
    SDX_RDS_DELETION_FAILED("resource.sdx.rdsdeletionfailed"),
    SDX_RDS_DELETION_FINISHED("resource.sdx.rdsdeletionfinished"),
    SDX_RDS_CREATION_STARTED("resource.sdx.rdscreationstarted"),
    SDX_RDS_CREATION_FAILED("resource.sdx.rdscreationfailed"),
    SDX_RDS_CREATION_FINISHED("resource.sdx.rdscreationfinished"),
    SDX_RDS_START_STARTED("resource.sdx.rdsstartstarted"),
    SDX_RDS_START_FAILED("resource.sdx.rdsstartfailed"),
    SDX_RDS_START_FINISHED("resource.sdx.rdsstartfinished"),
    SDX_RDS_STOP_STARTED("resource.sdx.rdsstopstarted"),
    SDX_RDS_STOP_FAILED("resource.sdx.rdsstopfailed"),
    SDX_RDS_STOP_FINISHED("resource.sdx.rdsstopfinished"),
    SDX_WAITING_FOR_ENVIRONMENT("resource.sdx.envwait"),
    SDX_ENVIRONMENT_FINISHED("resource.sdx.envfinished"),
    SDX_REPAIR_STARTED("resource.sdx.repair.started"),
    SDX_REPAIR_FINISHED("resource.sdx.repair.finished"),
    SDX_REPAIR_FAILED("resource.sdx.repair.failed"),
    SDX_START_STARTED("resource.sdx.start.started"),
    SDX_START_FINISHED("resource.sdx.start.finished"),
    SDX_START_FAILED("resource.sdx.start.failed"),
    SDX_STOP_STARTED("resource.sdx.stop.started"),
    SDX_STOP_FINISHED("resource.sdx.stop.finished"),
    SDX_DETACH_STARTED("resource.sdx.detach.started"),
    SDX_DETACH_FINISHED("resource.sdx.detach.finished"),
    SDX_DETACHED_CLUSTER_DELETION_FAILED("resource.sdx.detach.deletion.failed"),
    UPDATE_LOAD_BALANCER_DNS_FINISHED("resource.sdx.update_lb_dns.finished"),
    UPDATE_LOAD_BALANCER_DNS_FAILED("resource.sdx.update_lb_dns.failed"),
    SDX_STOP_FAILED("resource.sdx.stop.failed"),
    SDX_VALIDATION_FAILED_AND_SKIPPED("resource.sdx.validation.skipped"),
    SDX_CHANGE_IMAGE_STARTED("resource.sdx.change.image.started"),
    DATALAKE_UPGRADE_STARTED("resource.sdx.datalake.upgrade.started"),
    DATALAKE_UPGRADE_PREPARATION_STARTED("resource.sdx.datalake.upgrade.preparation.started"),
    DATALAKE_UPGRADE_FINISHED("resource.sdx.datalake.upgrade.finished"),
    DATALAKE_UPGRADE_PREPARATION_FINISHED("resource.sdx.datalake.upgrade.preparation.finished"),
    DATALAKE_UPGRADE_FAILED("resource.sdx.datalake.upgrade.failed"),
    DATALAKE_UPGRADE_PREPARATION_FAILED("resource.sdx.datalake.upgrade.preparation.failed"),
    SDX_SYNC_FAILED("resource.sdx.sync.failed"),
    WORKSPACE_CREATED("resource.workspace.created"),
    WORKSPACE_DELETED("resource.workspace.deleted"),
    BLUEPRINT_CREATED("resource.blueprint.created"),
    BLUEPRINT_DELETED("resource.blueprint.deleted"),
    CREDENTIAL_CREATED("resource.credential.created"),
    CREDENTIAL_MODIFIED("resource.credential.modified"),
    CREDENTIAL_DELETED("resource.credential.deleted"),
    CLUSTER_TEMPLATE_CREATED("resource.clustertemplate.created"),
    CLUSTER_TEMPLATE_DELETED("resource.clustertemplate.deleted"),
    LDAP_CREATED("resource.ldap.created"),
    LDAP_DELETED("resource.ldap.deleted"),
    IMAGE_CATALOG_CREATED("resource.imagecatalog.created"),
    IMAGE_CATALOG_DELETED("resource.imagecatalog.deleted"),
    NETWORK_CREATED("resource.network.created"),
    NETWORK_DELETED("resource.network.deleted"),
    RECIPE_CREATED("resource.recipe.created"),
    RECIPE_DELETED("resource.recipe.deleted"),
    RDS_CONFIG_CREATED("resource.rdsconfig.created"),
    RDS_CONFIG_DELETED("resource.rdsconfig.deleted"),
    PROXY_CONFIG_CREATED("resource.proxyconfig.created"),
    PROXY_CONFIG_DELETED("resource.proxyconfig.deleted"),
    SECURITY_GROUP_CREATED("resource.securitygroup.created"),
    SECURITY_GROUP_DELETED("resource.securitygroup.deleted"),
    TEMPLATE_CREATED("resource.template.created"),
    TEMPLATE_DELETED("resource.template.deleted"),
    TOPOLOGY_CREATED("resource.topology.created"),
    FILESYSTEM_CREATED("resource.esystem.created"),
    FILESYSTEM_DELETED("resource.filesystem.deleted"),
    TOPOLOGY_DELETED("resource.topology.deleted"),
    MAINTENANCE_MODE_ENABLED("resource.maintenancemode.enabled"),
    MAINTENANCE_MODE_DISABLED("resource.maintenancemode.disabled"),
    TEST_CONNECTION_SUCCESS("resource.connection.success"),
    TEST_CONNECTION_FAILED("resource.connection.failed"),

    ENVIRONMENT_INITIALIZATION_STARTED("environment.initialization.started"),
    ENVIRONMENT_INITIALIZATION_FAILED("environment.initialization.failed"),
    ENVIRONMENT_VALIDATION_STARTED("environment.validation.started"),
    ENVIRONMENT_VALIDATION_FAILED("environment.validation.failed"),
    ENVIRONMENT_VALIDATION_FAILED_AND_SKIPPED("environment.validation.skipped"),
    ENVIRONMENT_STORAGE_CONSUMPTION_COLLECTION_SCHEDULING_STARTED("environment.storage.consumption.collection.scheduling.started"),
    ENVIRONMENT_STORAGE_CONSUMPTION_COLLECTION_SCHEDULING_FAILED("environment.storage.consumption.collection.scheduling.failed"),
    ENVIRONMENT_NETWORK_CREATION_STARTED("environment.network.creation.started"),
    ENVIRONMENT_NETWORK_CREATION_FAILED("environment.network.creation.failed"),
    ENVIRONMENT_PUBLICKEY_CREATION_STARTED("environment.publickey.creation.started"),
    ENVIRONMENT_PUBLICKEY_CREATION_FAILED("environment.publickey.creation.failed"),
    ENVIRONMENT_RESOURCE_ENCRYPTION_INITIALIZATION_STARTED("environment.resource.encryption.initialization.started"),
    ENVIRONMENT_RESOURCE_ENCRYPTION_INITIALIZATION_FAILED("environment.resource.encryption.initialization.failed"),
    ENVIRONMENT_FREEIPA_CREATION_STARTED("environment.freeipa.creation.started"),
    ENVIRONMENT_FREEIPA_CREATION_FAILED("environment.freeipa.creation.failed"),
    ENVIRONMENT_CREATION_FINISHED("environment.creation.finished"),
    ENVIRONMENT_CREATION_FAILED("environment.creation.failed"),
    ENVIRONMENT_SYNC_FINISHED("environment.sync.finished"),
    ENVIRONMENT_SALT_PASSWORD_ROTATE_FINISHED("environment.salt.passwordrotate.finished"),
    ENVIRONMENT_SALT_PASSWORD_ROTATE_FAILED("environment.salt.passwordrotate.failed"),

    ENVIRONMENT_NETWORK_DELETION_STARTED("environment.network.deletion.started"),
    ENVIRONMENT_PUBLICKEY_DELETION_STARTED("environment.publickey.deletion.started"),
    ENVIRONMENT_CLUSTER_DEFINITION_DELETE_STARTED("environment.clusterdefinition.deletion.started"),
    ENVIRONMENT_DATABASE_DELETION_STARTED("environment.database.deletion.started"),
    ENVIRONMENT_FREEIPA_DELETION_STARTED("environment.freeipa.deletion.started"),
    ENVIRONMENT_STORAGE_CONSUMPTION_COLLECTION_UNSCHEDULING_STARTED("environment.storage.consumption.collection.unscheduling.started"),
    ENVIRONMENT_EXPERIENCE_DELETION_STARTED("environment.xp.deletion.started"),
    ENVIRONMENT_IDBROKER_MAPPINGS_DELETION_STARTED("environment.idbroker.mappings.deletion.started"),
    ENVIRONMENT_S3GUARD_TABLE_DELETION_STARTED("environment.s3guard.table.deletion.started"),
    ENVIRONMENT_UMS_RESOURCE_DELETION_STARTED("environment.ums.resource.deletion.started"),
    ENVIRONMENT_DATAHUB_CLUSTERS_DELETION_STARTED("environment.datahubs.deletion.started"),
    ENVIRONMENT_DATALAKE_CLUSTERS_DELETION_STARTED("environment.datalakes.deletion.started"),
    ENVIRONMENT_RESOURCE_ENCRYPTION_DELETION_STARTED("environment.resource.encryption.deletion.started"),
    ENVIRONMENT_DELETION_FINISHED("environment.deletion.finished"),
    ENVIRONMENT_DELETION_FAILED("environment.deletion.failed"),

    ENVIRONMENT_STOP_DATAHUB_STARTED("environment.stop.datahub.started"),
    ENVIRONMENT_STOP_DATAHUB_FAILED("environment.stop.datahub.failed"),
    ENVIRONMENT_STOP_DATALAKE_STARTED("environment.stop.datalake.started"),
    ENVIRONMENT_STOP_DATALAKE_FAILED("environment.stop.datalake.failed"),
    ENVIRONMENT_STOP_FREEIPA_STARTED("environment.stop.freeipa.started"),
    ENVIRONMENT_STOP_FREEIPA_FAILED("environment.stop.freeipa.failed"),

    ENVIRONMENT_STOPPED("environment.stop.success"),
    ENVIRONMENT_STOP_FAILED("environment.stop.failed"),

    ENVIRONMENT_START_DATAHUB_STARTED("environment.start.datahub.started"),
    ENVIRONMENT_START_DATAHUB_FAILED("environment.start.datahub.failed"),
    ENVIRONMENT_RESTART_DATAHUB_STARTED("environment.restart.datahub.started"),
    ENVIRONMENT_RESTART_DATAHUB_FAILED("environment.restart.datahub.failed"),
    ENVIRONMENT_RESTART_DATAHUB_FINISHED("environment.restart.datahub.finished"),
    ENVIRONMENT_START_DATALAKE_STARTED("environment.start.datalake.started"),
    ENVIRONMENT_START_DATALAKE_FAILED("environment.start.datalake.failed"),
    ENVIRONMENT_START_FREEIPA_STARTED("environment.start.freeipa.started"),
    ENVIRONMENT_START_FREEIPA_FAILED("environment.start.freeipa.failed"),
    ENVIRONMENT_START_SYNCHRONIZE_USERS_STARTED("environment.start.syncusers.started"),
    ENVIRONMENT_START_SYNCHRONIZE_USERS_FAILED("environment.start.syncusers.failed"),

    ENVIRONMENT_STACK_CONFIGS_UPDATE_STARTED("environment.stack.config.updates.started"),
    ENVIRONMENT_STACK_CONFIGS_UPDATE_FINISHED("environment.stack.config.updates.finished"),
    ENVIRONMENT_STACK_CONFIGS_UPDATE_FAILED("environment.stack.config.updates.failed"),

    ENVIRONMENT_STARTED("environment.start.success"),
    ENVIRONMENT_START_FAILED("environment.start.failed"),

    ENVIRONMENT_LOAD_BALANCER_ENV_UPDATE_STARTED("environment.loadbalancer.update.env.started"),
    ENVIRONMENT_LOAD_BALANCER_ENV_UPDATE_FAILED("environment.loadbalancer.update.env.failed"),
    ENVIRONMENT_LOAD_BALANCER_STACK_UPDATE_STARTED("environment.loadbalancer.update.stack.started"),
    ENVIRONMENT_LOAD_BALANCER_STACK_UPDATE_FAILED("environment.loadbalancer.update.stack.failed"),
    ENVIRONMENT_LOAD_BALANCER_UPDATE_FINISHED("environment.loadbalancer.update.success"),
    ENVIRONMENT_LOAD_BALANCER_UPDATE_FAILED("environment.loadbalancer.update.failed"),

    ENVIRONMENT_UPGRADE_CCM_VALIDATION_STARTED("environment.upgrade.ccm.validation.started"),
    ENVIRONMENT_UPGRADE_CCM_VALIDATION_FAILED("environment.upgrade.ccm.validation.failed"),
    ENVIRONMENT_UPGRADE_CCM_ON_FREEIPA_STARTED("environment.upgrade.ccm.freeipa.started"),
    ENVIRONMENT_UPGRADE_CCM_ON_FREEIPA_FAILED("environment.upgrade.ccm.freeipa.failed"),
    ENVIRONMENT_UPGRADE_CCM_TUNNEL_UPDATE_STARTED("environment.upgrade.ccm.tunnelupdate.started"),
    ENVIRONMENT_UPGRADE_CCM_TUNNEL_UPDATE_FAILED("environment.upgrade.ccm.tunnelupdate.failed"),
    ENVIRONMENT_UPGRADE_CCM_ON_DATALAKE_STARTED("environment.upgrade.ccm.datalake.started"),
    ENVIRONMENT_UPGRADE_CCM_ON_DATALAKE_FAILED("environment.upgrade.ccm.datalake.failed"),
    ENVIRONMENT_UPGRADE_CCM_ON_DATAHUB_STARTED("environment.upgrade.ccm.datahub.started"),
    ENVIRONMENT_UPGRADE_CCM_ON_DATAHUB_FAILED("environment.upgrade.ccm.datahub.failed"),
    ENVIRONMENT_UPGRADE_CCM_ROLLING_BACK("environment.upgrade.ccm.rollback"),
    ENVIRONMENT_UPGRADE_CCM_FINISHED("environment.upgrade.ccm.finished"),
    ENVIRONMENT_UPGRADE_CCM_FAILED("environment.upgrade.ccm.failed"),

    CREDENTIAL_AZURE_INTERACTIVE_CREATED("credential.azure.interactive.created"),
    CREDENTIAL_AZURE_INTERACTIVE_STATUS("credential.azure.interactive.status"),
    CREDENTIAL_AZURE_INTERACTIVE_FAILED("credential.azure.interactive.failed"),

    STACK_STOP_IGNORED("stack.stop.ignored"),
    STACK_START_IGNORED("stack.start.ignored"),
    STACK_STOP_REQUESTED("stack.stop.requested"),
    STACK_CLEANUP_SERVICE_TRIGGER_SYNC("stack.cleanup.service.trigger.sync"),
    STACK_SYNC_INSTANCE_STATUS_RETRIEVAL_FAILED("stack.sync.instance.status.retrieval.failed"),
    STACK_SYNC_INSTANCE_STATUS_COULDNT_DETERMINE("stack.sync.instance.status.couldnt.determine"),
    STACK_SYNC_INSTANCE_OPERATION_IN_PROGRESS("stack.sync.instance.operation.in.progress"),
    STACK_SYNC_INSTANCE_STOPPED_ON_PROVIDER("stack.sync.instance.stopped.on.provider"),
    STACK_SYNC_HOST_DELETED("stack.sync.host.deleted"),
    STACK_SYNC_INSTANCE_REMOVAL_FAILED("stack.sync.instance.removal.failed"),
    STACK_SYNC_HOST_UPDATED("stack.sync.host.updated"),
    STACK_SYNC_INSTANCE_TERMINATED("stack.sync.instance.terminated"),
    STACK_SYNC_INSTANCE_DELETED_CBMETADATA("stack.sync.instance.deleted.cbmetadata"),
    STACK_SYNC_INSTANCE_DELETED_BY_PROVIDER_CBMETADATA("stack.sync.instance.deletedbyprovider.cbmetadata"),
    STACK_SYNC_VERSIONS_FROM_CM_TO_DB_SUCCESS("stack.sync.versions.from.cm.to.db.succeeded"),
    STACK_SYNC_VERSIONS_FROM_CM_TO_DB_FAILED("stack.sync.versions.from.cm.to.db.failed"),
    STACK_DELETE_IN_PROGRESS("stack.delete.in.progress"),
    STACK_ADDING_INSTANCES("stack.adding.instances"),
    STACK_METADATA_EXTEND_WITH_COUNT("stack.metadata.extend.with.count"),
    STACK_INFRASTRUCTURE_UPDATE_FAILED("stack.infrastructure.update.failed"),
    STACK_IMAGE_UPDATE_STARTED("stack.image.update.started"),
    STACK_IMAGE_UPDATE_FINISHED("stack.image.update.finished"),
    STACK_IMAGE_UPDATE_FAILED("stack.image.update.failed"),
    STACK_IMAGE_SETUP("stack.image.setup"),
    STACK_PROVISIONING("stack.provisioning"),
    STACK_INFRASTRUCTURE_TIME("stack.infrastructure.time"),
    STACK_INFRASTRUCTURE_CREATE_FAILED("stack.infrastructure.create.failed"),
    STACK_INFRASTRUCTURE_ROLLBACK("stack.infrastructure.rollback"),
    STACK_INFRASTRUCTURE_ROLLBACK_FAILED("stack.infrastructure.rollback.failed"),
    STACK_REMOVING_INSTANCE("stack.removing.instance"),
    STACK_SCALING_TERMINATING_HOST_FROM_HOSTGROUP("stack.scaling.terminating.host.from.hostgroup"),
    STACK_REMOVING_INSTANCE_FINISHED("stack.removing.instance.finished"),
    STACK_REMOVING_INSTANCE_FAILED("stack.removing.instance.failed"),
    STACK_INFRASTRUCTURE_BOOTSTRAP("stack.infrastructure.bootstrap"),
    STACK_GATEWAY_CERTIFICATE_CREATE_SKIPPED("stack.gateway.certificate.create.skipped"),
    STACK_DOWNSCALE_INSTANCES("stack.downscale.instances"),
    STACK_DOWNSCALE_SUCCESS("stack.downscale.success"),
    STACK_DOWNSCALE_FAILED("stack.downscale.failed"),
    STACK_UPSCALE_QUOTA_ISSUE("stack.upscale.quota.issue"),
    STACK_SELECT_FOR_DOWNSCALE("stack.select.for.downscale"),
    STACK_REPAIR_COMPLETE_CLEAN("stack.repair.complete.clean"),
    STACK_REPAIR_ATTEMPTING("stack.repair.attempting"),
    STACK_REPAIR_TRIGGERED("stack.repair.triggered"),
    STACK_DELETE_COMPLETED("stack.delete.completed"),
    STACK_INFRASTRUCTURE_DELETE_FAILED("stack.infrastructure.delete.failed"),
    STACK_FORCED_DELETE_COMPLETED("stack.forced.delete.completed"),
    STACK_INFRASTRUCTURE_STARTING("stack.infrastructure.starting"),
    STACK_INFRASTRUCTURE_STARTED("stack.infrastructure.started"),
    STACK_INFRASTRUCTURE_STOPPING("stack.infrastructure.stopping"),
    STACK_INFRASTRUCTURE_STOPPED("stack.infrastructure.stopped"),
    STACK_INFRASTRUCTURE_START_FAILED("stack.infrastructure.start.failed"),
    STACK_INFRASTRUCTURE_STOP_FAILED("stack.infrastructure.stop.failed"),
    STACK_REPAIR_DETECTION_STARTED("stack.repair.detection.started"),
    STACK_INSTANCE_METADATA_RESTORED("stack.instance.metadata.restored"),
    STACK_REPAIR_FAILED("stack.repair.failed"),
    STACK_DATALAKE_UPDATE("stack.datalake.update"),
    STACK_DATALAKE_UPDATE_FINISHED("stack.datalake.update.finished"),
    STACK_DATALAKE_UPDATE_FAILED("stack.datalake.update.failed"),
    STACK_DIAGNOSTICS_SALT_VALIDATION_RUNNING("stack.diagnostics.salt.validation.running"),
    STACK_DIAGNOSTICS_SALT_VALIDATION_RUNNING_SKIP_UNRESPONSIVE("stack.diagnostics.salt.validation.running.skip.unresponsive"),
    STACK_DIAGNOSTICS_SALT_PILLAR_UPDATE_RUNNING("stack.diagnostics.salt.pillar.update.running"),
    STACK_DIAGNOSTICS_SALT_STATE_UPDATE_RUNNING("stack.diagnostics.salt.state.update.running"),
    STACK_DIAGNOSTICS_PREFLIGHT_CHECK_RUNNING("stack.diagnostics.preflight.check.running"),
    STACK_DIAGNOSTICS_PREFLIGHT_CHECK_FINISHED("stack.diagnostics.preflight.check.finished"),
    STACK_DIAGNOSTICS_INIT_RUNNING("stack.diagnostics.init.running"),
    STACK_DIAGNOSTICS_VM_PREFLIGHT_CHECK_RUNNING("stack.diagnostics.vm.preflight.check.running"),
    STACK_DIAGNOSTICS_TELEMETRY_UPGRADE_RUNNING("stack.diagnostics.telemetry.upgrade.running"),
    STACK_DIAGNOSTICS_ENSURE_MACHINE_USER("stack.diagnostics.ensure.machine.user"),
    STACK_DIAGNOSTICS_COLLECTION_RUNNING("stack.diagnostics.collection.running"),
    STACK_DIAGNOSTICS_UPLOAD_RUNNING("stack.diagnostics.upload.running"),
    STACK_DIAGNOSTICS_CLEANUP_RUNNING("stack.diagnostics.cleanup.running"),
    STACK_DIAGNOSTICS_COLLECTION_FINISHED("stack.diagnostics.collection.finished"),
    STACK_DIAGNOSTICS_COLLECTION_FAILED("stack.diagnostics.collection.failed"),
    STACK_CM_DIAGNOSTICS_INIT_RUNNING("stack.cm.diagnostics.init.running"),
    STACK_CM_DIAGNOSTICS_COLLECTION_RUNNING("stack.cm.diagnostics.collection.running"),
    STACK_CM_DIAGNOSTICS_UPLOAD_RUNNING("stack.cm.diagnostics.upload.running"),
    STACK_CM_DIAGNOSTICS_CLEANUP_RUNNING("stack.cm.diagnostics.cleanup.running"),
    STACK_CM_DIAGNOSTICS_COLLECTION_FINISHED("stack.cm.diagnostics.collection.finished"),
    STACK_CM_DIAGNOSTICS_COLLECTION_FAILED("stack.cm.diagnostics.collection.failed"),
    STACK_CM_MIXED_PACKAGE_VERSIONS_FAILED("stack.cm.mixed.package.versions.failed"),
    STACK_CM_MIXED_PACKAGE_VERSIONS_FAILED_NO_CANDIDATE("stack.cm.mixed.package.versions.failed.no.candidate"),
    STACK_CM_MIXED_PACKAGE_VERSIONS_FAILED_MULTIPLE("stack.cm.mixed.package.versions.failed.multiple"),
    STACK_CM_MIXED_PACKAGE_VERSIONS_NEWER_FAILED("stack.cm.mixed.package.versions.newer.failed"),
    STACK_LB_CREATE_ENTITY("stack.lb.update.create.entity"),
    STACK_LB_CREATE_CLOUD_RESOURCE("stack.lb.update.create.cloud.resource"),
    STACK_LB_COLLECT_METADATA("stack.lb.update.collect.metadata"),
    STACK_LB_REGISTER_PUBLIC_DNS("stack.lb.update.register.public.dns"),
    STACK_LB_REGISTER_FREEIPA_DNS("stack.lb.update.register.freeipa.dns"),
    STACK_LB_UPDATE_CM_CONFIG("stack.lb.update.update.cm.config"),
    STACK_LB_RESTART_CM("stack.lb.update.restart.cm"),
    STACK_LB_UPDATE_FINISHED("stack.lb.update.finished"),
    STACK_LB_UPDATE_FAILED("stack.lb.update.failed"),

    CLUSTER_BUILDING("cluster.building"),
    CLUSTER_RESET("cluster.reset"),
    CLUSTER_BUILT("cluster.built"),
    RECOVERY_FINISHED("recovery.finished"),
    CLUSTER_DELETE_FAILED("ambari.cluster.delete.failed"),
    CLUSTER_DELETE_STARTED("cluster.termination.started"),
    CLUSTER_CHANGING_CREDENTIAL("cluster.changing.credential"),
    CLUSTER_CHANGED_CREDENTIAL("cluster.changed.credential"),
    CLUSTER_CREATE_FAILED("cluster.create.failed"),
    CLUSTER_STARTING("cluster.starting"),
    CLUSTER_DNS_ENTRY_UPDATE_FINISHED("cluster.dns.update.finished"),
    CLUSTER_STARTED("cluster.started"),
    CLUSTER_START_FAILED("cluster.start.failed"),
    CLUSTER_CHANGE_CREDENTIAL_FAILED("cluster.change.credentail.failed"),
    CLUSTER_SINGLE_MASTER_REPAIR_STARTED("cluster.single.master.repair.started"),
    CLUSTER_SINGLE_MASTER_REPAIR_FINISHED("cluster.single.master.repair.finished"),
    CLUSTER_SCALING_UP("cluster.scaling.up"),
    CLUSTER_RE_REGISTER_WITH_CLUSTER_PROXY("cluster.re.register.with.cluster.proxy"),
    CLUSTER_SCALED_UP("cluster.scaled.up"),
    CLUSTER_RUN_SERVICES("cluster.run.services"),
    CLUSTER_RUN_CONTAINERS("cluster.run.containers"),
    CLUSTER_GATEWAY_CHANGE_FAILED("cluster.gateway.change.failed"),
    CLUSTER_GATEWAY_CHANGED_SUCCESSFULLY("cluster.gateway.changed.successfully"),
    CLUSTER_GATEWAY_CHANGE("cluster.gateway.change"),
    CLUSTER_FAILED_NODES_REPORTED_CLUSTER_EVENT("cluster.failednodes.reported.cluster"),
    CLUSTER_FAILED_NODES_REPORTED_HOST_EVENT("cluster.failednodes.reported.host"),
    CLUSTER_START_IGNORED("cluster.start.ignored"),
    CLUSTER_STOP_IGNORED("cluster.stop.ignored"),
    CLUSTER_HOST_STATUS_UPDATED("cluster.host.status.updated"),
    CLUSTER_START_REQUESTED("cluster.start.requested"),
    CLUSTER_AUTORECOVERY_REQUESTED_CLUSTER_EVENT("cluster.autorecovery.requested.cluster"),
    CLUSTER_AUTORECOVERY_REQUESTED_HOST_EVENT("cluster.autorecovery.requested.host"),
    CLUSTER_MANUALRECOVERY_REQUESTED("cluster.manualrecovery.requested"),
    CLUSTER_MANUALRECOVERY_COULD_NOT_START("cluster.manualrecovery.could.not.start"),
    CLUSTER_MANUALRECOVERY_NO_NODES_TO_RECOVER("cluster.manualrecovery.no.nodes.to.recover"),
    CLUSTER_RECOVERED_NODES_REPORTED_CLUSTER_EVENT("cluster.recoverednodes.reported.cluster"),
    CLUSTER_RECOVERED_NODES_REPORTED_HOST_EVENT("cluster.recoverednodes.reported.host"),
    CLUSTER_PRIMARY_GATEWAY_UNHEALTHY_SYNC_STARTED("cluster.pgw.unhealthy.sync.started"),
    CLUSTER_PACKAGES_ON_INSTANCES_ARE_DIFFERENT("cluster.sync.instance.different.packages"),
    CLUSTER_PACKAGE_VERSIONS_ON_INSTANCES_ARE_MISSING("cluster.sync.instance.missing.package.versions"),
    CLUSTER_PACKAGE_VERSION_CANNOT_BE_QUERIED("cluster.sync.instance.failedquery.packages"),
    CLUSTER_UPLOAD_RECIPES("cluster.recipes.upload"),
    CLUSTER_BOOTSTRAPPER_ERROR_BOOTSTRAP_FAILED_ON_NODES("cluster.bootstrapper.error.nodes.failed"),
    CLUSTER_BOOTSTRAPPER_ERROR_DELETING_NODE("cluster.bootstrapper.error.deleting.node"),
    CLUSTER_BOOTSTRAPPER_ERROR_INVALID_NODECOUNT("cluster.bootstrapper.error.invalide.nodecount"),
    CLUSTER_AMBARI_CLUSTER_COULD_NOT_SYNC("cluster.ambari.cluster.could.not.sync"),
    CLUSTER_AMBARI_CLUSTER_SYNCHRONIZED("cluster.ambari.cluster.synchronized"),
    DATALAKE_UPGRADE("datalake.upgrade"),
    DATALAKE_UPGRADE_PREPARATION("datalake.upgrade.preparation"),
    DATALAKE_UPGRADE_COULD_NOT_START("datalake.upgrade.could.not.start"),
    DATALAKE_UPGRADE_CCM("datalake.ccm.upgrade"),
    DATALAKE_UPGRADE_CCM_ERROR_INVALID_COUNT("datalake.ccm.upgrade.error.invalid.count"),
    DATALAKE_UPGRADE_CCM_ERROR_ENVIRONMENT_IS_NOT_LATEST("datalake.ccm.upgrade.error.environment.notlatest"),
    DATALAKE_UPGRADE_CCM_NO_DATALAKE("datalake.ccm.upgrade.no.datalake"),
    DATALAKE_UPGRADE_CCM_ALREADY_UPGRADED("datalake.ccm.upgrade.already.upgraded"),
    DATALAKE_UPGRADE_CCM_NOT_UPGRADEABLE("datalake.ccm.upgrade.not.upgradeable"),
    DATALAKE_UPGRADE_CCM_NOT_AVAILABLE("datalake.ccm.upgrade.not.available"),
    DATALAKE_UPGRADE_CCM_FAILED("datalake.ccm.upgrade.failed"),
    DATALAKE_UPGRADE_CCM_IN_PROGRESS("datalake.ccm.upgrade.inprogress"),
    DATAHUB_UPGRADE_CCM_ERROR_ENVIRONMENT_IS_NOT_LATEST("datahub.ccm.upgrade.error.environment.notlatest"),
    DATAHUB_UPGRADE_CCM_NOT_AVAILABLE("datahub.ccm.upgrade.not.available"),
    DATAHUB_UPGRADE_CCM_ALREADY_UPGRADED("datahub.ccm.upgrade.already.upgraded"),
    DATAHUB_UPGRADE_CCM_NOT_UPGRADEABLE("datahub.ccm.upgrade.not.upgradeable"),
    CLUSTER_MANAGER_UPGRADE("cluster.manager.upgrade"),
    CLUSTER_MANAGER_UPGRADE_NOT_NEEDED("cluster.manager.upgrade.not.needed"),
    CLUSTER_MANAGER_UPGRADE_FINISHED("cluster.manager.upgrade.finished"),
    CLUSTER_MANAGER_UPGRADE_FAILED("cluster.manager.upgrade.failed"),
    CLUSTER_UPGRADE("cluster.upgrade"),
    CLUSTER_UPGRADE_NOT_NEEDED("cluster.upgrade.not.needed"),
    CLUSTER_UPGRADE_FINISHED("cluster.upgrade.finished.newversion"),
    CLUSTER_UPGRADE_FINISHED_NOVERSION("cluster.upgrade.finished.noversion"),
    CLUSTER_UPGRADE_PREPARATION_STARTED("cluster.upgrade.preparation.started"),
    CLUSTER_UPGRADE_PREPARATION_FINISHED("cluster.upgrade.preparation.finished"),
    CLUSTER_UPGRADE_PREPARATION_FAILED("cluster.upgrade.preparation.failed"),
    CLUSTER_UPGRADE_DOWNLOAD_PARCEL("cluster.upgrade.download.parcel"),
    CLUSTER_UPGRADE_DISTRIBUTE_PARCEL("cluster.upgrade.distribute.parcel"),
    CLUSTER_UPGRADE_ACTIVATE_PARCEL("cluster.upgrade.activate.parcel"),
    CLUSTER_UPGRADE_START_UPGRADE("cluster.upgrade.start.upgrade"),
    CLUSTER_UPGRADE_START_POST_UPGRADE("cluster.upgrade.start.post-upgrade"),
    CLUSTER_UPGRADE_FAILED("cluster.upgrade.failed"),
    CLUSTER_UPGRADE_VALIDATION_STARTED("cluster.upgrade.validation.started"),
    CLUSTER_UPGRADE_VALIDATION_FINISHED("cluster.upgrade.validation.finished"),
    CLUSTER_UPGRADE_VALIDATION_SKIPPED("cluster.upgrade.validation.skipped"),
    CLUSTER_UPGRADE_VALIDATION_FAILED("cluster.upgrade.validation.failed"),
    CLUSTER_PROVISION_CLOUD_STORAGE_VALIDATION_ON_IDBROKER_FAILED("cluster.provision.idbrokerhost.cloudstorage.vmvalidation.failed"),

    CLUSTER_SCALING_STOPSTART_UPSCALE_INIT("cluster.scaling.stopstart.upscale.init"),
    CLUSTER_SCALING_STOPSTART_UPSCALE_NODES_STARTED("cluster.scaling.stopstart.upscale.nodes.started"),
    CLUSTER_SCALING_STOPSTART_UPSCALE_NODES_NOT_STARTED("cluster.scaling.stopstart.upscale.nodes.notstarted"),
    CLUSTER_SCALING_STOPSTART_UPSCALE_INADEQUATE_NODES("cluster.scaling.stopstart.upscale.inadequate.nodes"),
    CLUSTER_SCALING_STOPSTART_UPSCALE_COMMISSIONING("cluster.scaling.stopstart.upscale.commissioning"),
    CLUSTER_SCALING_STOPSTART_UPSCALE_COMMISSIONING2("cluster.scaling.stopstart.upscale.commissioning2"),
    CLUSTER_SCALING_STOPSTART_UPSCALE_COULDNOTCOMMISSION("cluster.scaling.stopstart.upscale.couldnotcommission"),
    CLUSTER_SCALING_STOPSTART_UPSCALE_WAITING_HOSTSTART("cluster.scaling.stopstart.upscale.waiting.hoststart"),
    CLUSTER_SCALING_STOPSTART_UPSCALE_CMHOSTSSTARTED("cluster.scaling.stopstart.upscale.cmhostsstarted"),
    CLUSTER_SCALING_STOPSTART_UPSCALE_START_FAILED("cluster.scaling.stopstart.upscale.start.failed"),
    CLUSTER_SCALING_STOPSTART_UPSCALE_COMMISSION_FAILED("cluster.scaling.stopstart.upscale.commission.failed"),
    CLUSTER_SCALING_STOPSTART_UPSCALE_FINISHED("cluster.scaling.stopstart.upscale.finished"),
    CLUSTER_SCALING_STOPSTART_UPSCALE_FAILED("cluster.scaling.stopstart.upscale.failed"),

    CLUSTER_SCALING_STOPSTART_DOWNSCALE_INIT("cluster.scaling.stopstart.downscale.init"),
    CLUSTER_SCALING_STOPSTART_DOWNSCALE_STARTING("cluster.scaling.stopstart.downscale.starting"),
    CLUSTER_SCALING_STOPSTART_DOWNSCALE_COULDNOTDECOMMISSION("cluster.scaling.stopstart.downscale.couldnotdecommission"),
    CLUSTER_SCALING_STOPSTART_DOWNSCALE_ENTERINGCMMAINTMODE("cluster.scaling.stopstart.downscale.enteringcmmaintmode"),
    CLUSTER_SCALING_STOPSTART_DOWNSCALE_ENTEREDCMMAINTMODE("cluster.scaling.stopstart.downscale.enteredcmmaintmode"),
    CLUSTER_SCALING_STOPSTART_DOWNSCALE_NODE_STOPPING("cluster.scaling.stopstart.downscale.nodes.stopping"),
    CLUSTER_SCALING_STOPSTART_DOWNSCALE_NODES_STOPPED("cluster.scaling.stopstart.downscale.nodes.stopped"),
    CLUSTER_SCALING_STOPSTART_DOWNSCALE_NODES_NOT_STOPPED("cluster.scaling.stopstart.downscale.nodes.notstopped"),
    CLUSTER_SCALING_STOPSTART_DOWNSCALE_DECOMMISSION_FAILED("cluster.scaling.stopstart.downscale.decommission.failed"),
    CLUSTER_SCALING_STOPSTART_DOWNSCALE_STOP_FAILED("cluster.scaling.stopstart.downscale.stop.failed"),
    CLUSTER_SCALING_STOPSTART_DOWNSCALE_FINISHED("cluster.scaling.stopstart.downscale.finished"),
    CLUSTER_SCALING_STOPSTART_DOWNSCALE_FAILED("cluster.scaling.stopstart.downscale.failed"),


    CLUSTER_SALT_UPDATE_STARTED("cluster.salt.update.started"),
    CLUSTER_SALT_UPDATE_FAILED("cluster.salt.update.failed"),
    CLUSTER_SALT_UPDATE_FINISHED("cluster.salt.update.finished"),
    CLUSTER_PILLAR_CONFIG_UPDATE_FAILED("cluster.pillar.config.update.failed"),
    CLUSTER_PILLAR_CONFIG_UPDATE_FINISHED("cluster.pillar.config.update.finished"),
    CLUSTER_PILLAR_CONFIG_UPDATE_STARTED("cluster.pillar.config.update.started"),
    CLUSTER_SALT_PASSWORD_ROTATE_STARTED("cluster.salt.passwordrotate.started"),
    CLUSTER_SALT_PASSWORD_ROTATE_FINISHED("cluster.salt.passwordrotate.finished"),
    CLUSTER_SALT_PASSWORD_ROTATE_FAILED("cluster.salt.passwordrotate.failed"),
    CLUSTER_STOPPING("cluster.stopping"),
    CLUSTER_STOPPED("cluster.stopped"),
    CLUSTER_STOP_FAILED("cluster.stop.failed"),
    CLUSTER_SCALING_DOWN("cluster.scaling.down"),
    CLUSTER_SCALING_DOWN_ZOMBIE_NODES("cluster.scaling.down.zombie.nodes"),
    CLUSTER_SCALED_DOWN("cluster.scaled.down"),
    CLUSTER_SCALED_DOWN_NONE("cluster.scaled.down.none"),
    CLUSTER_SCALING_FAILED("cluster.scaling.failed"),
    CLUSTER_SCALING_PARTIALLY_FAILED("cluster.scaling.partially.failed"),
    CLUSTER_DECOMMISSION_FAILED_FORCE_DELETE_CONTINUE("cluster.decommission.failed.force.delete.continue"),
    CLUSTER_STOP_MANAGEMENT_SERVER_STARTED("cluster.stop.management.server.started"),
    CLUSTER_STOP_COMPONENTS_STARTED("cluster.stop.components.started"),
    CLUSTER_START_MANAGEMENT_SERVER_STARTED("cluster.start.management.server.started"),
    CLUSTER_REGENERATE_KEYTABS_STARTED("cluster.regenerate.keytabs"),
    CLUSTER_REINSTALL_COMPONENTS_STARTED("cluster.single.master.repair.reinstall.components"),
    CLUSTER_START_COMPONENTS_STARTED("cluster.start.components.started"),
    CLUSTER_RESTART_ALL_STARTED("cluster.restart.all.started"),
    CLUSTER_CM_CLUSTER_SERVICES_STOPPING("cm.cluster.services.stopping"),
    CLUSTER_CM_CLUSTER_SERVICES_STOPPED("cm.cluster.services.stopped"),
    CLUSTER_CM_CLUSTER_SERVICES_STARTED("cm.cluster.services.started"),
    CLUSTER_CM_CLUSTER_SERVICES_STARTING("cm.cluster.services.starting"),
    CLUSTER_CM_CLUSTER_SERVICES_RESTARTING("cm.cluster.services.restarting"),
    CLUSTER_CM_UPDATING_REMOTE_DATA_CONTEXT("cm.cluster.updating.remote.data.context"),
    CLUSTER_CM_UPDATED_REMOTE_DATA_CONTEXT("cm.cluster.updated.remote.data.context"),
    CLUSTER_CM_SECURITY_GROUP_TOO_STRICT("cm.cluster.securitygroup.too.strict"),
    CLUSTER_CM_SERVICE_DEREGISTER_FAILED("cm.cluster.service.deregister.failed"),
    CLUSTER_CM_COMMAND_FAILED("cm.cluster.command.failed"),
    CLUSTER_CM_COMMAND_TIMEOUT("cm.cluster.command.timeout"),
    CLUSTER_CM_COMMAND_TIMEOUT_WARNING("cm.cluster.command.timeout.warning"),
    CLUSTER_MANAGER_CLUSTER_DECOMMISSIONING_TIME("cluster.ambari.cluster.decommissioning.time"),
    CLUSTER_MANAGER_CLUSTER_SERVICES_STOPPING("cluster.ambari.cluster.services.stopping"),
    CLUSTER_MANAGER_CLUSTER_SERVICES_STOPPED("cluster.ambari.cluster.services.stopped"),
    CLUSTER_MANAGER_CLUSTER_SERVICES_STARTING("cluster.ambari.cluster.services.starting"),
    CLUSTER_MANAGER_CLUSTER_SERVICES_STARTED("cluster.ambari.cluster.services.started"),
    CLUSTER_REMOVING_NODES("cluster.removing.nodes"),
    CLUSTER_REMOVING_ZOMBIE_NODES("cluster.removing.zombie.nodes"),
    CLUSTER_FORCE_REMOVING_NODES("cluster.force.removing.nodes"),
    CLUSTER_LOST_NODE_DECOMMISSION_ABORTED_TWICE("cluster.removing.node.lost.node.decommission.aborted"),
    CLUSTER_CERTIFICATE_REISSUE("cluster.certificate.reissue"),
    CLUSTER_CERTIFICATE_REDEPLOY("cluster.certificate.redeploy"),
    CLUSTER_CERTIFICATE_RENEWAL_FINISHED("cluster.certificate.renewal.finished"),
    CLUSTER_CERTIFICATE_RENEWAL_FAILED("cluster.certificate.renewal.failed"),
    CLUSTER_CERTIFICATES_ROTATION_STARTED("cluster.certificates.rotation.started"),
    CLUSTER_HOST_CERTIFICATES_ROTATION("cluster.host.certificates.rotation"),
    CLUSTER_MANAGER_SERVER_RESTARTING("cluster.manager.server.restarting"),
    CLUSTER_SERVICES_RESTARTING("cluster.services.restarting"),
    CLUSTER_CERTIFICATES_ROTATION_FINISHED("cluster.certificates.rotation.finished"),
    CLUSTER_CERTIFICATES_ROTATION_FAILED("cluster.certificates.rotation.failed"),

    CLUSTER_EXTERNAL_DATABASE_DELETION_STARTED("cluster.externaldatabase.deletion.started"),
    CLUSTER_EXTERNAL_DATABASE_DELETION_FAILED("cluster.externaldatabase.deletion.failed"),
    CLUSTER_EXTERNAL_DATABASE_DELETION_FINISHED("cluster.externaldatabase.deletion.finished"),
    CLUSTER_EXTERNAL_DATABASE_CREATION_STARTED("cluster.externaldatabase.creation.started"),
    CLUSTER_EXTERNAL_DATABASE_CREATION_FAILED("cluster.externaldatabase.creation.failed"),
    CLUSTER_EXTERNAL_DATABASE_CREATION_FINISHED("cluster.externaldatabase.creation.finished"),
    CLUSTER_EXTERNAL_DATABASE_START_COMMANCED("cluster.externaldatabase.start.commenced"),
    CLUSTER_EXTERNAL_DATABASE_START_FAILED("cluster.externaldatabase.start.failed"),
    CLUSTER_EXTERNAL_DATABASE_START_FINISHED("cluster.externaldatabase.start.finished"),
    CLUSTER_EXTERNAL_DATABASE_STOP_COMMANCED("cluster.externaldatabase.stop.commenced"),
    CLUSTER_EXTERNAL_DATABASE_STOP_FAILED("cluster.externaldatabase.stop.failed"),
    CLUSTER_EXTERNAL_DATABASE_STOP_FINISHED("cluster.externaldatabase.stop.finished"),

    CLUSTER_CCM_UPGRADE_TUNNEL_UPDATE("cluster.ccm.upgrade.tunnel.update"),
    CLUSTER_CCM_UPGRADE_PUSH_SALT_STATES("cluster.ccm.upgrade.push.salt.states"),
    CLUSTER_CCM_UPGRADE_RECONFIGURE_NGINX("cluster.ccm.upgrade.reconfigure.nginx"),
    CLUSTER_CCM_UPGRADE_REGISTER_CLUSTER_PROXY("cluster.ccm.upgrade.register.clusterproxy"),
    CLUSTER_CCM_UPGRADE_HEALTH_CHECK("cluster.ccm.upgrade.health.check"),
    CLUSTER_CCM_UPGRADE_REMOVE_AGENT("cluster.ccm.upgrade.remove.agent"),
    CLUSTER_CCM_UPGRADE_DEREGISTER_AGENT("cluster.ccm.upgrade.deregister.agent"),
    CLUSTER_CCM_UPGRADE_FAILED("cluster.ccm.upgrade.failed"),
    CLUSTER_CCM_UPGRADE_FINISHED("cluster.ccm.upgrade.finished"),

    KERBEROS_CONFIG_VALIDATION_FAILED("cluster.kerberosconfig.validation.failed"),

    CLOUD_CONFIG_VALIDATION_FAILED("cluster.cloudconfig.validation.failed"),
    CLOUD_PROVIDER_VALIDATION_WARNING("cluster.provider.validation.warning"),

    STACK_RETRY_FLOW_START("retry.flow.start"),

    MAINTENANCE_MODE_VALIDATION_STARTED("maintenance.mode.validation.started"),
    MAINTENANCE_MODE_VALIDATION_FINISHED_FOUND_WARNINGS("maintenance.mode.validation.finished.warn"),
    MAINTENANCE_MODE_VALIDATION_FINISHED_NO_WARNINGS("maintenance.mode.validation.finished.nowarn"),
    MAINTENANCE_MODE_VALIDATION_FAILED("maintenance.mode.validation.failed"),

    COMMON_BAD_REQUEST_NOTIFICATION_PATTERN("common.bad.request.notification.pattern"),

    DATALAKE_DATABASE_BACKUP("datalake.database.backup"),
    DATALAKE_DATABASE_BACKUP_FINISHED("datalake.database.backup.finished"),
    DATALAKE_DATABASE_BACKUP_FAILED("datalake.database.backup.failed"),
    DATALAKE_DATABASE_BACKUP_COULD_NOT_START("datalake.database.backup.could.not.start"),
    DATALAKE_DATABASE_RESTORE("datalake.database.restore"),
    DATALAKE_DATABASE_RESTORE_FINISHED("datalake.database.restore.finished"),
    DATALAKE_DATABASE_RESTORE_FAILED("datalake.database.restore.failed"),
    DATALAKE_DATABASE_RESTORE_COULD_NOT_START("datalake.database.restore.could.not.start"),
    DATALAKE_BACKUP_IN_PROGRESS("datalake.backup.in.progress"),
    DATALAKE_RESTORE_IN_PROGRESS("datalake.restore.in.progress"),
    DATALAKE_BACKUP_FINISHED("datalake.backup.finished"),
    DATALAKE_RESTORE_FINISHED("datalake.restore.finished"),
    DATALAKE_BACKUP_FAILED("datalake.backup.failed"),
    DATALAKE_RESTORE_FAILED("datalake.restore.failed"),
    DATALAKE_DETACHED("datalake.detached"),
    DATALAKE_CERT_RENEWAL_STARTED("datalake.cert.renewal.started"),
    DATALAKE_CERT_RENEWAL_FAILED("datalake.cert.renewal.failed"),
    DATALAKE_CERT_RENEWAL_FINISHED("datalake.cert.renewal.finished"),
    SDX_CERT_ROTATION_STARTED("resource.sdx.cert.rotation.started"),
    SDX_CERT_ROTATION_FAILED("resource.sdx.cert.rotation.failed"),
    SDX_CERT_ROTATION_FINISHED("resource.sdx.cert.rotation.finished"),

    DATALAKE_RECOVERY_REQUESTED("datalake.recovery.requested"),
    DATALAKE_RECOVERY_IN_PROGRESS("datalake.recovery.in.progress"),
    DATALAKE_RECOVERY_BRINGUP_FINISHED("datalake.recovery.bringup.finished"),
    DATALAKE_RECOVERY_BRINGUP_FAILED("datalake.recovery.bringup.failed"),
    DATALAKE_RECOVERY_TEARDOWN_FINISHED("datalake.recovery.teardown.finished"),
    DATALAKE_RECOVERY_FAILED("datalake.recovery.failed"),
    DATALAKE_RECOVERY_FINISHED("datalake.recovery.finished"),

    DATALAKE_RESIZE_TRIGGERED("datalake.resize.triggered"),
    DATALAKE_RESIZE_COMPLETE("datalake.resize.complete"),

    DATAHUB_REFRESH_IN_PROGRESS("datalake.datahub.refresh.in.progress"),
    DATAHUB_REFRESH_FAILED("datalake.datahub.refresh.failed"),

    DATALAKE_SALT_PASSWORD_ROTATION_IN_PROGRESS("datalake.salt.passwordrotation.in.progress"),
    DATALAKE_SALT_PASSWORD_ROTATION_FAILED("datalake.salt.passwordrotation.failed"),
    DATALAKE_SALT_PASSWORD_ROTATION_FINISHED("datalake.salt.passwordrotation.finished"),

    // deprecated
    STACK_GATEWAY_CERTIFICATE_CREATE_FAILED("stack.gateway.certificate.create.skipped");

    private final String message;

    ResourceEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
