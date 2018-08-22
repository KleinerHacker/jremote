package org.pcsoft.framework.jremote.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Mark a getter method in an interface as model based property. The interface must be annotated with {@link RemotePushModel}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface PushModelProperty {
    /**
     * The name of the model property. Used for referencing from {@link RemotePushService} or {@link RemotePushObserver} class, see {@link Push} or {@link PushObserverListener}
     * @return
     */
    String value();
}
