package org.pcsoft.framework.jremote.sc.commons.test.api;

import org.pcsoft.framework.jremote.api.ModelProperty;
import org.pcsoft.framework.jremote.api.RemoteModel;

@RemoteModel
public interface TestRemoteModel {
    @ModelProperty(sourcePushClass = TestPushService.class, sourcePushMethod = "pushName")
    String getName();

    @ModelProperty(sourcePushClass = TestPushService.class, sourcePushMethod = "pushValue")
    int getValue();

    //Default test only
    default int calc(int x, int y) {
        return x + y;
    }
}
