package org.pcsoft.framework.jremote.core.internal.proxy;

import org.pcsoft.framework.jremote.api.Event;
import org.pcsoft.framework.jremote.api.exception.JRemoteAnnotationException;
import org.pcsoft.framework.jremote.api.type.EventReceivedListener;
import org.pcsoft.framework.jremote.core.internal.type.MethodKey;
import org.pcsoft.framework.jremote.core.internal.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

final class RemoteEventServiceProxyBuilder extends ProxyBuilder<Event, Map<MethodKey, List<EventReceivedListener>>> {
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
    protected void assertMethod(Event event, Class<?> clazz, Method method, Object[] args) {
        assert method.getParameterCount() == 1 && method.getReturnType() == void.class;
    }

    @Override
    protected Object invokeMethod(Event event, Map<MethodKey, List<EventReceivedListener>> listenerMap, Class<?> clazz, Method method, Object[] args) {
        final MethodKey key = new MethodKey(event.eventClass(), event.event());
        fireEvent(key, listenerMap, args[0]);

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
}
