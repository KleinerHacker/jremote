package org.pcsoft.framework.jremote.core;

import org.pcsoft.framework.jremote.ext.np.api.NetworkProtocol;
import org.pcsoft.framework.jremote.ext.np.impl.rmi.RmiProtocol;
import org.pcsoft.framework.jremote.ext.up.api.UpdatePolicy;
import org.pcsoft.framework.jremote.ext.up.impl.def.DefaultUpdatePolicy;

/**
 * Configuration for extensions to use. Create it with {@link ExtensionConfigurationBuilder}.<br/>
 * Default values are used:<br/>
 * <ul>
 *     <li><b>Network Protocol</b> - RMI ({@link RmiProtocol})</li>
 *     <li><b>Update Policy</b> - Default (direct call) ({@link DefaultUpdatePolicy}</li>
 * </ul>
 */
public final class ExtensionConfiguration {
    private Class<? extends NetworkProtocol> networkProtocolClass = RmiProtocol.class;
    private Class<? extends UpdatePolicy> updatePolicyClass = DefaultUpdatePolicy.class;

    ExtensionConfiguration() {
    }

    /**
     * Returns the network protocol class (np), default is {@link RmiProtocol}
     * @return
     */
    public Class<? extends NetworkProtocol> getNetworkProtocolClass() {
        return networkProtocolClass;
    }

    void setNetworkProtocolClass(Class<? extends NetworkProtocol> networkProtocolClass) {
        this.networkProtocolClass = networkProtocolClass;
    }

    /**
     * Returns the update policy class (up), default is {@link DefaultUpdatePolicy}
     * @return
     */
    public Class<? extends UpdatePolicy> getUpdatePolicyClass() {
        return updatePolicyClass;
    }

    void setUpdatePolicyClass(Class<? extends UpdatePolicy> updatePolicyClass) {
        this.updatePolicyClass = updatePolicyClass;
    }
}
