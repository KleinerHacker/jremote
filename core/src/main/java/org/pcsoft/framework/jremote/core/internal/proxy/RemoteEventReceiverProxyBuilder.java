package org.pcsoft.framework.jremote.core.internal.proxy;

import org.pcsoft.framework.jremote.api.EventReceiverListener;
import org.pcsoft.framework.jremote.api.exception.JRemoteAnnotationException;
import org.pcsoft.framework.jremote.api.type.EventReceivedListener;
import org.pcsoft.framework.jremote.core.internal.type.MethodKey;
import org.pcsoft.framework.jremote.core.internal.validation.Validator;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

final class RemoteEventReceiverProxyBuilder extends RemoteListenerProxyBuilder<EventReceiverListener, Map<MethodKey, List<EventReceivedListener>>> {
    private static final RemoteEventReceiverProxyBuilder INSTANCE = new RemoteEventReceiverProxyBuilder();

    public static RemoteEventReceiverProxyBuilder getInstance() {
        return INSTANCE;
    }

    @Override
    protected void validate(Class<?> clazz) throws JRemoteAnnotationException {
        Validator.validateForRemoteEventObserver(clazz);
    }

    @Override
    protected void assertMethod(EventReceiverListener annotation, Class<?> clazz, Method method, Object[] args) {
        assert method.getParameterCount() == 1 && method.getParameterTypes()[0] == EventReceivedListener.class && method.getReturnType() == void.class;
    }

    @Override
    protected Object invokeMethod(EventReceiverListener receiverListener, Map<MethodKey, List<EventReceivedListener>> listenerMap, Class<?> clazz, Method method, Object[] args) {
        addOrRemoveListener(clazz, method, (EventReceivedListener) args[0], new MethodKey(method.getDeclaringClass(), receiverListener.value()),
                receiverListener.listenerType(), listenerMap);
        return null;
    }

    @Override
    protected String getProxyName() {
        return "Remote Event Receiver";
    }

    private RemoteEventReceiverProxyBuilder() {
        super(EventReceiverListener.class);
    }
}
