package org.pcsoft.framework.jremote.core.test.api;

import org.pcsoft.framework.jremote.api.Push;
import org.pcsoft.framework.jremote.api.RemotePushService;

import java.rmi.Remote;
import java.rmi.RemoteException;

@RemotePushService
public interface TestEventService extends Remote {
    @Push
    void pushLog(String msg) throws RemoteException;

    default int calc(int x, int y) throws RemoteException {
        return (int) (Math.sin(x) * Math.cos(y) * 1000);
    }
}
