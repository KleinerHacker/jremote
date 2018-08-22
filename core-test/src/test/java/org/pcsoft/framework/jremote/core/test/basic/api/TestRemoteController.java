package org.pcsoft.framework.jremote.core.test.basic.api;

import org.pcsoft.framework.jremote.api.Control;
import org.pcsoft.framework.jremote.api.RemoteControlService;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

@RemoteControlService
public interface TestRemoteController extends Remote {
    @Control
    void changeName(String newValue) throws RemoteException;

    @Control
    void changeValue(int value) throws RemoteException;

    @Control
    void changeWordList(List<String> words) throws RemoteException;

    @Control
    void addWord(String word) throws RemoteException;

    @Control
    void removeWord(String word) throws RemoteException;

    @Control
    void changeSizeList(int[] sizes) throws RemoteException;

    @Control
    void addSize(int size) throws RemoteException;

    @Control
    void removeSize(int size) throws RemoteException;

    @Control
    void log(String msg) throws RemoteException;

    //Default test only
    default int calc(int x, int y) throws RemoteException {
        return x * y;
    }
}
