package org.pcsoft.framework.jremote.np.impl.tcp;

import org.pcsoft.framework.jremote.np.api.Client;
import org.pcsoft.framework.jremote.np.api.NetworkProtocol;
import org.pcsoft.framework.jremote.np.api.Service;
import org.pcsoft.framework.jremote.np.impl.tcp.internal.TcpClient;
import org.pcsoft.framework.jremote.np.impl.tcp.internal.TcpService;

public final class TcpProtocol implements NetworkProtocol {
    @Override
    public Service createService(Object serviceImplementation) {
        return new TcpService(serviceImplementation);
    }

    @Override
    public Client createClient(Class<?> serviceClass) {
        return new TcpClient(serviceClass);
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
