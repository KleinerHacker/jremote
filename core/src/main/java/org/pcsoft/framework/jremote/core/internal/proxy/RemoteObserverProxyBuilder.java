package org.pcsoft.framework.jremote.core.internal.proxy;

import org.pcsoft.framework.jremote.api.ObserverListener;
import org.pcsoft.framework.jremote.api.RemoteObserver;
import org.pcsoft.framework.jremote.api.exception.JRemoteAnnotationException;
import org.pcsoft.framework.jremote.api.type.ChangeListener;
import org.pcsoft.framework.jremote.api.type.ObserverListenerType;
import org.pcsoft.framework.jremote.core.internal.type.PushMethodKey;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

final class RemoteObserverProxyBuilder {

    @SuppressWarnings("unchecked")
    static <T> T buildProxy(Class<T> clazz, Map<PushMethodKey, List<ChangeListener>> listenerMap) {
        if (clazz.getAnnotation(RemoteObserver.class) == null)
            throw new JRemoteAnnotationException(String.format("unable to find annotation %s on class %s", RemoteObserver.class.getName(), clazz.getName()));

        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, (proxy, method, args) -> {
            final ObserverListener observerListener = method.getAnnotation(ObserverListener.class);
            if (observerListener == null) {
                if (method.isDefault())
                    return method.invoke(proxy, args);

                throw new JRemoteAnnotationException(String.format("Found not default method with missing annotation %s on %s#%s",
                        ObserverListener.class.getName(), clazz.getName(), method.getName()));
            } else {
                if (method.getParameterCount() != 1 || !(args[0] instanceof ChangeListener) || method.getReturnType() != void.class)
                    throw new JRemoteAnnotationException(String.format("Method signature wrong: need a one-parameter method (%s) with a void return value: %s#%s",
                            ChangeListener.class.getName(), clazz.getName(), method.getName()));
            }

            addOrRemoveListener(clazz, method, (ChangeListener) args[0], observerListener, listenerMap);
            return null;
        });
    }

    private static <T> void addOrRemoveListener(Class<T> clazz, Method method, ChangeListener listener, ObserverListener observerListener,
                                                Map<PushMethodKey, List<ChangeListener>> listenerMap) {
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

    private static void doAddListener(PushMethodKey key, ChangeListener listener, Map<PushMethodKey, List<ChangeListener>> dataMap) {
        if (!dataMap.containsKey(key)) {
            dataMap.put(key, new ArrayList<>());
        }

        dataMap.get(key).add(listener);
    }

    private static void doRemoveListener(PushMethodKey key, ChangeListener listener, Map<PushMethodKey, List<ChangeListener>> dataMap) {
        if (!dataMap.containsKey(key)) {
            dataMap.put(key, new ArrayList<>());
        }

        dataMap.get(key).remove(listener);
    }

    private static <T> ObserverListenerType extractListenerType(Class<T> clazz, Method method, ObserverListener observerListener) {
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
    }
}
