package org.pcsoft.framework.jremote.core;

import org.pcsoft.framework.jremote.core.internal.manager.ServerProxyManager;
import org.pcsoft.framework.jremote.core.internal.registry.ServerClientPluginRegistry;
import org.pcsoft.framework.jremote.core.internal.type.PushModelHandler;
import org.pcsoft.framework.jremote.sc.api.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public final class RemoteServer implements Remote<ServerState> {
    private static final Logger LOGGER = LoggerFactory.getLogger(RemoteServer.class);

    private final String host;
    private final int port;

    private final ServerProxyManager proxyManager;
    private final PushManager pushManager = new PushManager();

    private Service registrationService = null;
    private Service keepAliveService = null;
    private final List<Service> controlServiceList = new ArrayList<>();

    private ServerState state = ServerState.Closed;
    private final List<Consumer<ServerState>> stateChangeListenerList = new ArrayList<>();

    RemoteServer(String host, int port) {
        this.host = host;
        this.port = port;

        this.proxyManager = new ServerProxyManager();
        this.proxyManager.addClientRegisteredListener(c -> {
            for (final Class<?> clazz : this.proxyManager.getModelHandlerClasses()) {
                final PushModelHandler modelHandler = this.proxyManager.getModelHandler(clazz);
                modelHandler.pushModelData(c);
            }
        });
    }

    ServerProxyManager getProxyManager() {
        return proxyManager;
    }

    public PushManager getPush() {
        return pushManager;
    }

    //region Delegates
    public Client[] getConnectedClients() {
        return proxyManager.getConnectedClients();
    }

    public void addClientRegisteredListener(Consumer<Client> l) {
        proxyManager.addClientRegisteredListener(l);
    }

    public void removeClientRegisteredListener(Consumer<Client> l) {
        proxyManager.removeClientRegisteredListener(l);
    }

    public void addClientUnregisteredListener(Consumer<Client> l) {
        proxyManager.addClientUnregisteredListener(l);
    }

    public void removeClientUnregisteredListener(Consumer<Client> l) {
        proxyManager.removeClientUnregisteredListener(l);
    }
    //endregion

    @Override
    public String getHost() {
        return host;
    }

    @Override
    public int getPort() {
        return port;
    }

    @Override
    public void open() throws IOException {
        if (state == ServerState.Opened)
            throw new IllegalStateException("Unable to open an opened service");

        LOGGER.info("Open remote server on " + host + ":" + port);

        createAndOpenControlServices();
        createAndOpenRegistrationService();
        createAndOpenKeepAliveService();

        state = ServerState.Opened;
        fireStateChange();
    }

    @Override
    public void close() throws Exception {
        if (state == ServerState.Closed)
            throw new IllegalStateException("Unable to close a closed service");

        LOGGER.info("Close remote server on " + host + ":" + port);

        closeAndRemoveKeepAliveService();
        closeAndRemoveRegistrationService();
        closeAndRemoveControlServices();

        state = ServerState.Closed;
        fireStateChange();
    }

    @Override
    public ServerState getState() {
        return state;
    }

    @Override
    public void addStateChangeListener(Consumer<ServerState> l) {
        stateChangeListenerList.add(l);
    }

    @Override
    public void removeStateChangeListener(Consumer<ServerState> l) {
        stateChangeListenerList.remove(l);
    }

    private void fireStateChange() {
        for (final Consumer<ServerState> l : stateChangeListenerList) {
            l.accept(state);
        }
    }

    //region Helper Methods
    private void createAndOpenRegistrationService() throws IOException {
        LOGGER.debug("> Create and open registration service");

        final Service service = ServerClientPluginRegistry.getInstance().createService();
        service.setServiceImplementation(proxyManager.getRemoteRegistrationServiceProxy());
        service.open(host, port);

        this.registrationService = service;
    }

    private void closeAndRemoveRegistrationService() throws IOException {
        LOGGER.debug("> Close and remove registration service");

        this.registrationService.close();
        this.registrationService = null;
    }

    private void createAndOpenKeepAliveService() throws IOException {
        LOGGER.debug("> Create and open keep alive service");

        final Service service = ServerClientPluginRegistry.getInstance().createService();
        service.setServiceImplementation(proxyManager.getRemoteKeepAliveServiceProxy());
        service.open(host, port);

        this.keepAliveService = service;
    }

    private void closeAndRemoveKeepAliveService() throws IOException {
        LOGGER.debug("> Close and remove keep alive service");

        this.keepAliveService.close();
        this.keepAliveService = null;
    }

    private void createAndOpenControlServices() throws IOException {
        LOGGER.debug("> Create and open control services");

        for (final Class<?> controlServiceClass : proxyManager.getRemoteControlClasses()) {
            final Object remoteControlServiceImpl = proxyManager.getRemoteControlServiceImpl(controlServiceClass);

            final Service service = ServerClientPluginRegistry.getInstance().createService();
            service.setServiceImplementation(remoteControlServiceImpl);
            service.open(host, port);

            this.controlServiceList.add(service);
        }
    }

    private void closeAndRemoveControlServices() throws IOException {
        LOGGER.debug("> Close and remove control services");

        for (final Service service : this.controlServiceList) {
            service.close();
        }
        controlServiceList.clear();
    }
    //endregion

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
