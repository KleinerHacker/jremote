package org.pcsoft.framework.jremote.api.interf;

public interface RemoteRestClient<T extends RemoteRestService> {
    T get();
}
