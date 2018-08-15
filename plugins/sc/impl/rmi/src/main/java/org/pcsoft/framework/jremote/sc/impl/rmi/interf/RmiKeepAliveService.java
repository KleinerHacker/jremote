package org.pcsoft.framework.jremote.sc.impl.rmi.interf;

import org.pcsoft.framework.jremote.sc.api.annotation.KeepAlive;
import org.pcsoft.framework.jremote.sc.api.annotation.RemoteKeepAliveService;

import java.rmi.Remote;

@RemoteKeepAliveService
public interface RmiKeepAliveService extends Remote {
    @KeepAlive
    boolean ping(String uuid);
}
