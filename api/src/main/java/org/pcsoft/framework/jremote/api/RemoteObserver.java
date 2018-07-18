package org.pcsoft.framework.jremote.api;

import java.lang.annotation.*;

/**
 * Mark an interface as remote observer for observing {@link RemoteModel} via push methods ({@link RemotePushService}), see {@link ObserverListener}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RemoteObserver {
}
