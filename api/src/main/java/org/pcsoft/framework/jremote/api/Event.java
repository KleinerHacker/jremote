package org.pcsoft.framework.jremote.api;

import org.pcsoft.framework.jremote.api.internal.RemoteMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Mark a method in an interface as event method. Interface must be annotated with {@link RemoteEventService}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@RemoteMethod
public @interface Event {
    /**
     * The name of the event in {@link RemoteEventReceiver} to deliver to, see {@link EventReceiverListener#value()}
     * @return
     */
    String event();

    /**
     * The class of {@link RemoteEventReceiver} within the {@link #event()} to deliver to, see {@link EventReceiverListener}
     * @return
     */
    Class<?> eventClass();
}
