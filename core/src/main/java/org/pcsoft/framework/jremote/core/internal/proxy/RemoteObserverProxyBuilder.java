package org.pcsoft.framework.jremote.core.internal.proxy;

import org.pcsoft.framework.jremote.api.PushObserverListener;
import org.pcsoft.framework.jremote.api.exception.JRemoteAnnotationException;
import org.pcsoft.framework.jremote.api.type.PushChangeListener;
import org.pcsoft.framework.jremote.api.type.ObserverListenerType;
import org.pcsoft.framework.jremote.core.internal.type.PushMethodKey;
import org.pcsoft.framework.jremote.core.internal.validation.Validator;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

final class RemoteObserverProxyBuilder extends ProxyBuilder<PushObserverListener, Map<PushMethodKey, List<PushChangeListener>>> {
    private static final RemoteObserverProxyBuilder INSTANCE = new RemoteObserverProxyBuilder();

    public static RemoteObserverProxyBuilder getInstance() {
        return INSTANCE;
    }

    @Override
    protected void validate(Class<?> clazz) throws JRemoteAnnotationException {
        Validator.validateForRemoteObserver(clazz);
    }

    @Override
    protected void assertMethod(PushObserverListener annotation, Class<?> clazz, Method method, Object[] args) {
        assert method.getParameterCount() == 1 && method.getParameterTypes()[0] == PushChangeListener.class && method.getReturnType() == void.class;
    }

    @Override
    protected Object invokeMethod(PushObserverListener observerListener, Map<PushMethodKey, List<PushChangeListener>> listenerMap, Class<?> clazz, Method method, Object[] args) {
        addOrRemoveListener(clazz, method, (PushChangeListener) args[0], observerListener, listenerMap);
        return null;
    }

    @Override
    protected String getProxyName() {
        return "Remote Observer";
    }

    private static <T> void addOrRemoveListener(Class<T> clazz, Method method, PushChangeListener listener, PushObserverListener observerListener,
                                                Map<PushMethodKey, List<PushChangeListener>> listenerMap) {
        final PushMethodKey key = new PushMethodKey(observerListener.pushClass(), observerListener.pushMethod());
        final ObserverListenerType type = extractListenerType(clazz, method, observerListener);
        switch (type) {
            case Add:
                doAddListener(key, listener, listenerMap);
                break;
            case Remove:
                doRemoveListener(key, listener, listenerMap);
                break;
            case AutoDetection:
            default:
                throw new RuntimeException();
        }
    }

    private static void doAddListener(PushMethodKey key, PushChangeListener listener, Map<PushMethodKey, List<PushChangeListener>> dataMap) {
        if (!dataMap.containsKey(key)) {
            dataMap.put(key, new ArrayList<>());
        }

        dataMap.get(key).add(listener);
    }

    private static void doRemoveListener(PushMethodKey key, PushChangeListener listener, Map<PushMethodKey, List<PushChangeListener>> dataMap) {
        if (!dataMap.containsKey(key)) {
            dataMap.put(key, new ArrayList<>());
        }

        dataMap.get(key).remove(listener);
    }

    private static <T> ObserverListenerType extractListenerType(Class<T> clazz, Method method, PushObserverListener observerListener) {
        final ObserverListenerType type;
        if (observerListener.listenerType() == ObserverListenerType.AutoDetection) {
            if (method.getName().toLowerCase().startsWith("add")) {
                type = ObserverListenerType.Add;
            } else if (method.getName().toLowerCase().startsWith("remove")) {
                type = ObserverListenerType.Remove;
            } else
                throw new JRemoteAnnotationException(String.format("Unable to find fit listener method type for %s#%s", clazz.getName(), method.getName()));
        } else {
            type = observerListener.listenerType();
        }
        return type;
    }

    private RemoteObserverProxyBuilder() {
        super(PushObserverListener.class);
    }
}
