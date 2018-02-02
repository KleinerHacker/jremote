package org.pcsoft.framework.jremote.api.internal;

import org.pcsoft.framework.jremote.api.interf.RemoteRestClient;
import org.pcsoft.framework.jremote.api.interf.RemoteRestService;

public interface ClientProxyFactory {
    <T extends RemoteRestService>T createProxy(Class<T> clazz);
    <T extends RemoteRestService>RemoteRestClient<T> createClient(Class<T> clazz);
}
