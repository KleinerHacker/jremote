package org.pcsoft.framework.jremote.core.internal.proxy;

import org.pcsoft.framework.jremote.api.PushMethod;
import org.pcsoft.framework.jremote.api.RemotePushService;
import org.pcsoft.framework.jremote.api.exception.JRemoteAnnotationException;
import org.pcsoft.framework.jremote.api.type.ChangeListener;
import org.pcsoft.framework.jremote.api.type.ItemUpdate;
import org.pcsoft.framework.jremote.core.internal.type.PushMethodKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Proxy;
import java.util.Collection;
import java.util.List;
import java.util.Map;

final class RemotePushServiceProxyBuilder {
    private static final Logger LOGGER = LoggerFactory.getLogger(RemotePushServiceProxyBuilder.class);

    @SuppressWarnings("unchecked")
    static <T> T buildProxy(Class<T> clazz, Map<PushMethodKey, Object> dataMap, Map<PushMethodKey, List<ChangeListener>> listenerMap) {
        LOGGER.debug("Create push service proxy for " + clazz.getName());

        if (clazz.getAnnotation(RemotePushService.class) == null)
            throw new JRemoteAnnotationException(String.format("Unable to find annotation %s on class %s", RemotePushService.class.getName(), clazz.getName()));

        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, (proxy, method, args) -> {
            LOGGER.debug(String.format("Call push service method %s#%s", clazz.getName(), method.getName()));

            final PushMethod pushMethod = method.getAnnotation(PushMethod.class);
            if (pushMethod == null) {
                if (method.isDefault())
                    return method.invoke(proxy, args);

                throw new JRemoteAnnotationException(String.format("Found not default method with missing annotation %s on %s#%s",
                        PushMethod.class.getName(), clazz.getName(), method.getName()));
            } else {
                switch (pushMethod.type()) {
                    case Simple:
                    case CompleteList:
                        if (method.getParameterCount() != 1 || method.getReturnType() != void.class)
                            throw new JRemoteAnnotationException(String.format("Method signature wrong: need a one-parameter method with a void return value: %s#%s",
                                    clazz.getName(), method.getName()));
                        break;
                    case SingleListItem:
                        if (method.getParameterCount() != 2 || method.getReturnType() != void.class)
                            throw new JRemoteAnnotationException(String.format("Method signature wrong: need a two-parameter method with a void return value: %s#%s",
                                    clazz.getName(), method.getName()));
                        break;
                    default:
                        throw new RuntimeException();
                }
            }

            final PushMethodKey key = new PushMethodKey(clazz, method.getName());
            updateData(pushMethod, key, args, dataMap);
            fireChange(key, listenerMap);

            return null;
        });
    }

    private static void fireChange(PushMethodKey key, Map<PushMethodKey, List<ChangeListener>> listenerMap) {
        if (listenerMap.containsKey(key)) {
            LOGGER.debug("> Fire change event for " + key.toString(false));
            for (final ChangeListener listener : listenerMap.get(key)) {
                listener.onChange();
            }
        } else {
            LOGGER.trace("> Ignore fire change event, key not found: " + key.toString(false));
        }
    }

    private static void updateData(PushMethod pushMethod, PushMethodKey key, Object[] args, Map<PushMethodKey, Object> dataMap) {
        if (dataMap.containsKey(key)) {
            LOGGER.debug("> Setup value into model for " + key.toString(false));
            switch (pushMethod.type()) {
                case Simple:
                    handleSimplePush(dataMap, args[0], key);
                    break;
                case CompleteList:
                    handleCompleteListPush((Collection) dataMap.get(key), args[0]);
                    break;
                case SingleListItem:
                    handleSingleListItemPush((Collection) dataMap.get(key), args[0], (ItemUpdate) args[1]);
                    break;
                default:
                    throw new RuntimeException();
            }
        } else {
            LOGGER.trace("> Ignore model update, key not found: " + key.toString(false));
        }
    }

    @SuppressWarnings("unchecked")
    private static void handleSingleListItemPush(Collection collection, Object item, ItemUpdate itemUpdate) {
        switch (itemUpdate) {
            case Add:
                collection.add(item);
                break;
            case Update:
                collection.remove(item);
                collection.add(item);
                break;
            case Remove:
                collection.remove(item);
                break;
            default:
                throw new RuntimeException();
        }
    }

    @SuppressWarnings("unchecked")
    private static void handleCompleteListPush(Collection collection, Object item) {
        collection.clear();
        collection.addAll((Collection) item);
    }

    private static void handleSimplePush(Map<PushMethodKey, Object> dataMap, Object data, PushMethodKey key) {
        dataMap.put(key, data);
    }

    private RemotePushServiceProxyBuilder() {
    }
}
