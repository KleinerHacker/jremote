package org.pcsoft.framework.jremote.api.type;

import org.pcsoft.framework.jremote.api.EventReceiverListener;

/**
 * Functional interface for the event observer listener method(s) to observe data from {@link org.pcsoft.framework.jremote.api.RemoteEventService}.
 * Method with this parameter must be annotated with {@link EventReceiverListener}.
 */
@FunctionalInterface
public interface EventReceivedListener<T> extends RemoteListener {
    /**
     * Is called if the linked event is called, see {@link EventReceiverListener}
     *
     * @param value New event value was received from {@link org.pcsoft.framework.jremote.api.RemoteEventService}
     */
    void onChange(T value);
}
