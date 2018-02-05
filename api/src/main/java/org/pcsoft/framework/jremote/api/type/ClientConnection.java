package org.pcsoft.framework.jremote.api.type;

import java.io.Closeable;
import java.io.IOException;
import java.net.URI;

public abstract class ClientConnection implements Closeable {
    private final URI uri;

    public ClientConnection(URI uri) {
        this.uri = uri;
    }

    public URI getUri() {
        return uri;
    }

    public abstract void open() throws IOException;

    public abstract boolean isOpen();
}
