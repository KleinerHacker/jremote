package org.pcsoft.framework.jremote.io.impl.tcp;

import org.pcsoft.framework.jremote.io.api.ServiceBase;

import java.io.IOException;
import java.lang.reflect.Proxy;

public final class TcpService extends ServiceBase {

    @Override
    public void startService(String host, int port) throws IOException {

    }

    @Override
    public void stopService() throws IOException {

    }

    @Override
    public <T> void createProxyFor(Class<T> serviceClass) {
        Proxy.newProxyInstance(serviceClass.getClassLoader(), new Class[]{serviceClass}, (proxy, method, args) -> {
            return null;
        });
    }
}
