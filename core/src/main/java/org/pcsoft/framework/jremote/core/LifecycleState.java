package org.pcsoft.framework.jremote.core;

/**
 * Represent all lifecycle states of {@link RemoteServer} and {@link RemoteClient}
 */
public enum LifecycleState {
    /**
     * Instance was created / build, see {@link RemoteServerBuilder} or {@link RemoteClientBuilder}
     */
    Created,
    /**
     * Instance was opened via {@link Remote#open()}
     */
    Opened,
    /**
     * Instance was closed via {@link Remote#close()}
     */
    Closed
}
