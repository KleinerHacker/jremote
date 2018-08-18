package org.pcsoft.framework.jremote.core.internal.proxy;

import org.pcsoft.framework.jremote.api.EventObserverListener;
import org.pcsoft.framework.jremote.api.exception.JRemoteAnnotationException;
import org.pcsoft.framework.jremote.api.type.EventChangeListener;
import org.pcsoft.framework.jremote.core.internal.type.MethodKey;
import org.pcsoft.framework.jremote.core.internal.validation.Validator;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

final class RemoteEventObserverProxyBuilder extends RemoteObserverProxyBuilder<EventObserverListener, Map<MethodKey, List<EventChangeListener>>> {
    private static final RemoteEventObserverProxyBuilder INSTANCE = new RemoteEventObserverProxyBuilder();

    public static RemoteEventObserverProxyBuilder getInstance() {
        return INSTANCE;
    }

    @Override
    protected void validate(Class<?> clazz) throws JRemoteAnnotationException {
        Validator.validateForRemoteEventObserver(clazz);
    }

    @Override
    protected void assertMethod(EventObserverListener annotation, Class<?> clazz, Method method, Object[] args) {
        assert method.getParameterCount() == 1 && method.getParameterTypes()[0] == EventChangeListener.class && method.getReturnType() == void.class;
    }

    @Override
    protected Object invokeMethod(EventObserverListener observerListener, Map<MethodKey, List<EventChangeListener>> listenerMap, Class<?> clazz, Method method, Object[] args) {
        addOrRemoveListener(clazz, method, (EventChangeListener) args[0], new MethodKey(observerListener.eventClass(), observerListener.eventMethod()),
                observerListener.listenerType(), listenerMap);
        return null;
    }

    @Override
    protected String getProxyName() {
        return "Remote Event Observer";
    }

    private RemoteEventObserverProxyBuilder() {
        super(EventObserverListener.class);
    }
}
