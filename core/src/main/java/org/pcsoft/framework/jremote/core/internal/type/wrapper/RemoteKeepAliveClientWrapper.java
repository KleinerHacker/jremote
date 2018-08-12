package org.pcsoft.framework.jremote.core.internal.type.wrapper;

public final class RemoteKeepAliveClientWrapper extends ClientWrapper {
    public RemoteKeepAliveClientWrapper(Object clientProxy) {
        super(clientProxy);
    }

    public boolean ping(String uuid) {
        return false;
    }
}
