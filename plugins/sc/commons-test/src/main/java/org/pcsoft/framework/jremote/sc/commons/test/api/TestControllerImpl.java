package org.pcsoft.framework.jremote.sc.commons.test.api;

import java.rmi.RemoteException;
import java.util.function.Supplier;

public class TestControllerImpl implements TestController {
    private final Supplier<TestPushService> pushServiceSupplier;

    public TestControllerImpl(Supplier<TestPushService> pushServiceSupplier) {
        this.pushServiceSupplier = pushServiceSupplier;
    }

    @Override
    public void changeName(String newValue) throws RemoteException {
        this.pushServiceSupplier.get().pushName(newValue);
    }

    @Override
    public void changeValue(int value) throws RemoteException {
        this.pushServiceSupplier.get().pushValue(value);
    }
}
