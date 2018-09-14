package org.pcsoft.framework.jremote.tutorial.type;

import org.pcsoft.framework.jremote.api.Event;
import org.pcsoft.framework.jremote.api.RemoteEventService;

import java.rmi.Remote;
import java.rmi.RemoteException;

@RemoteEventService
public interface HelloEventService extends Remote {
    @Event(event = HelloEventReceiver.EVENT_GREETING, eventClass = HelloEventReceiver.class)
    void onGreeting(String greeting) throws RemoteException;
}
