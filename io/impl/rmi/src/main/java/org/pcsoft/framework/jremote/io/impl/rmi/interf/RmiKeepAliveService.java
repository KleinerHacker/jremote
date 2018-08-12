package org.pcsoft.framework.jremote.io.impl.rmi.interf;

import org.pcsoft.framework.jremote.io.api.annotation.KeepAlive;
import org.pcsoft.framework.jremote.io.api.annotation.RemoteKeepAliveService;

import java.rmi.Remote;

@RemoteKeepAliveService
public interface RmiKeepAliveService extends Remote {
    @KeepAlive
    boolean ping(String uuid);
}
