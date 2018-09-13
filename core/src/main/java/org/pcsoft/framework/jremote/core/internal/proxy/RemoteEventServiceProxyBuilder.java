package org.pcsoft.framework.jremote.core.internal.proxy;

import org.pcsoft.framework.jremote.api.Event;
import org.pcsoft.framework.jremote.api.exception.JRemoteAnnotationException;
import org.pcsoft.framework.jremote.api.type.EventReceivedListener;
import org.pcsoft.framework.jremote.commons.type.MethodKey;
import org.pcsoft.framework.jremote.core.internal.validation.Validator;
import org.pcsoft.framework.jremote.ext.up.api.UpdatePolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

final class RemoteEventServiceProxyBuilder extends ProxyBuilder<Event, RemoteEventServiceProxyBuilder.DataHolder> {
    private static final RemoteEventServiceProxyBuilder INSTANCE = new RemoteEventServiceProxyBuilder();
    private static final Logger LOGGER = LoggerFactory.getLogger(RemoteEventServiceProxyBuilder.class);

    public static RemoteEventServiceProxyBuilder getInstance() {
        return INSTANCE;
    }

    @Override
    protected void validate(Class<?> clazz) throws JRemoteAnnotationException {
        Validator.validateForRemoteService(clazz);
        Validator.validateForRemoteEventService(clazz);
    }

    @Override
    protected Object invokeMethod(Event event, DataHolder dataHolder, Class<?> clazz, Method method, Object[] args) {
        final MethodKey key = new MethodKey(event.eventClass(), event.event());
        dataHolder.getUpdatePolicy().callReceiver(key, () -> fireEvent(key, dataHolder.getListenerMap(), args[0]));

        return null;
    }

    @Override
    protected String getProxyName() {
        return "Remote Push Service";
    }

    @SuppressWarnings("unchecked")
    private static void fireEvent(MethodKey key, Map<MethodKey, List<EventReceivedListener>> listenerMap, Object value) {
        if (listenerMap.containsKey(key)) {
            LOGGER.debug("> Fire remote event for " + key.toString(false));
            for (final EventReceivedListener listener : listenerMap.get(key)) {
                listener.onChange(value);
            }
        } else {
            LOGGER.trace("> Ignore fire remote event, key not found: " + key.toString(false));
        }
    }

    private RemoteEventServiceProxyBuilder() {
        super(Event.class);
    }

    //<editor-fold desc="Helper Classes">
    static final class DataHolder {
        private final Map<MethodKey, List<EventReceivedListener>> listenerMap;
        private final UpdatePolicy updatePolicy;

        public DataHolder(Map<MethodKey, List<EventReceivedListener>> listenerMap, UpdatePolicy updatePolicy) {
            this.listenerMap = listenerMap;
            this.updatePolicy = updatePolicy;
        }

        public Map<MethodKey, List<EventReceivedListener>> getListenerMap() {
            return listenerMap;
        }

        public UpdatePolicy getUpdatePolicy() {
            return updatePolicy;
        }
    }
    //</editor-fold>
}
