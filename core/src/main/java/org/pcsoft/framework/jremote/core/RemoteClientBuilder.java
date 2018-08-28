package org.pcsoft.framework.jremote.core;

import org.pcsoft.framework.jremote.ext.config.api.ClientConfiguration;
import org.pcsoft.framework.jremote.ext.np.api.NetworkProtocol;

public final class RemoteClientBuilder implements RemoteBuilder<RemoteClient> {
    public static RemoteClientBuilder create(String host, int port, int ownPort, Class<? extends NetworkProtocol> networkProtocolClass) {
        return new RemoteClientBuilder(host, port, ownPort, networkProtocolClass);
    }

    public static RemoteClientBuilder create(ClientConfiguration configuration, Class<? extends NetworkProtocol> networkProtocolClass) {
        configuration.validate();
        return create(configuration.getHost(), configuration.getPort(), configuration.getOwnPort(), networkProtocolClass);
    }

    private final RemoteClient remoteClient;

    private RemoteClientBuilder(String host, int port, int ownPort, Class<? extends NetworkProtocol> networkProtocolClass) {
        remoteClient = new RemoteClient(host, port, ownPort, networkProtocolClass);
        remoteClient.getProxyManager().setRemoteRegistrationClient(host, port, remoteClient.getNetworkProtocol());
        remoteClient.getProxyManager().setRemoteKeepAliveClient(host, port, remoteClient.getNetworkProtocol());
    }

    public final RemoteClientBuilder withRemotePushModel(Class<?>... pushModelClasses) {
        for (final Class<?> modelClass : pushModelClasses) {
            remoteClient.getProxyManager().addRemotePushModelProxy(modelClass);
        }
        return this;
    }

    public final RemoteClientBuilder withRemotePushObserver(Class<?>... pushObserverClasses) {
        for (final Class<?> observerClass : pushObserverClasses) {
            remoteClient.getProxyManager().addRemotePushObserverProxy(observerClass);
        }
        return this;
    }

    public final RemoteClientBuilder withRemoteEventReceiver(Class<?>... eventReceiverClasses) {
        for (final Class<?> observerClass : eventReceiverClasses) {
            remoteClient.getProxyManager().addRemoteEventReceiverProxy(observerClass);
        }
        return this;
    }

    public final RemoteClientBuilder withRemotePushService(Class<?>... pushServiceClasses) {
        for (final Class<?> pushServiceClass : pushServiceClasses) {
            remoteClient.getProxyManager().addRemotePushServiceProxy(pushServiceClass);
        }
        return this;
    }

    public final RemoteClientBuilder withRemoteEventService(Class<?>... eventServiceClasses) {
        for (final Class<?> pushServiceClass : eventServiceClasses) {
            remoteClient.getProxyManager().addRemoteEventServiceProxy(pushServiceClass);
        }
        return this;
    }

    public final RemoteClientBuilder withRemoteControlClient(Class<?>... controlClientClasses) {
        for (final Class<?> pushServiceClass : controlClientClasses) {
            remoteClient.getProxyManager().addRemoteControlClientProxy(
                    pushServiceClass, remoteClient.getHost(), remoteClient.getPort(), remoteClient.getNetworkProtocol());
        }
        return this;
    }

    @Override
    public RemoteClient build() {
        return remoteClient;
    }
}
