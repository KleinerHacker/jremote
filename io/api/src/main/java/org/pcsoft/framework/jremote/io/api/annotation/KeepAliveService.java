package org.pcsoft.framework.jremote.io.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Mark an interface as remote keep alive service. Must have exactly one method: <br/>
 * <ul>
 *     <li><code>boolean ping(String uuid)</code> annotated with <code>{@link KeepAlive}</code><br/>
 *     Method to ping server with client's UUID to get TRUE if client is registered, otherwise FALSE</li>
 * </ul>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface KeepAliveService {
}
