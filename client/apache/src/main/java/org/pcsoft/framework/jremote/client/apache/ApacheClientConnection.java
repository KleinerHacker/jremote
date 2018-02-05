package org.pcsoft.framework.jremote.client.apache;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.pcsoft.framework.jremote.api.type.ClientConnection;

import java.io.IOException;
import java.net.URI;

public final class ApacheClientConnection extends ClientConnection {
    private CloseableHttpClient client = null;

    public ApacheClientConnection(URI uri) {
        super(uri);
    }

    @Override
    public void open() throws IOException {
        if (isOpen())
            close();

        client = HttpClientBuilder.create().build();
    }

    @Override
    public boolean isOpen() {
        return client != null;
    }

    @Override
    public void close() throws IOException {
        client.close();
        client = null;
    }

    public CloseableHttpResponse execute(HttpRequestBase requestBase) throws IOException {
        if (!isOpen())
            throw new IllegalStateException("client not connected");

        return client.execute(requestBase);
    }
}
