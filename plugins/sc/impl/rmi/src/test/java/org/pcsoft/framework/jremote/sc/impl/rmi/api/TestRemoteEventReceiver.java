package org.pcsoft.framework.jremote.sc.impl.rmi.api;

import org.pcsoft.framework.jremote.api.EventReceiverListener;
import org.pcsoft.framework.jremote.api.RemoteEventReceiver;
import org.pcsoft.framework.jremote.api.type.EventReceivedListener;

@RemoteEventReceiver
public interface TestRemoteEventReceiver {
    @EventReceiverListener("log")
    void addLogListener(EventReceivedListener<String> listener);

    @EventReceiverListener("log")
    void removeLogListener(EventReceivedListener<String> listener);
}
