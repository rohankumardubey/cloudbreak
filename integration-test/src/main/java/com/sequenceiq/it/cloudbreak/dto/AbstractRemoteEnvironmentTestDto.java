package com.sequenceiq.it.cloudbreak.dto;

import static com.sequenceiq.it.cloudbreak.context.RunningParameter.emptyRunningParameter;

import java.util.ArrayList;
import java.util.List;

import jakarta.ws.rs.BadRequestException;

import com.sequenceiq.it.cloudbreak.action.Action;
import com.sequenceiq.it.cloudbreak.assertion.Assertion;
import com.sequenceiq.it.cloudbreak.context.RunningParameter;
import com.sequenceiq.it.cloudbreak.context.TestContext;
import com.sequenceiq.it.cloudbreak.microservice.RemoteEnvironmentClient;

public abstract class AbstractRemoteEnvironmentTestDto<R, S, T extends CloudbreakTestDto> extends AbstractTestDto<R, S, T, RemoteEnvironmentClient> {

    protected AbstractRemoteEnvironmentTestDto(R request, TestContext testContext) {
        super(request, testContext);
    }

    @Override
    public T when(Class<T> entityClass, Action<T, RemoteEnvironmentClient> action) {
        return getTestContext().when(entityClass, RemoteEnvironmentClient.class, action, emptyRunningParameter());
    }

    @Override
    public T when(Action<T, RemoteEnvironmentClient> action) {
        return getTestContext().when((T) this, RemoteEnvironmentClient.class, action, emptyRunningParameter());
    }

    @Override
    public T when(Class<T> entityClass, Action<T, RemoteEnvironmentClient> action, RunningParameter runningParameter) {
        return getTestContext().when(entityClass, RemoteEnvironmentClient.class, action, runningParameter);
    }

    @Override
    public T when(Action<T, RemoteEnvironmentClient> action, RunningParameter runningParameter) {
        return getTestContext().when((T) this, RemoteEnvironmentClient.class, action, runningParameter);
    }

    @Override
    public <T extends CloudbreakTestDto> T deleteGiven(Class<T> clazz, Action<T, RemoteEnvironmentClient> action, RunningParameter runningParameter) {
        getTestContext().when((T) getTestContext().given(clazz), RemoteEnvironmentClient.class, action, runningParameter);
        return getTestContext().expect((T) getTestContext().given(clazz), BadRequestException.class, runningParameter);
    }

    @Override
    public <E extends Exception> T whenException(Class<T> entityClass, Action<T, RemoteEnvironmentClient> action, Class<E> expectedException) {
        return getTestContext().whenException(entityClass, RemoteEnvironmentClient.class, action, expectedException, emptyRunningParameter());
    }

    @Override
    public <E extends Exception> T whenException(Action<T, RemoteEnvironmentClient> action, Class<E> expectedException) {
        return getTestContext().whenException((T) this, RemoteEnvironmentClient.class, action, expectedException, emptyRunningParameter());
    }

    @Override
    public <E extends Exception> T whenException(Class<T> entityClass, Action<T, RemoteEnvironmentClient> action, Class<E> expectedException,
            RunningParameter runningParameter) {
        return getTestContext().whenException(entityClass, RemoteEnvironmentClient.class, action, expectedException, runningParameter);
    }

    @Override
    public <E extends Exception> T whenException(Action<T, RemoteEnvironmentClient> action, Class<E> expectedException, RunningParameter runningParameter) {
        return getTestContext().whenException((T) this, RemoteEnvironmentClient.class, action, expectedException, runningParameter);
    }

    @Override
    public T then(Assertion<T, RemoteEnvironmentClient> assertion) {
        return then(assertion, emptyRunningParameter());
    }

    @Override
    public T then(Assertion<T, RemoteEnvironmentClient> assertion, RunningParameter runningParameter) {
        return getTestContext().then((T) this, RemoteEnvironmentClient.class, assertion, runningParameter);
    }

    @Override
    public T then(List<Assertion<T, RemoteEnvironmentClient>> assertions) {
        List<RunningParameter> runningParameters = new ArrayList<>(assertions.size());
        for (int i = 0; i < assertions.size(); i++) {
            runningParameters.add(emptyRunningParameter());
        }
        return then(assertions, runningParameters);
    }

    @Override
    public T then(List<Assertion<T, RemoteEnvironmentClient>> assertions, List<RunningParameter> runningParameters) {
        for (int i = 0; i < assertions.size() - 1; i++) {
            getTestContext().then((T) this, RemoteEnvironmentClient.class, assertions.get(i), runningParameters.get(i));
        }
        return getTestContext()
                .then((T) this, RemoteEnvironmentClient.class, assertions.get(assertions.size() - 1), runningParameters.get(runningParameters.size() - 1));
    }
}
