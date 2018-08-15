package org.pcsoft.framework.jremote.sc.commons.test.api;

import org.pcsoft.framework.jremote.api.Control;
import org.pcsoft.framework.jremote.api.RemoteControlService;

import java.rmi.Remote;
import java.rmi.RemoteException;

@RemoteControlService
public interface TestController extends Remote {
    @Control
    void changeName(String newValue) throws RemoteException;

    @Control
    void changeValue(int value) throws RemoteException;

    //Default test only
    default int calc(int x, int y) throws RemoteException {
        return x * y;
    }
}
