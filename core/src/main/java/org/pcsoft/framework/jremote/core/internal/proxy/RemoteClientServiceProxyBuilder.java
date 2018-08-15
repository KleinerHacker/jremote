package org.pcsoft.framework.jremote.core.internal.proxy;

import org.pcsoft.framework.jremote.api.internal.RemoteMethod;
import org.pcsoft.framework.jremote.core.internal.registry.ClientRegistry;
import org.pcsoft.framework.jremote.core.internal.registry.ServerClientPluginRegistry;
import org.pcsoft.framework.jremote.core.internal.validation.Validator;
import org.pcsoft.framework.jremote.io.api.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

//TODO: Insert into proxy builder architecture
final class RemoteClientServiceProxyBuilder {
    private static final Logger LOGGER = LoggerFactory.getLogger(RemoteClientServiceProxyBuilder.class);

    @SuppressWarnings("unchecked")
    static <T> T buildProxy(Class<T> clazz, String host, int port) {
        LOGGER.debug("Create client proxy for " + clazz.getName());

        Validator.validateForRemoteService(clazz);

        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, (proxy, method, args) -> {
            final boolean hasRemoteMethodAnnotation = Arrays.stream(method.getAnnotations())
                    .anyMatch(a -> a.annotationType().getAnnotation(RemoteMethod.class) != null);
            assert hasRemoteMethodAnnotation || method.isDefault();

            if (!hasRemoteMethodAnnotation) {
                if (method.isDefault())
                    return method.invoke(proxy, args);

                assert false;
            }

            return callClient(host, port, clazz, method, args);
        });
    }

    @SuppressWarnings("unchecked")
    static <T>T buildBroadcastProxy(Class<T> clazz, ClientRegistry clientRegistry) {
        LOGGER.debug("Create broadcast client proxy for " + clazz.getName());

        Validator.validateForRemoteBroadcastService(clazz);

        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, (proxy, method, args) -> {
            final boolean hasRemoteMethodAnnotation = Arrays.stream(method.getAnnotations())
                    .anyMatch(a -> a.annotationType().getAnnotation(RemoteMethod.class) != null);
            assert hasRemoteMethodAnnotation || method.isDefault();

            if (!hasRemoteMethodAnnotation) {
                if (method.isDefault())
                    return method.invoke(proxy, args);

                assert false;
            } else {
                assert method.getReturnType() == void.class;
            }

            for (final ClientRegistry.Client c : clientRegistry.getClients()) {
                callClient(c.getHost(), c.getPort(), clazz, method, args);
            }

            return null;
        });
    }

    private static <T> Object callClient(String host, int port, Class<T> clazz, Method method, Object[] args) throws IOException {
        try (final Client client = ServerClientPluginRegistry.getInstance().createClient()) {
            client.setServiceClass(clazz);
            client.open(host, port);
            return client.invokeRemote(method, args);
        }
    }
}
