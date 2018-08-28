package org.pcsoft.framework.jremote.ext.np.api.annotation;

import org.pcsoft.framework.jremote.api.internal.RemoteMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marker annotation for an interface method in a remote keep alive service, see {@link RemoteKeepAliveService}
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@RemoteMethod
public @interface KeepAlive {
}
