package org.pcsoft.framework.jremote.core.test.api;

import org.pcsoft.framework.jremote.api.PushModelProperty;
import org.pcsoft.framework.jremote.api.RemotePushModel;

@RemotePushModel
public interface TestRemotePushModel {
    @PushModelProperty("name")
    String getName();

    @PushModelProperty("value")
    int getValue();

    //Default test only
    default int calc(int x, int y) {
        return x + y;
    }
}
