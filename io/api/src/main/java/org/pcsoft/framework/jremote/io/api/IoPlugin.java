package org.pcsoft.framework.jremote.io.api;

/**
 * Interface for IO plugin mechanism
 */
public interface IoPlugin {
    /**
     * Returns the class of {@link Service} for this plugin. Class is instantiate automatically.
     *
     * @return
     */
    Class<? extends Service> getServiceClass();

    /**
     * Returns the class of {@link Client} for this plugin. Class is instantiate automatically.
     *
     * @return
     */
    Class<? extends Client> getClientClass();
}
