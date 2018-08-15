package org.pcsoft.framework.jremote.sc.api.annotation;

import org.pcsoft.framework.jremote.api.internal.RemoteMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marker annotation for an interface method in a remote registration service, see {@link RemoteRegistrationService}
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@RemoteMethod
public @interface Registration {
    /**
     * Type of registration method
     * @return
     */
    RegistrationType value();

    /**
     * Types of registration actions
     */
    public enum RegistrationType {
        /**
         * Register a client (method with three arguments: UUID (string), host, port)
         */
        Register,
        /**
         * Unregister a client (method with one argument: UUID (string))
         */
        Unregister
    }
}
