package org.pcsoft.framework.jremote.api.factory;

import org.pcsoft.framework.jremote.api.interf.RemoteRestService;
import org.pcsoft.framework.jremote.api.type.ClientConnection;

import java.net.URI;

public interface ClientFactory {
    ClientConnection createClientConnection(final URI uri);
    <T extends RemoteRestService>T createClient(Class<T> clazz, URI remoteUri, ClientConnection connection);
}
