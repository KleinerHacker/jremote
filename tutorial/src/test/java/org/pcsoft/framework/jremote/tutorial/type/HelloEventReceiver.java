package org.pcsoft.framework.jremote.tutorial.type;

import org.pcsoft.framework.jremote.api.EventReceiverListener;
import org.pcsoft.framework.jremote.api.RemoteEventReceiver;
import org.pcsoft.framework.jremote.api.type.EventReceivedListener;

@RemoteEventReceiver
public interface HelloEventReceiver {
    String EVENT_GREETING = "greeting";

    @EventReceiverListener(EVENT_GREETING)
    void addGreetingListener(EventReceivedListener<String> l);

    @EventReceiverListener(EVENT_GREETING)
    void removeGreetingListener(EventReceivedListener<String> l);
}
