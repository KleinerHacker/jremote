package org.pcsoft.framework.jremote.api;

import org.pcsoft.framework.jremote.api.internal.RemoteMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to mark a method in a {@link RemoteControlService} as a control method
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@RemoteMethod
public @interface Control {
}
