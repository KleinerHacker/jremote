package org.pcsoft.framework.jremote.ext.np.impl.rmi.internal.interf;

import org.pcsoft.framework.jremote.ext.np.api.annotation.Registration;
import org.pcsoft.framework.jremote.ext.np.api.annotation.RemoteRegistrationService;

import java.rmi.Remote;
import java.rmi.RemoteException;

@RemoteRegistrationService
public interface RmiRegistrationService extends Remote {
    @Registration(Registration.RegistrationType.Register)
    void register(String uuid, String host, int port) throws RemoteException;

    @Registration(Registration.RegistrationType.Unregister)
    void unregister(String uuid) throws RemoteException;
}
