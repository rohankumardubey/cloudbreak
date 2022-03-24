package com.sequenceiq.cloudbreak.filter;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import com.sequenceiq.cloudbreak.auth.crn.Crn;
import com.sequenceiq.cloudbreak.auth.security.authentication.AuthenticatedUserService;
import com.sequenceiq.cloudbreak.common.user.CloudbreakUser;
import com.sequenceiq.cloudbreak.service.user.UserService;
import com.sequenceiq.cloudbreak.service.workspace.CachedWorkspaceService;
import com.sequenceiq.cloudbreak.structuredevent.CloudbreakRestRequestThreadLocalService;
import com.sequenceiq.cloudbreak.workspace.model.User;
import com.sequenceiq.cloudbreak.workspace.model.Workspace;

public class WorkspaceConfiguratorFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(WorkspaceConfiguratorFilter.class);

    private final CloudbreakRestRequestThreadLocalService restRequestThreadLocalService;

    private final CachedWorkspaceService workspaceService;

    private final UserService userService;

    private final AuthenticatedUserService authenticatedUserService;

    public WorkspaceConfiguratorFilter(CloudbreakRestRequestThreadLocalService restRequestThreadLocalService, CachedWorkspaceService workspaceService,
            UserService userService, AuthenticatedUserService authenticatedUserService) {
        this.restRequestThreadLocalService = restRequestThreadLocalService;
        this.workspaceService = workspaceService;
        this.userService = userService;
        this.authenticatedUserService = authenticatedUserService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        CloudbreakUser cloudbreakUser = authenticatedUserService.getCbUser();
        try {
            if (cloudbreakUser != null) {
                User user = userService.getOrCreate(cloudbreakUser);
                String accountId = Crn.fromString(cloudbreakUser.getUserCrn()).getAccountId();
                // default workspaceName is always the accountId
                Optional<Workspace> tenantDefaultWorkspace = workspaceService.getByName(accountId, user);
                if (!tenantDefaultWorkspace.isPresent()) {
                    throw new IllegalStateException("Tenant default workspace does not exist!");
                }
                Long workspaceId = tenantDefaultWorkspace.get().getId();
                restRequestThreadLocalService.setRequestedWorkspaceId(workspaceId);
                WorkspaceIdModifiedRequest modifiedRequest = new WorkspaceIdModifiedRequest(request, workspaceId);
                LOGGER.debug("Before:CloudbreakRestRequestThreadLocalContext: {}", restRequestThreadLocalService.getRestThreadLocalContextAsString());
                filterChain.doFilter(modifiedRequest, response);
                LOGGER.debug("After:CloudbreakRestRequestThreadLocalContext: {}", restRequestThreadLocalService.getRestThreadLocalContextAsString());
            } else {
                filterChain.doFilter(request, response);
            }
        } finally {
            restRequestThreadLocalService.removeRequestedWorkspaceId();
        }
    }
}
