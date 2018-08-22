package org.pcsoft.framework.jremote.core.internal.proxy;

import org.pcsoft.framework.jremote.api.PushObserverListener;
import org.pcsoft.framework.jremote.api.exception.JRemoteAnnotationException;
import org.pcsoft.framework.jremote.api.type.PushChangedListener;
import org.pcsoft.framework.jremote.core.internal.type.MethodKey;
import org.pcsoft.framework.jremote.core.internal.validation.Validator;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

final class RemotePushObserverProxyBuilder extends RemoteListenerProxyBuilder<PushObserverListener, Map<MethodKey, List<PushChangedListener>>> {
    private static final RemotePushObserverProxyBuilder INSTANCE = new RemotePushObserverProxyBuilder();

    public static RemotePushObserverProxyBuilder getInstance() {
        return INSTANCE;
    }

    @Override
    protected void validate(Class<?> clazz) throws JRemoteAnnotationException {
        Validator.validateForRemotePushObserver(clazz);
    }

    @Override
    protected void assertMethod(PushObserverListener annotation, Class<?> clazz, Method method, Object[] args) {
        assert method.getParameterCount() == 1 && method.getParameterTypes()[0] == PushChangedListener.class && method.getReturnType() == void.class;
    }

    @Override
    protected Object invokeMethod(PushObserverListener observerListener, Map<MethodKey, List<PushChangedListener>> listenerMap, Class<?> clazz, Method method, Object[] args) {
        addOrRemoveListener(clazz, method, (PushChangedListener) args[0], new MethodKey(observerListener.modelClass(), observerListener.property()),
                observerListener.listenerType(), listenerMap);
        return null;
    }

    @Override
    protected String getProxyName() {
        return "Remote Push Observer";
    }

    private RemotePushObserverProxyBuilder() {
        super(PushObserverListener.class);
    }
}
