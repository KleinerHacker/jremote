package org.pcsoft.framework.jremote.api;

import org.pcsoft.framework.jremote.api.type.EventReceivedListener;
import org.pcsoft.framework.jremote.api.type.ObserverListenerType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Mark a method in an interface as listener method for a event call, see {@link RemoteEventService}. Interface must be annotated with {@link RemoteEventReceiver}.<br/>
 * <br/>
 * The method signature must build with a void return type and one argument of type {@link EventReceivedListener}:<br/>
 * <code>void addListener(EventReceivedListener&lt;?&gt; l)</code>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface EventReceiverListener {
    /**
     * The name of this event to receive from {@link RemoteEventService}, see {@link Event}
     * @return
     */
    String value();

    /**
     * Setup the type of listener method like 'add' or 'remove' (Default is {@link ObserverListenerType#AutoDetection})
     *
     * @return
     */
    ObserverListenerType listenerType() default ObserverListenerType.AutoDetection;
}
