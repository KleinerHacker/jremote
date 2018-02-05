package org.pcsoft.framework.jremote.api.factory;

import com.sun.net.httpserver.HttpServer;
import org.pcsoft.framework.jremote.api.interf.RemoteRestService;

import java.net.URI;

public interface ServerFactory {
    HttpServer createRestServer(URI uri, Class<? extends RemoteRestService>[] classes);
}
