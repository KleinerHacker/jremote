package org.pcsoft.framework.jremote.core;

import org.pcsoft.framework.jremote.core.internal.manager.ClientProxyManager;
import org.pcsoft.framework.jremote.core.internal.processor.KeepAliveProcessor;
import org.pcsoft.framework.jremote.core.internal.registry.NetworkProtocolPluginRegistry;
import org.pcsoft.framework.jremote.np.api.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public final class RemoteClient extends RemoteBase<ClientState> {
    private static final Logger LOGGER = LoggerFactory.getLogger(RemoteClient.class);

    private final UUID uuid = UUID.randomUUID();
    private final int ownPort;

    private final ClientProxyManager proxyManager;
    private final DataManager dataManager = new DataManager();
    private final ControlManager controlManager = new ControlManager();

    private final List<Service> pushServiceList = new ArrayList<>();
    private final List<Service> eventServiceList = new ArrayList<>();
    private final KeepAliveProcessor keepAliveProcessor = new KeepAliveProcessor();

    RemoteClient(String host, int port, int ownPort) {
        super(host, port);
        this.ownPort = ownPort;

        this.proxyManager = new ClientProxyManager();
    }

    ClientProxyManager getProxyManager() {
        return proxyManager;
    }

    public DataManager getData() {
        if (getLifecycleState() != LifecycleState.Opened)
            throw new IllegalStateException("Cannot get data manager now: client not opened");

        return dataManager;
    }

    public ControlManager getControl() {
        if (getLifecycleState() != LifecycleState.Opened)
            throw new IllegalStateException("Cannot get control manager now: client not opened");

        return controlManager;
    }

    public UUID getUuid() {
        return uuid;
    }

    @Override
    public String getHost() {
        return host;
    }

    @Override
    public int getPort() {
        return port;
    }

    public int getOwnPort() {
        return ownPort;
    }

    @Override
    public void doOpen() throws IOException {
        LOGGER.info("Open Remote Client on " + host + ":" + port + " (push on " + ownPort + ")");

        createAndOpenPushServices();
        createAndOpenEventServices();
        startKeepAlive();
    }

    @Override
    public void doClose() throws Exception {
        LOGGER.info("Close Remote Client on " + host + ":" + port);

        stopKeepAlive();
        closeAndRemoveEventServices();
        closeAndRemovePushServices();
    }

    @Override
    public ClientState getState() {
        if (getLifecycleState() != LifecycleState.Opened)
            throw new IllegalStateException("Cannot get state now: client not opened");

        return keepAliveProcessor.getConnectionState();
    }

    @Override
    public void addStateChangeListener(Consumer<ClientState> l) {
        keepAliveProcessor.addStateChangeListener(l);
    }

    @Override
    public void removeStateChangeListener(Consumer<ClientState> l) {
        keepAliveProcessor.removeStateChangeListener(l);
    }

    //region Helper Methods
    private void startKeepAlive() {
        LOGGER.debug("> Start keep alive thread");

        keepAliveProcessor.setup(getProxyManager().getRemoteRegistrationClient(), getProxyManager().getRemoteKeepAliveClient(), this);
        keepAliveProcessor.start();
    }

    private void stopKeepAlive() {
        LOGGER.debug("> Stop keep alive thread");

        if (keepAliveProcessor.isRunning()) {
            keepAliveProcessor.stop();
        }
        proxyManager.getRemoteRegistrationClient().unregister(uuid.toString());
    }

    private void createAndOpenPushServices() throws IOException {
        LOGGER.debug("> Create and open push services");
        for (final Class<?> pushClass : proxyManager.getRemotePushClasses()) {
            final Object pushServiceProxy = proxyManager.getRemotePushServiceProxy(pushClass);

            final Service service = NetworkProtocolPluginRegistry.getInstance().createService();
            service.setServiceImplementation(pushServiceProxy);
            service.open(host, ownPort);

            this.pushServiceList.add(service);
        }
    }

    private void closeAndRemovePushServices() throws IOException {
        LOGGER.debug("> Close and remove push services");

        for (final Service service : this.pushServiceList) {
            service.close();
        }
        this.pushServiceList.clear();
    }

    private void createAndOpenEventServices() throws IOException {
        LOGGER.debug("> Create and open event services");
        for (final Class<?> pushClass : proxyManager.getRemoteEventClasses()) {
            final Object eventServiceProxy = proxyManager.getRemoteEventServiceProxy(pushClass);

            final Service service = NetworkProtocolPluginRegistry.getInstance().createService();
            service.setServiceImplementation(eventServiceProxy);
            service.open(host, ownPort);

            this.eventServiceList.add(service);
        }
    }

    private void closeAndRemoveEventServices() throws IOException {
        LOGGER.debug("> Close and remove event services");

        for (final Service service : this.eventServiceList) {
            service.close();
        }
        this.eventServiceList.clear();
    }
    //endregion

    //region Helper Classes
    public final class DataManager {
        private DataManager() {
        }

        public <T> T getRemotePushModel(Class<T> clazz) {
            return proxyManager.getRemotePushModelProxy(clazz);
        }

        public <T> T getRemotePushObserver(Class<T> clazz) {
            return proxyManager.getRemotePushObserverProxy(clazz);
        }

        public <T> T getRemoteEventReceiver(Class<T> clazz) {
            return proxyManager.getRemoteEventReceiverProxy(clazz);
        }
    }

    public final class ControlManager {
        private ControlManager() {
        }

        public <T> T getControlClient(Class<T> clazz) {
            return proxyManager.getRemoteControlClientProxy(clazz);
        }
    }
    //endregion
}
