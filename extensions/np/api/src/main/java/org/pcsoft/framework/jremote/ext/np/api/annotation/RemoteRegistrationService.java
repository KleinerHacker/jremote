package org.pcsoft.framework.jremote.ext.np.api.annotation;

import org.pcsoft.framework.jremote.api.internal.RemoteService;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Mark an interface as remote registration service. Must have exactly two methods: <br/>
 * <ul>
 * <li><code>void register(String uuid, String host, int port)</code> annotated with <code>{@link Registration}({@link Registration.RegistrationType#Register})</code><br/>
 * Method to register a client, identified by its UUID.</li>
 * <li><code>void unregister(String uuid)</code> annotated with <code>{@link Registration}({@link Registration.RegistrationType#Unregister})</code><br/>
 * Method to unregister a client, identified by its UUID</li>
 * </ul>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@RemoteService
public @interface RemoteRegistrationService {
}
