package org.pcsoft.framework.jremote.core.internal.manager;

import org.pcsoft.framework.jremote.core.internal.proxy.ProxyFactory;
import org.pcsoft.framework.jremote.core.internal.registry.ClientRegistry;

public final class ServerProxyManager {
    private Object registrationServiceProxy;
    private Object keepAliveServiceProxy;

    //Data
    private final ClientRegistry clientRegistry = new ClientRegistry();

    public <T> void setRemoteRegistrationServiceProxy(Class<T> clazz) {
        registrationServiceProxy = ProxyFactory.buildRemoteRegistrationServiceProxy(clazz, clientRegistry);
    }

    public <T> void setRemoteKeepAliveServiceProxy(Class<T> clazz) {
        registrationServiceProxy = ProxyFactory.buildRemoteKeepAliveServiceProxy(clazz, clientRegistry);
    }
}
