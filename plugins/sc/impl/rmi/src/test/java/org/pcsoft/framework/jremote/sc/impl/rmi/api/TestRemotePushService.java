package org.pcsoft.framework.jremote.sc.impl.rmi.api;

import org.pcsoft.framework.jremote.api.Push;
import org.pcsoft.framework.jremote.api.RemotePushService;

import java.rmi.Remote;
import java.rmi.RemoteException;

@RemotePushService
public interface TestRemotePushService extends Remote {
    @Push(modelClass = TestRemotePushModel.class, property = "name")
    void pushName(String name) throws RemoteException;

    @Push(modelClass = TestRemotePushModel.class, property = "value")
    void pushValue(int value) throws RemoteException;
}
