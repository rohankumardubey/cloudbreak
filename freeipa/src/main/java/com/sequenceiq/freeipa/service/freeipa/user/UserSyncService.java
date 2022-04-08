package com.sequenceiq.freeipa.service.freeipa.user;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.sequenceiq.authorization.resource.AuthorizationResourceAction;
import com.sequenceiq.authorization.service.CommonPermissionCheckingUtils;
import com.sequenceiq.authorization.service.CustomCheckUtil;
import com.sequenceiq.cloudbreak.auth.ThreadBasedUserCrnProvider;
import com.sequenceiq.cloudbreak.auth.altus.EntitlementService;
import com.sequenceiq.cloudbreak.auth.crn.RegionAwareInternalCrnGeneratorFactory;
import com.sequenceiq.cloudbreak.common.exception.NotFoundException;
import com.sequenceiq.cloudbreak.logger.MDCBuilder;
import com.sequenceiq.freeipa.api.v1.freeipa.user.model.WorkloadCredentialsUpdateType;
import com.sequenceiq.freeipa.api.v1.operation.model.OperationState;
import com.sequenceiq.freeipa.api.v1.operation.model.OperationType;
import com.sequenceiq.freeipa.configuration.UsersyncConfig;
import com.sequenceiq.freeipa.entity.Operation;
import com.sequenceiq.freeipa.entity.Stack;
import com.sequenceiq.freeipa.entity.UserSyncStatus;
import com.sequenceiq.freeipa.service.freeipa.user.model.UserSyncOptions;
import com.sequenceiq.freeipa.service.operation.OperationService;
import com.sequenceiq.freeipa.service.stack.StackService;

