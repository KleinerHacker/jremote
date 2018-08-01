package org.pcsoft.framework.jremote.io.impl.rmi;

import org.pcsoft.framework.jremote.io.api.Client;
import org.pcsoft.framework.jremote.io.api.IoPlugin;
import org.pcsoft.framework.jremote.io.api.Service;

public final class RmiPlugin implements IoPlugin {
    @Override
    public Class<? extends Service> getServiceClass() {
        return RmiService.class;
    }

    @Override
    public Class<? extends Client> getClientClass() {
        return null;
    }
}
