package org.pcsoft.framework.jremote.core;

import org.pcsoft.framework.jremote.api.exception.JRemoteAnnotationException;
import org.pcsoft.framework.jremote.api.exception.JRemoteExecutionException;
import org.pcsoft.framework.jremote.commons.ReflectionUtils;
import org.pcsoft.framework.jremote.core.internal.registry.ServerClientPluginRegistry;
import org.pcsoft.framework.jremote.core.internal.validation.Validator;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;

public final class RemoteServerBuilder implements RemoteBuilder<RemoteServer> {
    public static RemoteServerBuilder create(String host, int port) {
        return new RemoteServerBuilder(host, port);
    }

    private final RemoteServer remoteServer;

    private RemoteServerBuilder(String host, int port) {
        remoteServer = new RemoteServer(host, port);
        remoteServer.getProxyManager().setRemoteRegistrationServiceProxy(
                ServerClientPluginRegistry.getInstance().getRegistrationServiceClass()
        );
        remoteServer.getProxyManager().setRemoteKeepAliveServiceProxy(
                ServerClientPluginRegistry.getInstance().getKeepAliveServiceClass()
        );
    }

    public RemoteServerBuilder withPushClient(Class<?>... pushClientClasses) {
        for (final Class<?> clazz : pushClientClasses) {
            remoteServer.getProxyManager().addRemotePushClientProxy(clazz);
        }
        return this;
    }

    public RemoteServerBuilder withControlService(Class<?>... controlServiceImplClasses) {
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

        return withControlService(impls);
    }

    public RemoteServerBuilder withControlService(Object... controlServiceImpls) {
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

    @Override
    public RemoteServer build() {
        return remoteServer;
    }
}
