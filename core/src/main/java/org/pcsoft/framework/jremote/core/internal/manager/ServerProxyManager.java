package org.pcsoft.framework.jremote.core.internal.manager;

import org.pcsoft.framework.jremote.core.internal.proxy.ProxyFactory;
import org.pcsoft.framework.jremote.core.internal.registry.ClientRegistry;

public final class ServerProxyManager {
    private Object registrationServiceProxy;

    //Data
    private final ClientRegistry clientRegistry = new ClientRegistry();

    //region Registration Service Proxy
    public <T> void setRemoteRegistrationServiceProxy(Class<T> clazz) {
        registrationServiceProxy = ProxyFactory.buildRemoteRegistrationServiceProxy(clazz, clientRegistry);
    }
    //endregion
}
