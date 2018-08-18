package org.pcsoft.framework.jremote.api;

import org.pcsoft.framework.jremote.api.type.ObserverListenerType;
import org.pcsoft.framework.jremote.api.type.PushChangeListener;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Mark a method in an interface as listener method for a event call, see {@link RemoteEventService}. Interface must be annotated with {@link RemoteEventObserver}.<br/>
 * <br/>
 * The method signature must build with a void return type and one argument of type {@link org.pcsoft.framework.jremote.api.type.EventChangeListener}:<br/>
 * <code>void addListener(EventChangeListener&lt;?&gt; l)</code>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface EventObserverListener {
    /**
     * Class of event with method to observe, must be annotated with {@link RemoteEventService}
     * @return
     */
    Class<?> eventClass();

    /**
     * Method name of event class to observe
     * @return
     */
    String eventMethod();

    /**
     * Setup the type of listener method like 'add' or 'remove' (Default is {@link ObserverListenerType#AutoDetection})
     * @return
     */
    ObserverListenerType listenerType() default ObserverListenerType.AutoDetection;
}
