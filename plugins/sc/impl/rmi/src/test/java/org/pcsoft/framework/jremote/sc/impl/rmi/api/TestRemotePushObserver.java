package org.pcsoft.framework.jremote.sc.impl.rmi.api;

import org.pcsoft.framework.jremote.api.PushObserverListener;
import org.pcsoft.framework.jremote.api.RemotePushObserver;
import org.pcsoft.framework.jremote.api.type.PushChangedListener;

@RemotePushObserver
public interface TestRemotePushObserver {
    @PushObserverListener(modelClass = TestRemotePushModel.class, property = "name")
    void addNameListener(PushChangedListener listener);

    @PushObserverListener(modelClass = TestRemotePushModel.class, property = "name")
    void removeNameListener(PushChangedListener listener);

    @PushObserverListener(modelClass = TestRemotePushModel.class, property = "value")
    void addValueListener(PushChangedListener listener);

    @PushObserverListener(modelClass = TestRemotePushModel.class, property = "value")
    void removeValueListener(PushChangedListener listener);

    @PushObserverListener(modelClass = TestRemotePushModel.class, property = "words")
    void addWordListListener(PushChangedListener listener);

    @PushObserverListener(modelClass = TestRemotePushModel.class, property = "words")
    void removeWordListListener(PushChangedListener listener);

    @PushObserverListener(modelClass = TestRemotePushModel.class, property = "sizes")
    void addSizeListListener(PushChangedListener listener);

    @PushObserverListener(modelClass = TestRemotePushModel.class, property = "sizes")
    void removeSizeListListener(PushChangedListener listener);
}
