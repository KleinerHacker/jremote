package org.pcsoft.framework.jremote.sc.impl.tcp;

import org.pcsoft.framework.jremote.sc.api.Client;
import org.pcsoft.framework.jremote.sc.api.ServiceClientPlugin;
import org.pcsoft.framework.jremote.sc.api.Service;

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
