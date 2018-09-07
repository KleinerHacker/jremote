package org.pcsoft.framework.jremote.core;

import org.pcsoft.framework.jremote.ext.np.api.NetworkProtocol;
import org.pcsoft.framework.jremote.ext.up.api.UpdatePolicy;

/**
 * Builder to create an {@link ExtensionConfiguration}.<br/>
 * This builder can create the configuration immediately. For default values see {@link ExtensionConfiguration}.
 */
public final class ExtensionConfigurationBuilder {
    /**
     * Creates a new instance of this builder
     * @return
     */
    public static ExtensionConfigurationBuilder create() {
        return new ExtensionConfigurationBuilder();
    }

    private final ExtensionConfiguration configuration = new ExtensionConfiguration();

    private ExtensionConfigurationBuilder() {
    }

    public ExtensionConfigurationBuilder withNetworkProtocol(Class<? extends NetworkProtocol> clazz) {
        configuration.setNetworkProtocolClass(clazz);
        return this;
    }

    public ExtensionConfigurationBuilder withUpdatePolicy(Class<? extends UpdatePolicy> clazz) {
        configuration.setUpdatePolicyClass(clazz);
        return this;
    }

    public ExtensionConfiguration build() {
        return configuration;
    }
}
