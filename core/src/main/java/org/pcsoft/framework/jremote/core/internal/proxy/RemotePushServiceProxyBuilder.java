package org.pcsoft.framework.jremote.core.internal.proxy;

import org.apache.commons.lang3.ArrayUtils;
import org.pcsoft.framework.jremote.api.Push;
import org.pcsoft.framework.jremote.api.exception.JRemoteAnnotationException;
import org.pcsoft.framework.jremote.api.type.PushChangedListener;
import org.pcsoft.framework.jremote.api.type.PushItemUpdate;
import org.pcsoft.framework.jremote.core.internal.type.MethodKey;
import org.pcsoft.framework.jremote.core.internal.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Map;

final class RemotePushServiceProxyBuilder extends ProxyBuilder<Push, RemotePushServiceProxyBuilder.DataHolder> {
    private static final RemotePushServiceProxyBuilder INSTANCE = new RemotePushServiceProxyBuilder();
    private static final Logger LOGGER = LoggerFactory.getLogger(RemotePushServiceProxyBuilder.class);

    public static RemotePushServiceProxyBuilder getInstance() {
        return INSTANCE;
    }

    @Override
    protected void validate(Class<?> clazz) throws JRemoteAnnotationException {
        Validator.validateForRemoteService(clazz);
        Validator.validateForRemotePushService(clazz);
    }

    @Override
    protected void assertMethod(Push push, Class<?> clazz, Method method, Object[] args) {
        switch (push.type()) {
            case Simple:
            case CompleteList:
                assert method.getParameterCount() == 1 && method.getReturnType() == void.class;
                break;
            case SingleListItem:
                assert method.getParameterCount() == 2 && method.getReturnType() == void.class;
                break;
            default:
                throw new RuntimeException();
        }
    }

    @Override
    protected Object invokeMethod(Push push, DataHolder data, Class<?> clazz, Method method, Object[] args) {
        final MethodKey key = new MethodKey(push.modelClass(), push.property());
        updateData(push, key, args, data.getDataMap());
        fireChange(key, data.getListenerMap());

        return null;
    }

    @Override
    protected String getProxyName() {
        return "Remote Push Service";
    }

    private static void fireChange(MethodKey key, Map<MethodKey, List<PushChangedListener>> listenerMap) {
        if (listenerMap.containsKey(key)) {
            LOGGER.debug("> Fire change event for " + key.toString(false));
            for (final PushChangedListener listener : listenerMap.get(key)) {
                listener.onChange();
            }
        } else {
            LOGGER.trace("> Ignore fire change event, key not found: " + key.toString(false));
        }
    }

    private static void updateData(Push push, MethodKey key, Object[] args, Map<MethodKey, Object> dataMap) {
        LOGGER.debug("> Setup value into model for " + key.toString(false));
        switch (push.type()) {
            case Simple:
                handleSimplePush(dataMap, args[0], key);
                break;
            case CompleteList:
                if (dataMap.get(key) instanceof Collection) {
                    handleCompleteCollectionPush((Collection) dataMap.get(key), args[0]);
                } else if (dataMap.get(key).getClass().isArray()) {
                    dataMap.put(key, handleCompleteArrayPush((Object[]) dataMap.get(key), args[0]));
                } else
                    throw new JRemoteAnnotationException("List pushes only allowed for collections and arrays: " + push.modelClass().getName() + " > " + push.property());
                break;
            case SingleListItem:
                if (dataMap.get(key) instanceof Collection) {
                    handleSingleCollectionItemPush((Collection) dataMap.get(key), args[0], (PushItemUpdate) args[1], push);
                } else if (dataMap.get(key).getClass().isArray()) {
                    dataMap.put(key, handleSingleArrayItemPush((Object[])dataMap.get(key), args[0], (PushItemUpdate) args[1], push));
                } else
                    throw new JRemoteAnnotationException("List pushes only allowed for collections and arrays: " + push.modelClass().getName() + " > " + push.property());
                break;
            default:
                throw new RuntimeException();
        }
    }

    private static Object[] handleSingleArrayItemPush(Object[] array, Object item, PushItemUpdate itemUpdate, Push push) {
        switch (itemUpdate) {
            case Add:
                return ArrayUtils.add(array, item);
            case Update:
                handleArrayUpdate(array, item, push);
                return array;
            case Remove:
                return ArrayUtils.remove(array, ArrayUtils.indexOf(array, item));
            default:
                throw new RuntimeException();
        }
    }

    private static void handleArrayUpdate(Object[] array, Object item, Push push) {
        final int index = ArrayUtils.indexOf(array, item);
        if (index < 0) {
            LOGGER.warn("Unable to find item in array to update model: " + push.modelClass().getName() + " > " + push.property());
            LOGGER.warn("Ignore update");
            return;
        }

        array[index] = item;
    }

    private static Object[] handleCompleteArrayPush(Object[] array, Object item) {
        return (Object[]) item;
    }

    @SuppressWarnings("unchecked")
    private static void handleSingleCollectionItemPush(Collection collection, Object item, PushItemUpdate itemUpdate, Push push) {
        switch (itemUpdate) {
            case Add:
                collection.add(item);
                break;
            case Update:
                handleCollectionUpdate(collection, item, push);
                break;
            case Remove:
                collection.remove(item);
                break;
            default:
                throw new RuntimeException();
        }
    }

    @SuppressWarnings("unchecked")
    private static void handleCollectionUpdate(Collection collection, Object item, Push push) {
        if (collection instanceof List) {
            final int index = ((List) collection).indexOf(item);
            if (index < 0) {
                LOGGER.warn("Unable to find item in list to update model: " + push.modelClass().getName() + " > " + push.property());
                LOGGER.warn("Use default algorithm instead");

                collection.remove(item);
                collection.add(item);

                return;
            }
            collection.remove(item);
            ((List) collection).add(index, item);
        } else {
            collection.remove(item);
            collection.add(item);
        }
    }

    @SuppressWarnings("unchecked")
    private static void handleCompleteCollectionPush(Collection collection, Object item) {
        collection.clear();
        collection.addAll((Collection) item);
    }

    private static void handleSimplePush(Map<MethodKey, Object> dataMap, Object data, MethodKey key) {
        dataMap.put(key, data);
    }

    private RemotePushServiceProxyBuilder() {
        super(Push.class);
    }

    static final class DataHolder {
        private final Map<MethodKey, Object> dataMap;
        private final Map<MethodKey, List<PushChangedListener>> listenerMap;

        public DataHolder(Map<MethodKey, Object> dataMap, Map<MethodKey, List<PushChangedListener>> listenerMap) {
            this.dataMap = dataMap;
            this.listenerMap = listenerMap;
        }

        public Map<MethodKey, Object> getDataMap() {
            return dataMap;
        }

        public Map<MethodKey, List<PushChangedListener>> getListenerMap() {
            return listenerMap;
        }
    }
}
