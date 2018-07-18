package org.pcsoft.framework.jremote.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Mark a method in an interface as listener method for a push call, see {@link RemotePushService}. Interface must be annotated with {@link RemoteObserver}.
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
}
