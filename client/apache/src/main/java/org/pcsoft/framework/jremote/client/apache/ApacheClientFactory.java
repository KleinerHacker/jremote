package org.pcsoft.framework.jremote.client.apache;

import org.apache.http.client.methods.*;
import org.apache.http.entity.ByteArrayEntity;
import org.pcsoft.framework.jremote.api.exception.JRemoteCommunicationException;
import org.pcsoft.framework.jremote.api.exception.JRemoteException;
import org.pcsoft.framework.jremote.api.exception.JRemoteReceiverFailureException;
import org.pcsoft.framework.jremote.api.exception.JRemoteServiceInterfaceException;
import org.pcsoft.framework.jremote.api.type.ClientConnection;
import org.pcsoft.framework.jremote.api.type.RestHeader;
import org.pcsoft.framework.jremote.commons.ClientFactoryBase;

import java.io.IOException;
import java.net.URI;

public final class ApacheClientFactory extends ClientFactoryBase {
    private static final ApacheClientFactory INSTANCE = new ApacheClientFactory();

    public static ApacheClientFactory getInstance() {
        return INSTANCE;
    }

    private ApacheClientFactory() {
    }

    @Override
    public ClientConnection createClientConnection(URI uri) {
        return new ApacheClientConnection(uri);
    }

    @Override
    protected byte[] callService(String url, RestMethod method, RestHeader[] headers, byte[] entity, ClientConnection connection) throws JRemoteException {
        final HttpRequestBase requestBase = createRequestBase(url, method, headers, entity);

        final CloseableHttpResponse response;
        try {
            response = ((ApacheClientConnection) connection).execute(requestBase);
        } catch (IOException e) {
            throw new JRemoteCommunicationException("Unable to execute HTTP request", e);
        }

        if (response.getStatusLine().getStatusCode() / 100 != 2) {
            throw new JRemoteReceiverFailureException(response.getStatusLine().getReasonPhrase(), response.getStatusLine().getStatusCode());
        }

        try {
            if (response.getEntity() != null)
                return org.apache.commons.io.IOUtils.toByteArray(response.getEntity().getContent());
        } catch (IOException e) {
            throw new JRemoteCommunicationException("Unable to read entity content", e);
        }

        return null;
    }

    private static HttpRequestBase createRequestBase(String url, RestMethod restMethod, RestHeader[] headers, byte[] entity) throws JRemoteServiceInterfaceException {
        final HttpRequestBase requestBase;
        switch (restMethod) {
            case Get:
                if (entity != null)
                    throw new JRemoteServiceInterfaceException("Unable to send content with method " + restMethod.name());
                requestBase = new HttpGet(url);
                break;
            case Post:
                requestBase = new HttpPost(url);
                if (entity != null) {
                    ((HttpPost) requestBase).setEntity(new ByteArrayEntity(entity));
                }
                break;
            case Put:
                requestBase = new HttpPut(url);
                if (entity != null) {
                    ((HttpPut) requestBase).setEntity(new ByteArrayEntity(entity));
                }
                break;
            case Delete:
                if (entity != null)
                    throw new JRemoteServiceInterfaceException("Unable to send content with method " + restMethod.name());
                requestBase = new HttpDelete(url);
                break;
            case Head:
                if (entity != null)
                    throw new JRemoteServiceInterfaceException("Unable to send content with method " + restMethod.name());
                requestBase = new HttpHead(url);
                break;
            case Options:
                if (entity != null)
                    throw new JRemoteServiceInterfaceException("Unable to send content with method " + restMethod.name());
                requestBase = new HttpOptions(url);
                break;
            default:
                throw new JRemoteServiceInterfaceException("Unknown HTTP method: " + restMethod.name());
        }

        for (final RestHeader header : headers) {
            requestBase.addHeader(header.getName(), header.getValue());
        }

        return requestBase;
    }
}
