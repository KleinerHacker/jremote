package org.pcsoft.framework.jremote.api.type;

/**
 * Functional interface for the push observer listener method(s) to observe data changes in
 * {@link org.pcsoft.framework.jremote.api.RemotePushModel} from {@link org.pcsoft.framework.jremote.api.RemotePushService}.
 * Method with this parameter must be annotated with {@link org.pcsoft.framework.jremote.api.PushObserverListener}.
 */
@FunctionalInterface
public interface PushChangeListener extends ChangeListener {
    /**
     * Is called if the linked value has changed, see {@link org.pcsoft.framework.jremote.api.PushObserverListener}
     */
    void onChange();
}
