package org.pcsoft.framework.jremote.core.test.basic.api;

import org.pcsoft.framework.jremote.api.Event;
import org.pcsoft.framework.jremote.api.RemoteEventService;

import java.rmi.Remote;
import java.rmi.RemoteException;

@RemoteEventService
public interface TestRemoteEventService extends Remote {
    @Event(eventClass = TestRemoteEventReceiver.class, event = "log")
    void fireLog(String msg) throws RemoteException;

    default int calc(int x, int y) throws RemoteException {
        return (int) (Math.sin(x) * Math.cos(y) * 1000);
    }
}
