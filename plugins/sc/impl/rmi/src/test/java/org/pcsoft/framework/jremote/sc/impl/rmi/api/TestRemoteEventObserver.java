package org.pcsoft.framework.jremote.sc.impl.rmi.api;

import org.pcsoft.framework.jremote.api.EventObserverListener;
import org.pcsoft.framework.jremote.api.RemoteEventObserver;
import org.pcsoft.framework.jremote.api.type.EventChangeListener;

@RemoteEventObserver
public interface TestRemoteEventObserver {
    @EventObserverListener(eventClass = TestRemoteEventService.class, eventMethod = "fireLog")
    void addLogListener(EventChangeListener<String> listener);

    @EventObserverListener(eventClass = TestRemoteEventService.class, eventMethod = "fireLog")
    void removeLogListener(EventChangeListener<String> listener);
}
