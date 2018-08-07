package org.pcsoft.framework.jremote.io.impl.tcp;

import org.pcsoft.framework.jremote.io.api.Client;
import org.pcsoft.framework.jremote.io.api.IoPlugin;
import org.pcsoft.framework.jremote.io.api.Service;

public final class TcpPlugin implements IoPlugin {
    @Override
    public Class<? extends Service> getServiceClass() {
        return TcpService.class;
    }

    @Override
    public Class<? extends Client> getClientClass() {
        return TcpClient.class;
    }
}
