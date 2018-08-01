package org.pcsoft.framework.jremote.core;

import org.pcsoft.framework.jremote.core.internal.manager.ServerProxyManager;

public final class RemoteServer implements Remote {
    private final String host;
    private final int port;

    private final ServerProxyManager proxyManager;

    RemoteServer(String host, int port) {
        this.host = host;
        this.port = port;

        this.proxyManager = new ServerProxyManager();
    }

    ServerProxyManager getProxyManager() {
        return proxyManager;
    }

    @Override
    public String getHost() {
        return host;
    }

    @Override
    public int getPort() {
        return port;
    }

    @Override
    public void open() {

    }

    @Override
    public void close() throws Exception {

    }
}
