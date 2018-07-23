package org.pcsoft.framework.jremote.api;

import org.pcsoft.framework.jremote.api.type.ObserverListenerType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Mark a method in an interface as listener method for a push call, see {@link RemotePushService}. Interface must be annotated with {@link RemoteObserver}.<br/>
 * <br/>
 * The method signature must build with a void return type and one argument of type {@link org.pcsoft.framework.jremote.api.type.ChangeListener}:<br/>
 * <code>void addListener(ChangeListener l)</code>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ObserverListener {
    /**
     * Class of push with method to observe, must be annotated with {@link RemotePushService}
     * @return
     */
    Class<?> pushClass();

    /**
     * Method name of push class to observe
     * @return
     */
    String pushMethod();

    /**
     * Setup the type of listener method like 'add' or 'remove' (Default is {@link ObserverListenerType#AutoDetection})
     * @return
     */
    ObserverListenerType listenerType() default ObserverListenerType.AutoDetection;
}
