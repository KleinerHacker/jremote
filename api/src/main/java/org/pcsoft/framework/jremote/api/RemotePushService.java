package org.pcsoft.framework.jremote.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Mark an interface as remote push service to push data into {@link RemoteModel} and {@link RemoteObserver}, see {@link PushMethod}
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RemotePushService {
}
