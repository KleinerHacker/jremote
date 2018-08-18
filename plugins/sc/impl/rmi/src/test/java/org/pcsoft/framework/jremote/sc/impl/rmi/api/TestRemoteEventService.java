package org.pcsoft.framework.jremote.sc.impl.rmi.api;

import org.pcsoft.framework.jremote.api.Event;
import org.pcsoft.framework.jremote.api.RemoteEventService;

import java.rmi.Remote;
import java.rmi.RemoteException;

@RemoteEventService
public interface TestRemoteEventService extends Remote {
    @Event
    void fireLog(String msg) throws RemoteException;
}
