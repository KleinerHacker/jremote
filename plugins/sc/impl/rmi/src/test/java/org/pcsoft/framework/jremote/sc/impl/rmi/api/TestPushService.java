package org.pcsoft.framework.jremote.sc.impl.rmi.api;

import org.pcsoft.framework.jremote.api.Push;
import org.pcsoft.framework.jremote.api.RemotePushService;

import java.rmi.Remote;
import java.rmi.RemoteException;

@RemotePushService
public interface TestPushService extends Remote {
    @Push
    void pushName(String name) throws RemoteException;

    @Push
    void pushValue(int value) throws RemoteException;
}
