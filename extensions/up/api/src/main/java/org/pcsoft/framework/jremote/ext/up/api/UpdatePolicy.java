package org.pcsoft.framework.jremote.ext.up.api;

/**
 * Interface to describe an update policy for updating model and call observer / receiver
 */
public interface UpdatePolicy {
    /**
     * Is called if a value must be updated in model and observer must called
     */
    void updateModelAndCallObserver();

    /**
     * Is called if an event was received and must called
     */
    void callReceiver();
}
