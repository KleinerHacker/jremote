package org.pcsoft.framework.jremote.tutorial.type;

import org.pcsoft.framework.jremote.api.PushObserverListener;
import org.pcsoft.framework.jremote.api.RemotePushObserver;
import org.pcsoft.framework.jremote.api.type.PushChangedListener;

@RemotePushObserver
public interface HelloPushObserver {
    @PushObserverListener(property = HelloPushModel.PROP_GREETING_COUNT, modelClass = HelloPushModel.class)
    void addGreetingCountListener(PushChangedListener l);

    @PushObserverListener(property = HelloPushModel.PROP_GREETING_COUNT, modelClass = HelloPushModel.class)
    void removeGreetingCountListener(PushChangedListener l);
}
