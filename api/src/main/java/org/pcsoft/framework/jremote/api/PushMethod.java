package org.pcsoft.framework.jremote.api;

import org.pcsoft.framework.jremote.api.type.PushMethodType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Mark a method in an interface as push method. Interface must be annotated with {@link RemotePushService}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface PushMethod {
    /**
     * Type of push update, see {@link PushMethodType} for more information.
     * @return
     */
    PushMethodType type() default PushMethodType.Simple;
}
