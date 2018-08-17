package org.pcsoft.framework.jremote.sc.impl.rmi.api;

import org.pcsoft.framework.jremote.api.ModelProperty;
import org.pcsoft.framework.jremote.api.RemoteModel;

@RemoteModel
public interface TestRemoteModel {
    @ModelProperty(sourcePushClass = TestPushService.class, sourcePushMethod = "pushName")
    String getName();

    @ModelProperty(sourcePushClass = TestPushService.class, sourcePushMethod = "pushValue")
    int getValue();
}
