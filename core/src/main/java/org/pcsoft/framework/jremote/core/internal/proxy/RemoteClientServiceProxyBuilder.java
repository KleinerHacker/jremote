package org.pcsoft.framework.jremote.core.internal.proxy;

import org.pcsoft.framework.jremote.api.internal.RemoteMethod;
import org.pcsoft.framework.jremote.core.internal.registry.ClientRegistry;
import org.pcsoft.framework.jremote.core.internal.validation.Validator;
import org.pcsoft.framework.jremote.np.api.Client;
import org.pcsoft.framework.jremote.np.api.NetworkProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

//TODO: Insert into proxy builder architecture
final class RemoteClientServiceProxyBuilder {
    private static final Logger LOGGER = LoggerFactory.getLogger(RemoteClientServiceProxyBuilder.class);

    @SuppressWarnings("unchecked")
    static <T> T buildProxy(Class<T> clazz, String host, int port, NetworkProtocol networkProtocol) {
        LOGGER.debug("Create client proxy for " + clazz.getName());

        Validator.validateForRemoteService(clazz);

        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, (proxy, method, args) -> {
            final boolean hasRemoteMethodAnnotation = Arrays.stream(method.getAnnotations())
                    .anyMatch(a -> a.annotationType().getAnnotation(RemoteMethod.class) != null);
            assert hasRemoteMethodAnnotation || method.isDefault();

            if (!hasRemoteMethodAnnotation) {
                if (method.isDefault())
                    return MethodHandles.privateLookupIn(method.getDeclaringClass(), MethodHandles.lookup())
                            .unreflectSpecial(method, method.getDeclaringClass())
                            .bindTo(proxy)
                            .invokeWithArguments(args);

                assert false;
            }

            return callClient(host, port, clazz, method, args, networkProtocol);
        });
    }

    @SuppressWarnings("unchecked")
    static <T> T buildBroadcastProxy(Class<T> clazz, ClientRegistry clientRegistry, NetworkProtocol networkProtocol) {
        LOGGER.debug("Create broadcast client proxy for " + clazz.getName());

        Validator.validateForRemoteBroadcastService(clazz);

        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, (proxy, method, args) -> {
            final boolean hasRemoteMethodAnnotation = Arrays.stream(method.getAnnotations())
                    .anyMatch(a -> a.annotationType().getAnnotation(RemoteMethod.class) != null);
            assert hasRemoteMethodAnnotation || method.isDefault();

            if (!hasRemoteMethodAnnotation) {
                if (method.isDefault())
                    return MethodHandles.privateLookupIn(method.getDeclaringClass(), MethodHandles.lookup())
                            .unreflectSpecial(method, method.getDeclaringClass())
                            .bindTo(proxy)
                            .invokeWithArguments(args);

                assert false;
            } else {
                assert method.getReturnType() == void.class;
            }

            for (final org.pcsoft.framework.jremote.core.Client c : clientRegistry.getClients()) {
                callClient(c.getHost(), c.getPort(), clazz, method, args, networkProtocol);
            }

            return null;
        });
    }

    private static <T> Object callClient(String host, int port, Class<T> clazz, Method method, Object[] args, NetworkProtocol networkProtocol) throws IOException {
        try (final Client client = networkProtocol.createClient(clazz)) {
            client.open(host, port);
            return client.invokeRemote(method, args);
        }
    }
}
