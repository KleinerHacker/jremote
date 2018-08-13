package org.pcsoft.framework.jremote.core;

import org.pcsoft.framework.jremote.core.internal.manager.ServerProxyManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class RemoteServer implements Remote {
    private static final Logger LOGGER = LoggerFactory.getLogger(RemoteServer.class);

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
        LOGGER.info("Open remote server on " + host + ":" + port);
    }

    @Override
    public void close() throws Exception {
        LOGGER.info("Close remote server on " + host + ":" + port);
    }
}
