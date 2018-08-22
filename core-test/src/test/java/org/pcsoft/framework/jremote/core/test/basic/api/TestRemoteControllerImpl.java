package org.pcsoft.framework.jremote.core.test.basic.api;

import org.junit.jupiter.api.Assertions;
import org.pcsoft.framework.jremote.api.type.PushItemUpdate;

import java.rmi.RemoteException;
import java.util.List;
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
        assertionForPush();
        this.pushServiceSupplier.get().pushName(newValue);
    }

    @Override
    public void changeValue(int value) throws RemoteException {
        assertionForPush();
        this.pushServiceSupplier.get().pushValue(value);
    }

    @Override
    public void changeWordList(List<String> words) throws RemoteException {
        assertionForPush();
        this.pushServiceSupplier.get().pushAllWords(words);
    }

    @Override
    public void addWord(String word) throws RemoteException {
        assertionForPush();
        this.pushServiceSupplier.get().pushWord(word, PushItemUpdate.Add);
    }

    @Override
    public void removeWord(String word) throws RemoteException {
        assertionForPush();
        this.pushServiceSupplier.get().pushWord(word, PushItemUpdate.Remove);
    }

    @Override
    public void changeSizeList(int[] sizes) throws RemoteException {
        assertionForPush();
        this.pushServiceSupplier.get().pushAllSizes(sizes);
    }

    @Override
    public void addSize(int size) throws RemoteException {
        assertionForPush();
        this.pushServiceSupplier.get().pushSize(size, PushItemUpdate.Add);
    }

    @Override
    public void removeSize(int size) throws RemoteException {
        assertionForPush();
        this.pushServiceSupplier.get().pushSize(size, PushItemUpdate.Remove);
    }

    @Override
    public void log(String msg) throws RemoteException {
        assertionForEvent();
        this.eventServiceSupplier.get().fireLog(msg);
    }

    private void assertionForPush() {
        try {
            Assertions.assertEquals(5 - 7, this.pushServiceSupplier.get().calc(5, 7));
        } catch (RemoteException e) {
            Assertions.fail(e);
        }
    }

    private void assertionForEvent() {
        try {
            Assertions.assertEquals((int) (Math.sin(5) * Math.cos(7) * 1000), this.eventServiceSupplier.get().calc(5, 7));
        } catch (RemoteException e) {
            Assertions.fail(e);
        }
    }
}
