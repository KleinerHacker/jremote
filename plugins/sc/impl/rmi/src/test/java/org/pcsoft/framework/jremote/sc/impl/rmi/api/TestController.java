package org.pcsoft.framework.jremote.sc.impl.rmi.api;

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
}
