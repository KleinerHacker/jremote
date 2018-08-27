package org.pcsoft.framework.jremote.np.impl.rmi.interf;

import org.pcsoft.framework.jremote.np.api.annotation.KeepAlive;
import org.pcsoft.framework.jremote.np.api.annotation.RemoteKeepAliveService;

import java.rmi.Remote;
import java.rmi.RemoteException;

@RemoteKeepAliveService
public interface RmiKeepAliveService extends Remote {
    @KeepAlive
    boolean ping(String uuid) throws RemoteException;
}
