package org.pcsoft.framework.jremote.core.test.api;

import org.pcsoft.framework.jremote.api.ObserverListener;
import org.pcsoft.framework.jremote.api.RemoteObserver;
import org.pcsoft.framework.jremote.api.type.ChangeListener;

@RemoteObserver
public interface TestRemoteObserver {
    @ObserverListener(pushClass = TestPushService.class, pushMethod = "pushName")
    void addNameListener(ChangeListener listener);

    @ObserverListener(pushClass = TestPushService.class, pushMethod = "pushName")
    void removeNameListener(ChangeListener listener);

    @ObserverListener(pushClass = TestPushService.class, pushMethod = "pushValue")
    void addValueListener(ChangeListener listener);

    @ObserverListener(pushClass = TestPushService.class, pushMethod = "pushValue")
    void removeValueListener(ChangeListener listener);

    @ObserverListener(pushClass = TestEventService.class, pushMethod = "pushLog")
    void addLogListener(ChangeListener listener);

    @ObserverListener(pushClass = TestEventService.class, pushMethod = "pushLog")
    void removeLogListener(ChangeListener listener);

    default int calc(int x, int y) {
        return (int) Math.pow(x + y, 2);
    }
}
