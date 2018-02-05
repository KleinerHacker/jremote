package org.pcsoft.framework.jremote.server.jersey;

import com.sun.net.httpserver.HttpServer;
import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.pcsoft.framework.jremote.api.factory.ServerFactory;
import org.pcsoft.framework.jremote.api.interf.RemoteRestService;

import java.net.URI;

public final class JerseyServerFactory implements ServerFactory {
    @Override
    public HttpServer createRestServer(URI uri, Class<? extends RemoteRestService>[] classes) {
        return JdkHttpServerFactory.createHttpServer(uri, new ResourceConfig(classes));
    }
}
