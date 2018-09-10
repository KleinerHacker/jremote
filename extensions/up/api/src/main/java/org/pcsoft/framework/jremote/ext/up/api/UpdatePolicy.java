package org.pcsoft.framework.jremote.ext.up.api;

import org.pcsoft.framework.jremote.api.type.PushItemUpdate;
import org.pcsoft.framework.jremote.commons.type.MethodKey;

/**
 * Interface to describe an update policy for updating model and call observer / receiver
 */
public interface UpdatePolicy {
    /**
     * Is called if a value must be updated in model and observer must called.<br/>
     * If you want to run the model update and observer call use the given callback.
     *
     * @param methodKey      Key of method that are called (push)
     * @param update         Update type in case of lists. If the value is not a list or the complete list is updated, this value is <code>null</code>
     * @param value          Value to update
     * @param updateCallback Callback to call if implementation is ready to run model update and call observer listener, if exists
     */
    void runModelUpdateAndObserverInvocation(MethodKey methodKey, PushItemUpdate update, Object value, Runnable updateCallback);

    /**
     * Is called if an event was received and must called.<br/>
     * If you want to run the event calling use the given callback.
     *
     * @param methodKey     Key of method that are called (event)
     * @param eventCallback Callback to call if implementation is ready to call event
     */
    void callReceiver(MethodKey methodKey, Runnable eventCallback);
}
