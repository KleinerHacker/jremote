package org.pcsoft.framework.jremote.core.test.api;

import org.pcsoft.framework.jremote.api.EventObserverListener;
import org.pcsoft.framework.jremote.api.RemoteEventObserver;
import org.pcsoft.framework.jremote.api.type.EventChangeListener;

@RemoteEventObserver
public interface TestRemoteEventObserver {
    @EventObserverListener(eventClass = TestRemoteEventService.class, eventMethod = "fireLog")
    void addLogListener(EventChangeListener<String> listener);

    @EventObserverListener(eventClass = TestRemoteEventService.class, eventMethod = "fireLog")
    void removeLogListener(EventChangeListener<String> listener);

    default int calc(int x, int y) {
        return (int) Math.pow(x + y, 3);
    }
}
