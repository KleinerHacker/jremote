package org.pcsoft.framework.jremote.api;

import org.pcsoft.framework.jremote.api.type.ObserverListenerType;
import org.pcsoft.framework.jremote.api.type.PushChangedListener;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Mark a method in an interface as listener method for a push call, see {@link RemotePushService}. Interface must be annotated with {@link RemotePushObserver}.<br/>
 * <br/>
 * The method signature must build with a void return type and one argument of type {@link PushChangedListener}:<br/>
 * <code>void addListener(PushChangedListener l)</code>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface PushObserverListener {
    /**
     * The name of the {@link RemotePushModel} property the observer listener listen to, see {@link PushModelProperty#value()}
     * @return
     */
    String property();

    /**
     * The class of the {@link RemotePushModel} within the {@link #property()} the observer listener listen to, see {@link PushModelProperty}
     * @return
     */
    Class<?> modelClass();

    /**
     * Setup the type of listener method like 'add' or 'remove' (Default is {@link ObserverListenerType#AutoDetection})
     *
     * @return
     */
    ObserverListenerType listenerType() default ObserverListenerType.AutoDetection;
}
