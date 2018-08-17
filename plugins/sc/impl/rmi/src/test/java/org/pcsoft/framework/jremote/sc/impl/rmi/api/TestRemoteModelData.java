package org.pcsoft.framework.jremote.sc.impl.rmi.api;

public class TestRemoteModelData implements TestRemoteModel {
    @Override
    public String getName() {
        return "Initial";
    }

    @Override
    public int getValue() {
        return -3;
    }
}
