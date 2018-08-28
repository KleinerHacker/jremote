package org.pcsoft.framework.jremote.core;

import org.pcsoft.framework.jremote.ext.np.api.NetworkProtocol;

import java.io.IOException;
import java.util.function.Consumer;

/**
 * Remote interface for {@link RemoteServer} and {@link RemoteClient} with all common methods
 * @param <S>
 */
public interface Remote<S extends State> extends AutoCloseable {
    /**
     * Open the remote object
     * @throws IOException
     */
    void open() throws IOException;

    /**
     * Returns the current state of the remote object. This is an other state than {@link #getLifecycleState()}
     * @return
     */
    S getState();

    void addStateChangeListener(Consumer<S> l);

    void removeStateChangeListener(Consumer<S> l);

    /**
     * Returns the lifecycle state of the remote object
     * @return
     */
    LifecycleState getLifecycleState();

    void addLifecycleStateChangeListener(Consumer<LifecycleState> l);

    void removeLifecycleStateChangeListener(Consumer<LifecycleState> l);

    /**
     * Returns the configured host name of this remote object to use
     * @return
     */
    String getHost();

    /**
     * Returns the configured port of this remote object to use
     * @return
     */
    int getPort();

    /**
     * Returns the class of network protocol for this remote instance to use.
     * @return
     */
    Class<? extends NetworkProtocol> getNetworkProtocolClass();
}
