package org.pcsoft.framework.jremote.api;

import java.lang.annotation.*;

/**
 * Mark an interface as remote observer for observing {@link RemotePushModel} via push methods ({@link RemotePushService}), see {@link PushObserverListener}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RemotePushObserver {
}
