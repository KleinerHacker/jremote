package org.pcsoft.framework.jremote.sc.commons.test.api;

import org.pcsoft.framework.jremote.api.PushMethod;
import org.pcsoft.framework.jremote.api.RemotePushService;

import java.rmi.Remote;
import java.rmi.RemoteException;

@RemotePushService
public interface TestPushService extends Remote {
    @PushMethod
    void pushName(String name) throws RemoteException;

    @PushMethod
    void pushValue(int value) throws RemoteException;

    //Default test only
    default int calc(int x, int y) throws RemoteException {
        return x - y;
    }
}
