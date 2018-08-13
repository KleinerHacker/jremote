package org.pcsoft.framework.jremote.core;

import org.pcsoft.framework.jremote.core.internal.manager.ServerProxyManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

public final class RemoteServer implements Remote {
    private static final Logger LOGGER = LoggerFactory.getLogger(RemoteServer.class);

    private final String host;
    private final int port;

    private final ServerProxyManager proxyManager;
    private final PushManager pushManager = new PushManager();

    RemoteServer(String host, int port) {
        this.host = host;
        this.port = port;

        this.proxyManager = new ServerProxyManager();
    }

    ServerProxyManager getProxyManager() {
        return proxyManager;
    }

    public PushManager getPush() {
        return pushManager;
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

    @Override
    public ConnectionState getState() {
        return null;
    }

    @Override
    public void addStateChangeListener(Consumer<ConnectionState> l) {

    }

    @Override
    public void removeStateChangeListener(Consumer<ConnectionState> l) {

    }

    //region Helper Classes
    public final class PushManager {
        private PushManager() {
        }

        public <T>T getPushClient(Class<T> clazz) {
            return proxyManager.getRemotePushClientProxy(clazz);
        }
    }
    //endregion
}
