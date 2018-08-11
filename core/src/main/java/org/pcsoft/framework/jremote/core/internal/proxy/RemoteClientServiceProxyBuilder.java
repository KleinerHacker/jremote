package org.pcsoft.framework.jremote.core.internal.proxy;

import org.pcsoft.framework.jremote.api.exception.JRemoteAnnotationException;
import org.pcsoft.framework.jremote.core.internal.registry.ServerClientPluginRegistry;
import org.pcsoft.framework.jremote.io.api.Client;
import org.pcsoft.framework.jremote.io.api.annotation.Registration;
import org.pcsoft.framework.jremote.io.api.annotation.RemoteRegistrationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Proxy;

final class RemoteClientServiceProxyBuilder {
    private static final Logger LOGGER = LoggerFactory.getLogger(RemoteClientServiceProxyBuilder.class);

    @SuppressWarnings("unchecked")
    static <T> T buildProxy(Class<T> clazz, String host, int port) {
        LOGGER.debug("Create client proxy for " + clazz.getName());

        if (clazz.getAnnotation(RemoteRegistrationService.class) == null)
            throw new JRemoteAnnotationException(String.format("Unable to find annotation %s on class %s", RemoteRegistrationService.class.getName(), clazz.getName()));

        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, (proxy, method, args) -> {
            final Registration registration = method.getAnnotation(Registration.class);
            if (registration == null) {
                if (method.isDefault())
                    return method.invoke(proxy, args);

                throw new JRemoteAnnotationException(String.format("Found not default method with missing annotation %s on %s#%s",
                        Registration.class.getName(), clazz.getName(), method.getName()));
            }

            try (final Client client = ServerClientPluginRegistry.getInstance().createClient()) {
                client.open(host, port);
                client.setServiceClass(clazz);
                switch (registration.value()) {
                    case Register:
                        if (method.getParameterCount() != 3 || method.getParameterTypes()[0] != String.class || method.getParameterTypes()[1])
                        break;
                    case Unregister:
                        break;
                    default:
                        throw new RuntimeException();
                }
                client.invokeRemote(method, args);
            }

            return null;
        });
    }
}
