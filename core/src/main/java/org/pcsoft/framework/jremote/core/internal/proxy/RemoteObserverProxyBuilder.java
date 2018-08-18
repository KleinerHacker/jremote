package org.pcsoft.framework.jremote.core.internal.proxy;

import org.pcsoft.framework.jremote.api.exception.JRemoteAnnotationException;
import org.pcsoft.framework.jremote.api.type.ChangeListener;
import org.pcsoft.framework.jremote.api.type.ObserverListenerType;
import org.pcsoft.framework.jremote.core.internal.type.MethodKey;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

abstract class RemoteObserverProxyBuilder<A extends Annotation, D> extends ProxyBuilder<A, D> {
    public RemoteObserverProxyBuilder(Class<A> methodAnnotationClass) {
        super(methodAnnotationClass);
    }

    protected static <T extends ChangeListener> void addOrRemoveListener(Class<?> clazz, Method method, T listener,
                                                                         MethodKey methodKey, ObserverListenerType listenerType,
                                                                         Map<MethodKey, List<T>> listenerMap) {
        final ObserverListenerType type = extractListenerType(clazz, method, listenerType);
        switch (type) {
            case Add:
                doAddListener(methodKey, listener, listenerMap);
                break;
            case Remove:
                doRemoveListener(methodKey, listener, listenerMap);
                break;
            case AutoDetection:
            default:
                throw new RuntimeException();
        }
    }

    private static ObserverListenerType extractListenerType(Class<?> clazz, Method method, ObserverListenerType listenerType) {
        final ObserverListenerType type;
        if (listenerType == ObserverListenerType.AutoDetection) {
            if (method.getName().toLowerCase().startsWith("add")) {
                type = ObserverListenerType.Add;
            } else if (method.getName().toLowerCase().startsWith("remove")) {
                type = ObserverListenerType.Remove;
            } else
                throw new JRemoteAnnotationException(String.format("Unable to find fit listener method type for %s#%s", clazz.getName(), method.getName()));
        } else {
            type = listenerType;
        }
        return type;
    }

    private static <T extends ChangeListener> void doAddListener(MethodKey key, T listener, Map<MethodKey, List<T>> dataMap) {
        if (!dataMap.containsKey(key)) {
            dataMap.put(key, new ArrayList<>());
        }

        dataMap.get(key).add(listener);
    }

    private static <T extends ChangeListener> void doRemoveListener(MethodKey key, T listener, Map<MethodKey, List<T>> dataMap) {
        if (!dataMap.containsKey(key)) {
            dataMap.put(key, new ArrayList<>());
        }

        dataMap.get(key).remove(listener);
    }
}
