package org.pcsoft.framework.jremote.core.test.api;

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

    //Default test only
    default int calc(int x, int y) throws RemoteException {
        return x - y;
    }
}
