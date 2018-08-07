package org.pcsoft.framework.jremote.io.impl.rmi;

import org.pcsoft.framework.jremote.io.api.ClientBase;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public final class RmiClient extends ClientBase {
    private Object stub = null;

    @Override
    public void open(String host, int port) throws IOException {
        final Registry registry = LocateRegistry.getRegistry(host, port);
        try {
            stub = registry.lookup(getServiceClass().getName());
        } catch (NotBoundException e) {
            throw new IOException("No service for class " + getServiceClass().getName() + " are bound on " + host + ":" + port, e);
        }
    }

    @Override
    public void close() throws IOException {
        //Empty
    }

    @Override
    public Object invokeRemote(Method method, Object... params) throws IOException {
        try {
            return method.invoke(stub, params);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Unable to access method", e);
        } catch (InvocationTargetException e) {
            throw new IOException("Error while call method " + method.getDeclaringClass().getName() + "#" + method.getName(), e);
        }
    }
}
