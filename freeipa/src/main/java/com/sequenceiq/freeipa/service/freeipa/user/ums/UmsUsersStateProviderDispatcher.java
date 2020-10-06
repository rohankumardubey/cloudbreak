package com.sequenceiq.freeipa.service.freeipa.user.ums;

import static com.sequenceiq.cloudbreak.auth.ThreadBasedUserCrnProvider.INTERNAL_ACTOR_CRN;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.inject.Inject;

import com.sequenceiq.cloudbreak.auth.altus.EntitlementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.sequenceiq.cloudbreak.auth.altus.exception.UmsOperationException;
import com.sequenceiq.freeipa.service.freeipa.user.model.UmsUsersState;

@Service
public class UmsUsersStateProviderDispatcher {

    private static final Logger LOGGER = LoggerFactory.getLogger(UmsUsersStateProviderDispatcher.class);

    @Inject
    private EntitlementService entitlementService;

    @Inject
    private DefaultUmsUsersStateProvider defaultUmsUsersStateProvider;

    @Inject
    private BulkUmsUsersStateProvider bulkUmsUsersStateProvider;

    public Map<String, UmsUsersState> getEnvToUmsUsersStateMap(
            String accountId, String actorCrn, Collection<String> environmentCrns,
            Set<String> userCrns, Set<String> machineUserCrns, Optional<String> requestIdOptional) {
        try {
            LOGGER.debug("Getting UMS state for environments {} with requestId {}", environmentCrns, requestIdOptional);

            boolean fullSync = userCrns.isEmpty() && machineUserCrns.isEmpty();

            if (fullSync && entitlementService.umsUserSyncModelGenerationEnabled(INTERNAL_ACTOR_CRN, accountId)) {
                return dispatchBulk(accountId, actorCrn, environmentCrns, userCrns, machineUserCrns,
                        requestIdOptional, fullSync);
            } else {
                return dispatchDefault(accountId, actorCrn, environmentCrns, userCrns, machineUserCrns,
                        requestIdOptional, fullSync);
            }
        } catch (RuntimeException e) {
            throw new UmsOperationException(String.format("Error during UMS operation: '%s'", e.getLocalizedMessage()), e);
        }
    }

    private Map<String, UmsUsersState> dispatchBulk(
            String accountId, String actorCrn, Collection<String> environmentCrns,
            Set<String> userCrns, Set<String> machineUserCrns, Optional<String> requestIdOptional,
            boolean fullSync) {
        try {
            return bulkUmsUsersStateProvider.get(accountId, environmentCrns, requestIdOptional);
        } catch (RuntimeException e) {
            LOGGER.debug("Failed to retrieve UMS user sync state through bulk request. Falling back on default approach");
            return dispatchDefault(accountId, actorCrn, environmentCrns, userCrns, machineUserCrns,
                    requestIdOptional, fullSync);
        }
    }

    private Map<String, UmsUsersState> dispatchDefault(
            String accountId, String actorCrn, Collection<String> environmentCrns,
            Set<String> userCrns, Set<String> machineUserCrns, Optional<String> requestIdOptional,
            boolean fullSync) {
        return defaultUmsUsersStateProvider.get(
                accountId, actorCrn,
                environmentCrns, userCrns, machineUserCrns,
                requestIdOptional, fullSync);
    }
}