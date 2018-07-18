package org.pcsoft.framework.jremote.core;

import org.pcsoft.framework.jremote.core.internal.manager.ServerProxyManager;

public final class RemoteServer implements Remote {
    private final ServerProxyManager proxyManager;

    RemoteServer() {
        proxyManager = new ServerProxyManager();
    }

    ServerProxyManager getProxyManager() {
        return proxyManager;
    }
}
