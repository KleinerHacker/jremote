package org.pcsoft.framework.jremote.core;

import org.pcsoft.framework.jremote.api.exception.JRemoteAnnotationException;
import org.pcsoft.framework.jremote.api.exception.JRemoteExecutionException;
import org.pcsoft.framework.jremote.core.internal.registry.ServerClientPluginRegistry;
import org.pcsoft.framework.jremote.core.internal.validation.Validator;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
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
        for (final Class<?> implClazz : controlServiceImplClasses) {
            if (implClazz.isInterface() || Modifier.isAbstract(implClazz.getModifiers()) || implClazz.isMemberClass())
                throw new JRemoteAnnotationException("Unable to use an abstract class, an interface or a member class as control service. " +
                        "It must be a implemented class of an control interface: " + implClazz.getName());

            final List<Class<?>> classList = extractControlServicesInterfaces(implClazz);
            if (classList.isEmpty())
                throw new JRemoteAnnotationException("Unable to find any implemented control service interface: " + implClazz.getName());

            final Object instance;
            try {
                instance = implClazz.getConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException e) {
                throw new JRemoteAnnotationException("Unable to find an empty public constructor for control service impl " + implClazz.getName(), e);
            } catch (InvocationTargetException e) {
                throw new JRemoteExecutionException("Unable to instantiate control service: method throws exception: " + implClazz.getName(), e);
            }

            for (final Class<?> controlServiceClass : classList) {
                remoteServer.getProxyManager().addRemoteControlServiceImpl(controlServiceClass, instance);
            }
        }
        return this;
    }

    @Override
    public RemoteServer build() {
        return remoteServer;
    }

    //region Helper Methods
    private static List<Class<?>> extractControlServicesInterfaces(Class<?> clazz) {
        final List<Class<?>> list = new ArrayList<>();
        findNextControlServicesInterfaces(clazz, list);

        return list;
    }

    private static void findNextControlServicesInterfaces(Class<?> clazz, List<Class<?>> list) {
        for (final Class<?> interfaceClass : clazz.getInterfaces()) {
            try {
                Validator.validateForRemoteService(interfaceClass);
                Validator.validateForRemoteControlService(interfaceClass);

                list.add(interfaceClass);
            } catch (JRemoteAnnotationException ignore) {
                //skip
            }
        }

        if (clazz.getSuperclass() != null) {
            findNextControlServicesInterfaces(clazz.getSuperclass(), list);
        }
    }
    //endregion
}
