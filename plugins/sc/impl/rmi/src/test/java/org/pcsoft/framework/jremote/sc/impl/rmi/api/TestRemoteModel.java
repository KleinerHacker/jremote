package org.pcsoft.framework.jremote.sc.impl.rmi.api;

import org.pcsoft.framework.jremote.api.PushModelProperty;
import org.pcsoft.framework.jremote.api.RemotePushModel;

@RemotePushModel
public interface TestRemoteModel {
    @PushModelProperty(sourcePushClass = TestPushService.class, sourcePushMethod = "pushName")
    String getName();

    @PushModelProperty(sourcePushClass = TestPushService.class, sourcePushMethod = "pushValue")
    int getValue();
}
