package org.pcsoft.framework.jremote.server.jersey;

import org.pcsoft.framework.jremote.api.*;
import org.pcsoft.framework.jremote.api.builder.RemoteServerBuilder;
import org.pcsoft.framework.jremote.api.interf.RemoteRestService;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class JerseyRemoteServerBuilder implements RemoteServerBuilder<JerseyRemoteServerBuilder> {
    public static JerseyRemoteServerBuilder create(URI uri) {
        return new JerseyRemoteServerBuilder(uri);
    }

    private final URI uri;
    private final List<Class<? extends RemoteRestService>> classes = new ArrayList<>();

    private JerseyRemoteServerBuilder(URI uri) {
        this.uri = uri;
    }

    @Override
    public JerseyRemoteServerBuilder withRestService(Class<? extends RemoteRestService> restServiceClass) {
        classes.add(restServiceClass);
        return this;
    }

    @Override
    public RemoteServer build() {
        return new JerseyRemoteServer(uri, classes.toArray(new Class[classes.size()]));
    }
}
