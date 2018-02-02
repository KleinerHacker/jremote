package org.pcsoft.framework.jremote.client.jersey;

import org.pcsoft.framework.jremote.api.RemoteClient;
import org.pcsoft.framework.jremote.api.builder.RemoteClientBuilder;
import org.pcsoft.framework.jremote.api.interf.RemoteRestService;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class JerseyRemoteClientBuilder implements RemoteClientBuilder<JerseyRemoteClientBuilder> {
    public static JerseyRemoteClientBuilder create(URI uri) {
        return new JerseyRemoteClientBuilder(uri);
    }

    private final URI uri;
    private final List<Class<? extends RemoteRestService>> classes = new ArrayList<>();

    private JerseyRemoteClientBuilder(URI uri) {
        this.uri = uri;
    }

    @Override
    public JerseyRemoteClientBuilder withRestService(Class<? extends RemoteRestService> restServiceClass) {
        classes.add(restServiceClass);
        return this;
    }

    @Override
    public RemoteClient build() {
        return new JerseyRemoteClient(uri, classes.toArray(new Class[classes.size()]));
    }
}
