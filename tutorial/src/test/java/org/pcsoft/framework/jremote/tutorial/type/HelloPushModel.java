package org.pcsoft.framework.jremote.tutorial.type;

import org.pcsoft.framework.jremote.api.PushModelProperty;
import org.pcsoft.framework.jremote.api.RemotePushModel;

@RemotePushModel
public interface HelloPushModel {
    String PROP_GREETING_COUNT = "greeting-count";

    @PushModelProperty(PROP_GREETING_COUNT)
    int getGreetingCount();
}
