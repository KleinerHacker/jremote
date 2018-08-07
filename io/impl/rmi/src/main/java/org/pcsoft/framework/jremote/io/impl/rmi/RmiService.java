package org.pcsoft.framework.jremote.io.impl.rmi;

import org.pcsoft.framework.jremote.io.api.ServiceBase;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public final class RmiService extends ServiceBase {
    private Registry registry;

    @Override
    public void open(String host, int port) throws IOException {
        if (getServiceImplementation() == null)
            throw new IllegalStateException("Service not ready yet: no service implementation was set");
        if (!(getServiceImplementation() instanceof Remote))
            throw new IllegalStateException("Needed remote interface is not implemented: " + getServiceImplementation().getClass().getName());

        registry = LocateRegistry.getRegistry();
        registry.rebind(getServiceImplementation().getClass().getName(), UnicastRemoteObject.exportObject((Remote) getServiceImplementation(), port));
    }

    @Override
    public void close() throws IOException {
        try {
            registry.unbind(getServiceImplementation().getClass().getName());
            registry = null;
        } catch (NotBoundException e) {
            throw new IOException("Exception while unbind RMI service", e);
        }
    }
}
