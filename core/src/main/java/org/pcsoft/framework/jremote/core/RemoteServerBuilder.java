package org.pcsoft.framework.jremote.core;

import org.pcsoft.framework.jremote.api.exception.JRemoteAnnotationException;
import org.pcsoft.framework.jremote.api.exception.JRemoteExecutionException;
import org.pcsoft.framework.jremote.commons.util.ReflectionUtils;
import org.pcsoft.framework.jremote.core.internal.validation.Validator;
import org.pcsoft.framework.jremote.ext.config.api.ServerConfiguration;
import org.pcsoft.framework.jremote.ext.np.api.NetworkProtocol;
import org.pcsoft.framework.jremote.ext.up.api.UpdatePolicy;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;

/**
 * Represent a builder to create a {@link RemoteServer} with all its needed features.
 */
public final class RemoteServerBuilder implements RemoteBuilder<RemoteServer> {
    /**
     * Creates an instance of this builder (with default extension usage, see {@link ExtensionConfiguration})
     * @param configuration Server configuration (system settings) for the server to create
     * @return
     */
    public static RemoteServerBuilder create(ServerConfiguration configuration) {
        return create(configuration, new ExtensionConfiguration());
    }

    /**
     * Creates an instance of this builder
     * @param serverConfiguration Server configuration (system settings) for the server to create
     * @param extensionConfiguration Extension configuration (defines extensions to use for this server)
     * @return
     */
    public static RemoteServerBuilder create(ServerConfiguration serverConfiguration, ExtensionConfiguration extensionConfiguration) {
        serverConfiguration.validate();
        return new RemoteServerBuilder(serverConfiguration.getHost(), serverConfiguration.getPort(),
                extensionConfiguration.getNetworkProtocolClass(), extensionConfiguration.getUpdatePolicyClass());
    }

    private final RemoteServer remoteServer;

    private RemoteServerBuilder(String host, int port, Class<? extends NetworkProtocol> networkProtocolClass, Class<? extends UpdatePolicy> updatePolicyClass) {
        remoteServer = new RemoteServer(host, port, networkProtocolClass, updatePolicyClass);
        remoteServer.getProxyManager().setRemoteRegistrationServiceProxy(remoteServer.getNetworkProtocol().getRegistrationServiceClass());
        remoteServer.getProxyManager().setRemoteKeepAliveServiceProxy(remoteServer.getNetworkProtocol().getKeepAliveServiceClass());
    }

    public RemoteServerBuilder withPushClient(Class<?>... pushClientClasses) {
        for (final Class<?> clazz : pushClientClasses) {
            remoteServer.getProxyManager().addRemotePushClientProxy(clazz, remoteServer.getNetworkProtocol());
        }
        return this;
    }

    public RemoteServerBuilder withEventClient(Class<?>... eventClientClasses) {
        for (final Class<?> clazz : eventClientClasses) {
            remoteServer.getProxyManager().addRemoteEventClientProxy(clazz, remoteServer.getNetworkProtocol());
        }
        return this;
    }

    public RemoteServerBuilder withRemoteControlService(Class<?>... controlServiceImplClasses) {
        final Object[] impls = Arrays.stream(controlServiceImplClasses)
                .map(cl -> {
                    if (cl.isInterface() || Modifier.isAbstract(cl.getModifiers()) || cl.isMemberClass())
                        throw new JRemoteAnnotationException("Unable to use an abstract class, an interface or a member class as control service. " +
                                "It must be a implemented class of an control interface: " + cl.getName());

                    try {
                        return cl.getConstructor().newInstance();
                    } catch (InstantiationException | IllegalAccessException | NoSuchMethodException e) {
                        throw new JRemoteAnnotationException("Unable to find an empty public constructor for control service impl " + cl.getName(), e);
                    } catch (InvocationTargetException e) {
                        throw new JRemoteExecutionException("Unable to instantiate control service: method throws exception: " + cl.getName(), e);
                    }
                })
                .toArray(Object[]::new);

        return withRemoteControlService(impls);
    }

    public RemoteServerBuilder withRemoteControlService(Object... controlServiceImpls) {
        for (final Object impl : controlServiceImpls) {
            final List<Class<?>> classList = ReflectionUtils.findInterfaces(impl.getClass(), clazz -> {
                try {
                    Validator.validateForRemoteService(clazz);
                    Validator.validateForRemoteControlService(clazz);

                    return true;
                } catch (JRemoteAnnotationException e) {
                    return false;
                }
            });
            if (classList.isEmpty())
                throw new JRemoteAnnotationException("Unable to find any implemented control service interface: " + impl.getClass().getName());

            for (final Class<?> controlServiceClass : classList) {
                remoteServer.getProxyManager().addRemoteControlServiceImpl(controlServiceClass, impl);
            }
        }

        return this;
    }

    public RemoteServerBuilder withPushModelData(Class<?>... pushModelDataClasses) {
        final Object[] impls = Arrays.stream(pushModelDataClasses)
                .map(cl -> {
                    if (cl.isInterface() || Modifier.isAbstract(cl.getModifiers()) || cl.isMemberClass())
                        throw new JRemoteAnnotationException("Unable to use an abstract class, an interface or a member class as model data. " +
                                "It must be a implemented class of an remote model interface: " + cl.getName());

                    try {
                        return cl.getConstructor().newInstance();
                    } catch (InstantiationException | IllegalAccessException | NoSuchMethodException e) {
                        throw new JRemoteAnnotationException("Unable to find an empty public constructor for model data " + cl.getName(), e);
                    } catch (InvocationTargetException e) {
                        throw new JRemoteExecutionException("Unable to instantiate model data: method throws exception: " + cl.getName(), e);
                    }
                })
                .toArray(Object[]::new);

        return withPushModelData(impls);
    }

    public RemoteServerBuilder withPushModelData(Object... pushModelDataList) {
        for (final Object impl : pushModelDataList) {
            final List<Class<?>> classList = ReflectionUtils.findInterfaces(impl.getClass(), clazz -> {
                try {
                    Validator.validateForRemotePushModel(clazz);
                    return true;
                } catch (JRemoteAnnotationException e) {
                    return false;
                }
            });
            if (classList.isEmpty())
                throw new JRemoteAnnotationException("Unable to find any implemented remote model interface: " + impl.getClass().getName());

            for (final Class<?> modelDataClass : classList) {
                remoteServer.getProxyManager().addPushModelHandler(modelDataClass, impl);
            }
        }

        return this;
    }

    @Override
    public RemoteServer build() {
        return remoteServer;
    }
}
