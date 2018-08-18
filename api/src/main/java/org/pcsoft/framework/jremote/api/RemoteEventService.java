package org.pcsoft.framework.jremote.api;

import org.pcsoft.framework.jremote.api.internal.RemoteService;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Mark an interface as remote event service to push data via {@link RemotePushObserver}, see {@link Event}
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@RemoteService
public @interface RemoteEventService {
}
