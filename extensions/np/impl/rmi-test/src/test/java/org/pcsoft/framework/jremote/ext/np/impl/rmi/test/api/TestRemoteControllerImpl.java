package org.pcsoft.framework.jremote.ext.np.impl.rmi.test.api;

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
        this.pushServiceSupplier.get().pushName(newValue);
    }

    @Override
    public void changeValue(int value) throws RemoteException {
        this.pushServiceSupplier.get().pushValue(value);
    }

    @Override
    public void changeWordList(List<String> words) throws RemoteException {
        this.pushServiceSupplier.get().pushAllWords(words);
    }

    @Override
    public void addWord(String word) throws RemoteException {
        this.pushServiceSupplier.get().pushWord(word, PushItemUpdate.Add);
    }

    @Override
    public void removeWord(String word) throws RemoteException {
        this.pushServiceSupplier.get().pushWord(word, PushItemUpdate.Remove);
    }

    @Override
    public void changeSizeList(int[] sizes) throws RemoteException {
        this.pushServiceSupplier.get().pushAllSizes(sizes);
    }

    @Override
    public void addSize(int size) throws RemoteException {
        this.pushServiceSupplier.get().pushSize(size, PushItemUpdate.Add);
    }

    @Override
    public void removeSize(int size) throws RemoteException {
        this.pushServiceSupplier.get().pushSize(size, PushItemUpdate.Remove);
    }

    @Override
    public void log(String msg) throws RemoteException {
        this.eventServiceSupplier.get().fireLog(msg);
    }
}
