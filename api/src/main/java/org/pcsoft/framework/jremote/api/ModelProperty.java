package org.pcsoft.framework.jremote.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Mark a getter method in an interface as model based property. The interface must be annotated with {@link RemoteModel}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ModelProperty {
    /**
     * Class of push with method to observe, must be annotated with {@link RemotePushService}
     * @return
     */
    Class<?> sourcePushClass();

    /**
     * Method name of push class to observe
     * @return
     */
    String sourcePushMethod();
}
