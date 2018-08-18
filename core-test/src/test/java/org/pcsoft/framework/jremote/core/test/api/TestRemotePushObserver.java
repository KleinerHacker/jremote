package org.pcsoft.framework.jremote.core.test.api;

import org.pcsoft.framework.jremote.api.PushObserverListener;
import org.pcsoft.framework.jremote.api.RemotePushObserver;
import org.pcsoft.framework.jremote.api.type.PushChangeListener;

@RemotePushObserver
public interface TestRemotePushObserver {
    @PushObserverListener(pushClass = TestRemotePushService.class, pushMethod = "pushName")
    void addNameListener(PushChangeListener listener);

    @PushObserverListener(pushClass = TestRemotePushService.class, pushMethod = "pushName")
    void removeNameListener(PushChangeListener listener);

    @PushObserverListener(pushClass = TestRemotePushService.class, pushMethod = "pushValue")
    void addValueListener(PushChangeListener listener);

    @PushObserverListener(pushClass = TestRemotePushService.class, pushMethod = "pushValue")
    void removeValueListener(PushChangeListener listener);

    default int calc(int x, int y) {
        return (int) Math.pow(x + y, 2);
    }
}
