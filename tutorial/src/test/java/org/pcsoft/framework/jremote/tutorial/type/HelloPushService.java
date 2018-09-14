package org.pcsoft.framework.jremote.tutorial.type;

import org.pcsoft.framework.jremote.api.Push;
import org.pcsoft.framework.jremote.api.RemotePushService;

import java.rmi.Remote;
import java.rmi.RemoteException;

@RemotePushService
public interface HelloPushService extends Remote {
    @Push(property = HelloPushModel.PROP_GREETING_COUNT, modelClass = HelloPushModel.class)
    void pushGreetingCount(int count) throws RemoteException;
}
