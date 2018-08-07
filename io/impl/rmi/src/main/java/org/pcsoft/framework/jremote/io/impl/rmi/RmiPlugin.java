package org.pcsoft.framework.jremote.io.impl.rmi;

import org.pcsoft.framework.jremote.io.api.Client;
import org.pcsoft.framework.jremote.io.api.ServiceClientPlugin;
import org.pcsoft.framework.jremote.io.api.Service;
import org.pcsoft.framework.jremote.io.impl.rmi.interf.RmiKeepAliveService;
import org.pcsoft.framework.jremote.io.impl.rmi.interf.RmiRegistrationService;

public final class RmiPlugin implements ServiceClientPlugin {
    @Override
    public Class<? extends Service> getServiceClass() {
        return RmiService.class;
    }

    @Override
    public Class<? extends Client> getClientClass() {
        return RmiClient.class;
    }

    @Override
    public Class<?> getRegistrationServiceClass() {
        return RmiRegistrationService.class;
    }

    @Override
    public Class<?> getKeepAliveServiceClass() {
        return RmiKeepAliveService.class;
    }
}
