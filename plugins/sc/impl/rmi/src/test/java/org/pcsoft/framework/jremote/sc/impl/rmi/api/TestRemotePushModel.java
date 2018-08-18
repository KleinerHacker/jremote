package org.pcsoft.framework.jremote.sc.impl.rmi.api;

import org.pcsoft.framework.jremote.api.PushModelProperty;
import org.pcsoft.framework.jremote.api.RemotePushModel;

@RemotePushModel
public interface TestRemotePushModel {
    @PushModelProperty(sourcePushClass = TestRemotePushService.class, sourcePushMethod = "pushName")
    String getName();

    @PushModelProperty(sourcePushClass = TestRemotePushService.class, sourcePushMethod = "pushValue")
    int getValue();
}
