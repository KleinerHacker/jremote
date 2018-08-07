package org.pcsoft.framework.jremote.io.impl.tcp;

import org.pcsoft.framework.jremote.io.api.Client;
import org.pcsoft.framework.jremote.io.api.ServiceClientPlugin;
import org.pcsoft.framework.jremote.io.api.Service;

public final class TcpPlugin implements ServiceClientPlugin {
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
