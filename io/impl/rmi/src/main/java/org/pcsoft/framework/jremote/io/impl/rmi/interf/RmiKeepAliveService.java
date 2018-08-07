package org.pcsoft.framework.jremote.io.impl.rmi.interf;

import org.pcsoft.framework.jremote.io.api.annotation.KeepAlive;
import org.pcsoft.framework.jremote.io.api.annotation.KeepAliveService;

import java.rmi.Remote;

@KeepAliveService
public interface RmiKeepAliveService extends Remote {
    @KeepAlive
    boolean ping(String uuid);
}
