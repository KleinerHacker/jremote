package org.pcsoft.framework.jremote.core.internal.proxy;

import org.pcsoft.framework.jremote.api.PushMethod;
import org.pcsoft.framework.jremote.api.exception.JRemoteAnnotationException;
import org.pcsoft.framework.jremote.api.type.ChangeListener;
import org.pcsoft.framework.jremote.api.type.ItemUpdate;
import org.pcsoft.framework.jremote.core.internal.type.PushMethodKey;
import org.pcsoft.framework.jremote.core.internal.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Map;

final class RemotePushServiceProxyBuilder extends ProxyBuilder<PushMethod, RemotePushServiceProxyBuilder.DataHolder> {
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
    protected void assertMethod(PushMethod pushMethod, Class<?> clazz, Method method, Object[] args) {
        switch (pushMethod.type()) {
            case Simple:
            case CompleteList:
                assert method.getParameterCount() != 1 || method.getReturnType() != void.class;
                break;
            case SingleListItem:
                assert method.getParameterCount() != 2 || method.getReturnType() != void.class;
                break;
            default:
                throw new RuntimeException();
        }
    }

    @Override
    protected Object invokeMethod(PushMethod pushMethod, DataHolder data, Class<?> clazz, Method method, Object[] args) {
        final PushMethodKey key = new PushMethodKey(clazz, method.getName());
        updateData(pushMethod, key, args, data.getDataMap());
        fireChange(key, data.getListenerMap());

        return null;
    }

    @Override
    protected String getProxyName() {
        return "Remote Push Service";
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
        super(PushMethod.class);
    }

    static final class DataHolder {
        private final Map<PushMethodKey, Object> dataMap;
        private final Map<PushMethodKey, List<ChangeListener>> listenerMap;

        public DataHolder(Map<PushMethodKey, Object> dataMap, Map<PushMethodKey, List<ChangeListener>> listenerMap) {
            this.dataMap = dataMap;
            this.listenerMap = listenerMap;
        }

        public Map<PushMethodKey, Object> getDataMap() {
            return dataMap;
        }

        public Map<PushMethodKey, List<ChangeListener>> getListenerMap() {
            return listenerMap;
        }
    }
}
