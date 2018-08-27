package org.pcsoft.framework.jremote.np.impl.tcp;

import org.pcsoft.framework.jremote.np.api.Client;
import org.pcsoft.framework.jremote.np.api.Service;
import org.pcsoft.framework.jremote.np.api.NetworkProtocolPlugin;

public final class TcpPlugin implements NetworkProtocolPlugin {
    @Override
    public Class<? extends Service> getServiceClass() {
        return TcpService.class;
    }

    @Override
    public Class<? extends Client> getClientClass() {
        return TcpClient.class;
    }

    @Override
    public Class<?> getRegistrationServiceClass() {
        return null; //TODO
    }

    @Override
    public Class<?> getKeepAliveServiceClass() {
        return null; //TODO
    }
}
