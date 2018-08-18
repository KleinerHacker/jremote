package org.pcsoft.framework.jremote.api.type;

/**
 * Functional interface for the event observer listener method(s) to observe data from {@link org.pcsoft.framework.jremote.api.RemoteEventService}.
 * Method with this parameter must be annotated with {@link org.pcsoft.framework.jremote.api.EventObserverListener}.
 */
@FunctionalInterface
public interface EventChangeListener<T> {
    /**
     * Is called if the linked event is called, see {@link org.pcsoft.framework.jremote.api.EventObserverListener}
     *
     * @param value New event value was received from {@link org.pcsoft.framework.jremote.api.RemoteEventService}
     */
    void onChange(T value);
}
