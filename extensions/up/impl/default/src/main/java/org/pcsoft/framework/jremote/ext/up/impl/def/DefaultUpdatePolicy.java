package org.pcsoft.framework.jremote.ext.up.impl.def;

import org.pcsoft.framework.jremote.api.type.PushItemUpdate;
import org.pcsoft.framework.jremote.commons.type.MethodKey;
import org.pcsoft.framework.jremote.ext.up.api.UpdatePolicy;

/**
 * Represent the default implementation for an update policy extension.
 */
public class DefaultUpdatePolicy implements UpdatePolicy {
    @Override
    public void runModelUpdateAndObserverInvocation(MethodKey methodKey, PushItemUpdate update, Object value, Runnable updateCallback) {
        updateCallback.run();
    }

    @Override
    public void callReceiver(MethodKey methodKey, Runnable eventCallback) {
        eventCallback.run();
    }
}
