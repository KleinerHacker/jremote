package org.pcsoft.framework.jremote.core.test.api;

import org.pcsoft.framework.jremote.api.PushModelProperty;
import org.pcsoft.framework.jremote.api.RemotePushModel;

@RemotePushModel
public interface TestRemoteModel {
    @PushModelProperty(sourcePushClass = TestPushService.class, sourcePushMethod = "pushName")
    String getName();

    @PushModelProperty(sourcePushClass = TestPushService.class, sourcePushMethod = "pushValue")
    int getValue();

    //Default test only
    default int calc(int x, int y) {
        return x + y;
    }
}
