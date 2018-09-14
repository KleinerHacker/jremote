package org.pcsoft.framework.jremote.tutorial.type;


import org.pcsoft.framework.jremote.api.Control;
import org.pcsoft.framework.jremote.api.RemoteControlService;

import java.rmi.Remote;
import java.rmi.RemoteException;

@RemoteControlService
public interface HelloControlService extends Remote {
    @Control
    void sayHello(String name) throws RemoteException;
}
