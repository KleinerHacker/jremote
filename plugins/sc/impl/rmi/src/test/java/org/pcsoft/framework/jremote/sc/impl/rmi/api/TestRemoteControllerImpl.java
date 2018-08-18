package org.pcsoft.framework.jremote.sc.impl.rmi.api;

import java.rmi.RemoteException;
import java.util.function.Supplier;

public class TestRemoteControllerImpl implements TestRemoteController {
    private final Supplier<TestRemotePushService> pushServiceSupplier;
    private final Supplier<TestRemoteEventService> eventServiceSupplier;

    public TestRemoteControllerImpl(Supplier<TestRemotePushService> pushServiceSupplier, Supplier<TestRemoteEventService> eventServiceSupplier) {
        this.pushServiceSupplier = pushServiceSupplier;
        this.eventServiceSupplier = eventServiceSupplier;
    }

    @Override
    public void changeName(String newValue) throws RemoteException {
        this.pushServiceSupplier.get().pushName(newValue);
    }

    @Override
    public void changeValue(int value) throws RemoteException {
        this.pushServiceSupplier.get().pushValue(value);
    }

    @Override
    public void log(String msg) throws RemoteException {
        this.eventServiceSupplier.get().fireLog(msg);
    }
}
