package org.pcsoft.framework.jremote.api;

import org.pcsoft.framework.jremote.api.interf.RemoteRestClient;
import org.pcsoft.framework.jremote.api.interf.RemoteRestService;

import java.io.Closeable;

public interface RemoteBase extends Closeable {
    void open();

    RemoteRestClient<? extends RemoteRestService> getClient(Class<? extends RemoteRestService> clazz);
}
