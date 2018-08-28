package org.pcsoft.framework.jremote.np.impl.rmi;

import org.pcsoft.framework.jremote.np.api.Client;
import org.pcsoft.framework.jremote.np.api.NetworkProtocol;
import org.pcsoft.framework.jremote.np.api.Service;
import org.pcsoft.framework.jremote.np.impl.rmi.internal.RmiClient;
import org.pcsoft.framework.jremote.np.impl.rmi.internal.RmiService;
import org.pcsoft.framework.jremote.np.impl.rmi.internal.interf.RmiKeepAliveService;
import org.pcsoft.framework.jremote.np.impl.rmi.internal.interf.RmiRegistrationService;

public final class RmiProtocol implements NetworkProtocol {
    @Override
    public Service createService(Object serviceImplementation) {
        return new RmiService(serviceImplementation);
    }

    @Override
    public Client createClient(Class<?> serviceClass) {
        return new RmiClient(serviceClass);
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
