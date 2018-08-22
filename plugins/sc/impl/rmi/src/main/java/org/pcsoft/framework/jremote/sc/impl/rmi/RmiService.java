package org.pcsoft.framework.jremote.sc.impl.rmi;

import org.apache.commons.lang3.StringUtils;
import org.pcsoft.framework.jremote.commons.AnnotationUtils;
import org.pcsoft.framework.jremote.commons.ReflectionUtils;
import org.pcsoft.framework.jremote.sc.api.ServiceBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Proxy;
import java.rmi.AlreadyBoundException;
import java.rmi.ConnectException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class RmiService extends ServiceBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(RmiService.class);

    private Registry registry;
    private Remote exportObject;

    @Override
    public void open(String host, int port) throws IOException {
        if (getServiceImplementation() == null)
            throw new IllegalStateException("Service not ready yet: no service implementation was set");
        if (!(getServiceImplementation() instanceof Remote))
            throw new IllegalStateException("Needed remote interface is not implemented: " + getServiceImplementation().getClass().getName());

        //Try get existing registry
        registry = LocateRegistry.getRegistry(host, port);
        try {
            registry.list();
        } catch (ConnectException e) {
            //If failed try create new registry
            registry = LocateRegistry.createRegistry(port);
        }

        LOGGER.debug("> Open RMI service at " + host + ":" + port + " with names " + StringUtils.join(getServiceNames(), ','));
        exportObject = UnicastRemoteObject.exportObject((Remote) getServiceImplementation(), 0);
        for (final String serviceName : getServiceNames()) {
            try {
                registry.bind(serviceName, exportObject);
            } catch (AlreadyBoundException e) {
                throw new IOException("Already bind on address " + host + ":" + port + " with name " + serviceName, e);
            }
        }
    }

    @Override
    public void close() throws IOException {
        LOGGER.debug("> Close RMI service with names " + StringUtils.join(getServiceNames(), ','));
        for (final String serviceName : getServiceNames()) {
            try {
                registry.unbind(serviceName);
            } catch (NotBoundException e) {
                throw new IOException("Exception while unbind RMI service with name " + serviceName, e);
            }
        }
        registry = null;
    }

    private List<String> getServiceNames() {
        final List<String> list = new ArrayList<>();

        if (Proxy.isProxyClass(getServiceImplementation().getClass())) {
            list.add(getServiceImplementation().getClass().getInterfaces()[0].getName());
        } else {
            final List<Class<?>> classList = ReflectionUtils.findInterfaces(getServiceImplementation().getClass(), AnnotationUtils::isRemoteService);
            list.addAll(
                    classList.stream()
                            .map(Class::getName)
                            .collect(Collectors.toList())
            );
        }

        return list;
    }
}
