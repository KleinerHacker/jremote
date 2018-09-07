package org.pcsoft.framework.jremote.ext.np.impl.rmi;

import org.pcsoft.framework.jremote.ext.np.api.Client;
import org.pcsoft.framework.jremote.ext.np.api.NetworkProtocol;
import org.pcsoft.framework.jremote.ext.np.api.Service;
import org.pcsoft.framework.jremote.ext.np.impl.rmi.internal.RmiClient;
import org.pcsoft.framework.jremote.ext.np.impl.rmi.internal.RmiService;
import org.pcsoft.framework.jremote.ext.np.impl.rmi.internal.interf.RmiKeepAliveService;
import org.pcsoft.framework.jremote.ext.np.impl.rmi.internal.interf.RmiRegistrationService;

/**
 * Represent the RMI implementation for a network protocol extension.
 */
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
