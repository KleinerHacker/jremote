package org.pcsoft.framework.jremote.ext.np.impl.rmi.test.api;

import org.pcsoft.framework.jremote.api.Event;
import org.pcsoft.framework.jremote.api.RemoteEventService;

import java.rmi.Remote;
import java.rmi.RemoteException;

@RemoteEventService
public interface TestRemoteEventService extends Remote {
    @Event(eventClass = TestRemoteEventReceiver.class, event = "log")
    void fireLog(String msg) throws RemoteException;
}
