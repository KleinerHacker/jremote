package org.pcsoft.framework.jremote.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Represent the base implementation for the remote interface. Contains always used members like host and port.
 *
 * @param <S>
 */
public abstract class RemoteBase<S extends State> implements Remote<S> {
    protected final String host;
    protected final int port;

    private LifecycleState lifecycleState = LifecycleState.Created;
    private final List<Consumer<LifecycleState>> lifecycleStateChangeListenerList = new ArrayList<>();

    RemoteBase(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public final void open() throws IOException {
        if (lifecycleState != LifecycleState.Created)
            throw new IllegalStateException(String.format("Unable to open: lifecycle state must be <%s> but is %s",
                    LifecycleState.Created, lifecycleState));

        doOpen();
        lifecycleState = LifecycleState.Opened;
        fireLifecycleStateChanged();
    }

    @Override
    public final void close() throws Exception {
        if (lifecycleState != LifecycleState.Opened)
            throw new IllegalStateException(String.format("Unable to close: lifecycle state must be <%s> but is %s",
                    LifecycleState.Opened, lifecycleState));

        doClose();
        lifecycleState = LifecycleState.Closed;
        fireLifecycleStateChanged();
    }

    @Override
    public final LifecycleState getLifecycleState() {
        return lifecycleState;
    }

    @Override
    public final void addLifecycleStateChangeListener(Consumer<LifecycleState> l) {
        lifecycleStateChangeListenerList.add(l);
    }

    @Override
    public final void removeLifecycleStateChangeListener(Consumer<LifecycleState> l) {
        lifecycleStateChangeListenerList.remove(l);
    }

    private void fireLifecycleStateChanged() {
        for (final Consumer<LifecycleState> l : lifecycleStateChangeListenerList) {
            l.accept(lifecycleState);
        }
    }

    protected abstract void doOpen() throws IOException;

    protected abstract void doClose() throws Exception;
}
