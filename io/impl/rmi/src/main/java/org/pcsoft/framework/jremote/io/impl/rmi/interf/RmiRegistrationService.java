package org.pcsoft.framework.jremote.io.impl.rmi.interf;

import org.pcsoft.framework.jremote.io.api.annotation.Registration;
import org.pcsoft.framework.jremote.io.api.annotation.RemoteRegistrationService;

import java.rmi.Remote;
import java.rmi.RemoteException;

@RemoteRegistrationService
public interface RmiRegistrationService extends Remote {
    @Registration(Registration.RegistrationType.Register)
    void register(String uuid, String host, int port) throws RemoteException;

    @Registration(Registration.RegistrationType.Unregister)
    void unregister(String uuid) throws RemoteException;
}
