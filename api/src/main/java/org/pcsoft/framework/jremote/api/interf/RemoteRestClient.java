package org.pcsoft.framework.jremote.api.interf;

/**
 * Class for a remote REST client, based on the used {@link RemoteRestService}
 * @param <T> REST service to connect as client
 */
public final class RemoteRestClient<T extends RemoteRestService> {
    private final T proxyService;

    public RemoteRestClient(T proxyService) {
        this.proxyService = proxyService;
    }

    /**
     * Returns the generated proxy object for communication
     * @return
     */
    T get() {
        return proxyService;
    }
}
