package org.pcsoft.framework.jremote.core.test.api;

import org.pcsoft.framework.jremote.api.PushMethod;
import org.pcsoft.framework.jremote.api.RemotePushService;

@RemotePushService
public interface TestPushService {
    @PushMethod
    void pushName(String name);

    @PushMethod
    void pushValue(int value);

    //Default test only
    default int calc(int x, int y) {
        return x - y;
    }
}
