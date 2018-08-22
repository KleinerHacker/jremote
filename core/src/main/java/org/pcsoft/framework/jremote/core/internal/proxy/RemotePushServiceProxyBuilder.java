package org.pcsoft.framework.jremote.core.internal.proxy;

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
                handleCompleteListPush((Collection) dataMap.get(key), args[0]);
                break;
            case SingleListItem:
                handleSingleListItemPush((Collection) dataMap.get(key), args[0], (PushItemUpdate) args[1]);
                break;
            default:
                throw new RuntimeException();
        }
    }

    @SuppressWarnings("unchecked")
    private static void handleSingleListItemPush(Collection collection, Object item, PushItemUpdate itemUpdate) {
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
