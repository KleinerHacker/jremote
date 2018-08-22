package org.pcsoft.framework.jremote.api;

import org.pcsoft.framework.jremote.api.internal.RemoteMethod;
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
@RemoteMethod
public @interface Push {
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
     * Type of push update, see {@link PushMethodType} for more information.
     *
     * @return
     */
    PushMethodType type() default PushMethodType.Default;
}
