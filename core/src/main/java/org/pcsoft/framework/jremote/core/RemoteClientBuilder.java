package org.pcsoft.framework.jremote.core;

import org.pcsoft.framework.jremote.ext.config.api.ClientConfiguration;
import org.pcsoft.framework.jremote.ext.np.api.NetworkProtocol;
import org.pcsoft.framework.jremote.ext.up.api.UpdatePolicy;

/**
 * Represents a builder to create a {@link RemoteClient} with all its features.
 */
public final class RemoteClientBuilder implements RemoteBuilder<RemoteClient> {
    /**
     * Creates an instance of this builder (with default extension usage, see {@link ExtensionConfiguration})
     * @param configuration Client configuration (system settings) for the client to create
     * @return
     */
    public static RemoteClientBuilder create(ClientConfiguration configuration) {
        return create(configuration, new ExtensionConfiguration());
    }

    /**
     * Creates an instance of this builder
     * @param clientConfiguration Client configuration (system settings) for the client to create
     * @param extensionConfiguration Extension configuration (defines extensions to use for this client)
     * @return
     */
    public static RemoteClientBuilder create(ClientConfiguration clientConfiguration, ExtensionConfiguration extensionConfiguration) {
        clientConfiguration.validate();
        return new RemoteClientBuilder(clientConfiguration.getHost(), clientConfiguration.getPort(), clientConfiguration.getOwnPort(),
                extensionConfiguration.getNetworkProtocolClass(), extensionConfiguration.getUpdatePolicyClass());
    }

    private final RemoteClient remoteClient;

    private RemoteClientBuilder(String host, int port, int ownPort, Class<? extends NetworkProtocol> networkProtocolClass,
                                Class<? extends UpdatePolicy> updatePolicyClass) {
        remoteClient = new RemoteClient(host, port, ownPort, networkProtocolClass, updatePolicyClass);
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
            remoteClient.getProxyManager().addRemotePushServiceProxy(pushServiceClass, remoteClient.getUpdatePolicy());
        }
        return this;
    }

    public final RemoteClientBuilder withRemoteEventService(Class<?>... eventServiceClasses) {
        for (final Class<?> pushServiceClass : eventServiceClasses) {
            remoteClient.getProxyManager().addRemoteEventServiceProxy(pushServiceClass, remoteClient.getUpdatePolicy());
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
