package org.pcsoft.framework.jremote.sc.impl.rmi;

import org.pcsoft.framework.jremote.sc.api.ServiceBase;

import java.io.IOException;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public final class RmiService extends ServiceBase {
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
        try {
            exportObject = UnicastRemoteObject.exportObject((Remote) getServiceImplementation(), 0);
            registry.bind(getServiceImplementation().getClass().getName(), exportObject);
        } catch (AlreadyBoundException e) {
            throw new IOException("Already bind on address " + host + ":" + port, e);
        }
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
