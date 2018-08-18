package org.pcsoft.framework.jremote.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Mark an interface as remote model. In it all pushes ({@link RemotePushService}) are stored in defined properties, see {@link PushModelProperty}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RemotePushModel {
}
