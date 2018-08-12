package org.pcsoft.framework.jremote.core.internal.proxy;

import org.pcsoft.framework.jremote.api.internal.RemoteMethod;
import org.pcsoft.framework.jremote.core.internal.registry.ServerClientPluginRegistry;
import org.pcsoft.framework.jremote.core.internal.validation.Validator;
import org.pcsoft.framework.jremote.io.api.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

            if (hasRemoteMethodAnnotation) {
                if (method.isDefault())
                    return method.invoke(proxy, args);

                assert false;
            }

            try (final Client client = ServerClientPluginRegistry.getInstance().createClient()) {
                client.open(host, port);
                client.setServiceClass(clazz);
                return client.invokeRemote(method, args);
            }
        });
    }
}
