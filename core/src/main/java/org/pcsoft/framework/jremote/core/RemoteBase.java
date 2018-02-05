package org.pcsoft.framework.jremote.core;

import com.sun.net.httpserver.HttpServer;
import org.pcsoft.framework.jremote.api.factory.ClientFactory;
import org.pcsoft.framework.jremote.api.factory.ServerFactory;
import org.pcsoft.framework.jremote.api.interf.RemoteRestService;

import java.io.Closeable;
import java.io.IOException;
import java.net.URI;

public abstract class RemoteBase implements Closeable {
    protected final ServerFactory serverFactory;
    protected final ClientFactory clientFactory;

    protected final HttpServer httpServer;

    protected RemoteBase(final ServerFactory serverFactory, final ClientFactory clientFactory, final URI uri, final Class<? extends RemoteRestService>[] restServiceClasses) {
        this.serverFactory = serverFactory;
        this.clientFactory = clientFactory;

        httpServer = serverFactory.createRestServer(uri, restServiceClasses);
    }

    public final void open() {
        httpServer.start();
        onOpen();
    }

    @Override
    public final void close() throws IOException {
        onClose();
        httpServer.stop(0);
    }

    protected void onOpen() {

    }

    protected void onClose() {

    }
}