@Service
public class UserSyncService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserSyncService.class);

    @Inject
    private StackService stackService;

    @Inject
    private OperationService operationService;

    @Inject
    @Qualifier(UsersyncConfig.USERSYNC_TASK_EXECUTOR)
    private ExecutorService asyncTaskExecutor;

    @Inject
    private UserSyncStatusService userSyncStatusService;

    @Inject
    private CommonPermissionCheckingUtils commonPermissionCheckingUtils;

    @Inject
    private EntitlementService entitlementService;

    @Inject
    private UserSyncRequestValidator userSyncRequestValidator;

    @Inject
    private RegionAwareInternalCrnGeneratorFactory regionAwareInternalCrnGeneratorFactory;

    @Inject
    private UserSyncForEnvService userSyncForEnvService;

    @Inject
    private CustomCheckUtil customCheckUtil;

    public Operation synchronizeUsers(String accountId, String actorCrn, Set<String> environmentCrnFilter,
            Set<String> userCrnFilter, Set<String> machineUserCrnFilter, WorkloadCredentialsUpdateType workloadCredentialsUpdateType) {
        UserSyncRequestFilter userSyncFilter = new UserSyncRequestFilter(userCrnFilter, machineUserCrnFilter, Optional.empty());
        List<Stack> stacks = getStacksForSync(accountId, actorCrn, environmentCrnFilter, userSyncFilter);
        UserSyncOptions options = getUserSyncOptions(accountId, userSyncFilter.isFullSync(), workloadCredentialsUpdateType);
        return performSyncForStacks(accountId, userSyncFilter, options, stacks);
    }

    public Operation synchronizeUsersWithCustomPermissionCheck(String accountId, String actorCrn, Set<String> environmentCrnFilter,
            UserSyncRequestFilter userSyncFilter, WorkloadCredentialsUpdateType workloadCredentialsUpdateType, AuthorizationResourceAction action) {
        List<Stack> stacks = getStacksForSync(accountId, actorCrn, environmentCrnFilter, userSyncFilter);
        List<String> relatedEnvironmentCrns = stacks.stream().map(Stack::getEnvironmentCrn).collect(Collectors.toList());
        customCheckUtil.run(actorCrn, () -> commonPermissionCheckingUtils.checkPermissionForUserOnResources(action, actorCrn, relatedEnvironmentCrns));
        UserSyncOptions options = getUserSyncOptions(accountId, userSyncFilter.isFullSync(), workloadCredentialsUpdateType);
        return performSyncForStacks(accountId, userSyncFilter, options, stacks);
    }

    private UserSyncOptions getUserSyncOptions(String accountId, boolean fullSync, WorkloadCredentialsUpdateType requestedCredentialsUpdateType) {
        WorkloadCredentialsUpdateType credentialsUpdateType = requestedCredentialsUpdateType == WorkloadCredentialsUpdateType.UPDATE_IF_CHANGED &&
                !entitlementService.usersyncCredentialsUpdateOptimizationEnabled(accountId) ?
                WorkloadCredentialsUpdateType.FORCE_UPDATE : requestedCredentialsUpdateType;
        UserSyncOptions userSyncOptions = new UserSyncOptions(fullSync, entitlementService.isFmsToFreeipaBatchCallEnabled(accountId),
                credentialsUpdateType);
        LOGGER.info("Credentials update optimization is{} enabled for this sync request",
                userSyncOptions.isCredentialsUpdateOptimizationEnabled() ? "" : " not");
        return userSyncOptions;
    }

    private Operation performSyncForStacks(String accountId, UserSyncRequestFilter userSyncFilter, UserSyncOptions options,
            List<Stack> stacks) {
        logAffectedStacks(stacks);
        Set<String> environmentCrns = stacks.stream().map(Stack::getEnvironmentCrn).collect(Collectors.toSet());
        Operation operation = operationService.startOperation(accountId, OperationType.USER_SYNC, environmentCrns,
                union(userSyncFilter.getUserCrnFilter(), userSyncFilter.getMachineUserCrnFilter()));

        LOGGER.info("Starting operation [{}] with status [{}]", operation.getOperationId(), operation.getStatus());

        if (operation.getStatus() == OperationState.RUNNING) {
            operationService.tryWithOperationCleanup(operation.getOperationId(), accountId, () ->
                    ThreadBasedUserCrnProvider.doAs(
                            regionAwareInternalCrnGeneratorFactory.iam().getInternalCrnForServiceAsString(), () -> {
                                if (userSyncFilter.isFullSync()) {
                                    stacks.forEach(stack -> updateUserSyncStatusForStack(operation, stack));
                                }
                                asyncSynchronizeUsers(operation.getOperationId(), accountId, stacks, userSyncFilter, options);
                            }));
        }

        return operation;
    }

    private void updateUserSyncStatusForStack(Operation operation, Stack stack) {
        UserSyncStatus userSyncStatus = userSyncStatusService.getOrCreateForStack(stack);
        userSyncStatus.setLastStartedFullSync(operation);
        userSyncStatusService.save(userSyncStatus);
    }

    private void logAffectedStacks(List<Stack> stacks) {
        String stacksAffected = stacks.stream().map(stack ->
                        "environment crn: [" + stack.getEnvironmentCrn() + ']'
                                + " resource crn: [" + stack.getResourceCrn() + ']'
                                + " resource name: [" + stack.getName() + ']')
                .collect(Collectors.joining("; "));
        LOGGER.info("Affected stacks: {}", stacksAffected);
    }

    private List<Stack> getStacksForSync(String accountId, String actorCrn, Set<String> environmentCrnFilter, UserSyncRequestFilter userSyncRequestFilter) {
        userSyncRequestValidator.validateParameters(accountId, actorCrn, environmentCrnFilter, userSyncRequestFilter);
        LOGGER.debug("Synchronizing users in account {} for environmentCrns {}, user sync filter {}", accountId, environmentCrnFilter, userSyncRequestFilter);

        List<Stack> stacks = stackService.getMultipleByEnvironmentCrnOrChildEnvironmantCrnAndAccountId(environmentCrnFilter, accountId);
        if (stacks.isEmpty()) {
            throw new NotFoundException(String.format("No matching FreeIPA stacks found for account %s with environment crn filter %s",
                    accountId, environmentCrnFilter));
        } else {
            LOGGER.debug("Found {} stacks", stacks.size());
            return stacks;
        }
    }

    private void asyncSynchronizeUsers(String operationId, String accountId, List<Stack> stacks, UserSyncRequestFilter userSyncFilter, UserSyncOptions options) {
        try {
            MDCBuilder.addOperationId(operationId);
            asyncTaskExecutor.submit(() -> userSyncForEnvService.synchronizeUsers(operationId, accountId, stacks, userSyncFilter, options));
        } finally {
            MDCBuilder.removeOperationId();
        }

    }

    private Set<String> union(Collection<String> collection1, Collection<String> collection2) {
        return Stream.concat(collection1.stream(), collection2.stream()).collect(Collectors.toSet());
    }
}