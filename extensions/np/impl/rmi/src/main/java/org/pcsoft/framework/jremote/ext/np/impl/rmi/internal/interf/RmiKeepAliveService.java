package org.pcsoft.framework.jremote.ext.np.impl.rmi.internal.interf;

import org.pcsoft.framework.jremote.ext.np.api.annotation.KeepAlive;
import org.pcsoft.framework.jremote.ext.np.api.annotation.RemoteKeepAliveService;

import java.rmi.Remote;
import java.rmi.RemoteException;

@RemoteKeepAliveService
public interface RmiKeepAliveService extends Remote {
    @KeepAlive
    boolean ping(String uuid) throws RemoteException;
}
