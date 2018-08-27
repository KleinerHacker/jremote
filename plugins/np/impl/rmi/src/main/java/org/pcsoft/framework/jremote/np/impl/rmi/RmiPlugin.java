package org.pcsoft.framework.jremote.np.impl.rmi;

import org.pcsoft.framework.jremote.np.api.Client;
import org.pcsoft.framework.jremote.np.api.Service;
import org.pcsoft.framework.jremote.np.api.NetworkProtocolPlugin;
import org.pcsoft.framework.jremote.np.impl.rmi.interf.RmiKeepAliveService;
import org.pcsoft.framework.jremote.np.impl.rmi.interf.RmiRegistrationService;

public final class RmiPlugin implements NetworkProtocolPlugin {
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
