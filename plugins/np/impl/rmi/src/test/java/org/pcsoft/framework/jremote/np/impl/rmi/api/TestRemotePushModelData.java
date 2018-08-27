package org.pcsoft.framework.jremote.np.impl.rmi.api;

import java.util.List;

public class TestRemotePushModelData implements TestRemotePushModel {
    @Override
    public String getName() {
        return "Initial";
    }

    @Override
    public int getValue() {
        return -3;
    }

    @Override
    public List<String> getWordList() {
        return List.of("Hello", "World");
    }

    @Override
    public int[] getSizeList() {
        return new int[]{10, 20, 30};
    }
}
