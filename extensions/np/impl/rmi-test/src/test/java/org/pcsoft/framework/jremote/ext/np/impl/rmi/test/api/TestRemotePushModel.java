package org.pcsoft.framework.jremote.ext.np.impl.rmi.test.api;

import org.pcsoft.framework.jremote.api.PushModelProperty;
import org.pcsoft.framework.jremote.api.RemotePushModel;

import java.util.List;

@RemotePushModel
public interface TestRemotePushModel {
    @PushModelProperty("name")
    String getName();

    @PushModelProperty("value")
    int getValue();

    @PushModelProperty("words")
    List<String> getWordList();

    @PushModelProperty("sizes")
    int[] getSizeList();
}
