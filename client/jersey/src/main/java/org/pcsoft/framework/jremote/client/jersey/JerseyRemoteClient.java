package org.pcsoft.framework.jremote.client.jersey;

import com.sun.net.httpserver.HttpServer;
import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.pcsoft.framework.jremote.api.RemoteClient;
import org.pcsoft.framework.jremote.api.interf.RemoteRestClient;
import org.pcsoft.framework.jremote.api.interf.RemoteRestService;
import org.pcsoft.framework.jremote.commons.internal.ClientProxyCache;
import org.pcsoft.framework.jremote.core.jersey.internal.JerseyClientProxyFactory;
import org.pcsoft.framework.jremote.core.jersey.internal.JerseyRemoteRestClient;

import java.io.IOException;
import java.net.URI;

class JerseyRemoteClient implements RemoteClient {
    private final HttpServer httpServer;
    private final ClientProxyCache clientProxyCache = new ClientProxyCache(JerseyClientProxyFactory.getInstance());

    @SafeVarargs
    JerseyRemoteClient(URI uri, Class<? extends RemoteRestService>... serverImplClasses) {
        httpServer = JdkHttpServerFactory.createHttpServer(uri, new ResourceConfig(serverImplClasses), false);
    }

    @Override
    public void open() {
        httpServer.start();
    }

    @Override
    public void close() throws IOException {
        httpServer.stop(0);
    }

    @Override
    public RemoteRestClient<? extends RemoteRestService> getClient(Class<? extends RemoteRestService> clazz) {
        return clientProxyCache.get(clazz);
    }
}
