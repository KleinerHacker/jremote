package org.pcsoft.framework.jremote.core.internal.processor;

import org.pcsoft.framework.jremote.core.ConnectionState;
import org.pcsoft.framework.jremote.core.RemoteClient;
import org.pcsoft.framework.jremote.core.internal.type.wrapper.RemoteKeepAliveClientWrapper;
import org.pcsoft.framework.jremote.core.internal.type.wrapper.RemoteRegistrationClientWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public final class KeepAliveProcessor implements Processor {
    private static final Logger LOGGER = LoggerFactory.getLogger(KeepAliveProcessor.class);
    private static final ThreadFactory THREAD_FACTORY = r -> {
        final Thread thread = new Thread(r, "JRemote Keep Alive Thread");
        thread.setDaemon(true);

        return thread;
    };

    private static final int DEF_KEEP_ALIVE_DELAY = 1000;
    private static final double KEEP_ALIVE_SHUTDOWN_TIMEOUT_FACTOR = 2.5d;

    private final AtomicInteger keepAliveDelay = new AtomicInteger(DEF_KEEP_ALIVE_DELAY);
    private final AtomicBoolean keepAliveCanceled = new AtomicBoolean(false), keepAliveRunning = new AtomicBoolean(false);
    private final AtomicReference<ConnectionState> connectionState = new AtomicReference<>(ConnectionState.Unknown);
    private final List<Consumer<ConnectionState>> stateChangedListenerList = new ArrayList<>();

    private RemoteRegistrationClientWrapper registrationClientWrapper;
    private RemoteKeepAliveClientWrapper keepAliveClientWrapper;
    private RemoteClient remoteClient;

    private ExecutorService keepAliveExecutor = null;

    public int getKeepAliveDelay() {
        return keepAliveDelay.get();
    }

    public void setKeepAliveDelay(int keepAliveDelay) {
        this.keepAliveDelay.set(keepAliveDelay);
    }

    public ConnectionState getConnectionState() {
        return connectionState.get();
    }

    public void addStateChangeListener(Consumer<ConnectionState> l) {
        stateChangedListenerList.add(l);
    }

    public void removeStateChangeListener(Consumer<ConnectionState> l) {
        stateChangedListenerList.remove(l);
    }

    public void setup(RemoteRegistrationClientWrapper registrationClientWrapper, RemoteKeepAliveClientWrapper keepAliveClientWrapper, RemoteClient remoteClient) {
        if (keepAliveRunning.get())
            throw new IllegalStateException("Unable to call setup while processor is running");

        this.registrationClientWrapper = registrationClientWrapper;
        this.keepAliveClientWrapper = keepAliveClientWrapper;
        this.remoteClient = remoteClient;
    }

    @Override
    public void start() {
        if (keepAliveRunning.get())
            throw new IllegalStateException("Unable to start a running processor");

        keepAliveCanceled.set(false);
        keepAliveExecutor = Executors.newCachedThreadPool(THREAD_FACTORY);
        keepAliveExecutor.submit(() -> {
            keepAliveRunning.set(true);
            try {
                while (!keepAliveCanceled.get()) {
                    try {
                        runKeepAlive();
                    } catch (Exception e) {
                        LOGGER.error("Unknown exception while running keep alive, thread is continued", e);
                    }
                }
            } catch (Throwable e) {
                LOGGER.error("Unknown error while running keep alive thread, stop running now", e);
            } finally {
                keepAliveRunning.set(false);
                keepAliveCanceled.set(false);
            }
        });
    }

    @Override
    public void stop() {
        if (!keepAliveRunning.get())
            throw new IllegalStateException("Unable to stop a not running processor");
        if (keepAliveCanceled.get()) {
            LOGGER.warn("Processor already in stop mode");
            return;
        }

        keepAliveExecutor.shutdown();
        try {
            keepAliveExecutor.awaitTermination((int) (keepAliveDelay.get() * KEEP_ALIVE_SHUTDOWN_TIMEOUT_FACTOR), TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            LOGGER.warn("Keep alive thread could not finished after ");
        }
        keepAliveExecutor = null;
    }

    @Override
    public boolean isRunning() {
        return keepAliveRunning.get();
    }

    private void runKeepAlive() {
        try {
            final boolean known = keepAliveClientWrapper.ping(remoteClient.getUuid().toString());
            if (!known) {
                registrationClientWrapper.register(remoteClient.getUuid().toString(), remoteClient.getHost(), remoteClient.getPort());
            }

            if (connectionState.get() != ConnectionState.Connected) {
                LOGGER.info("Connect client to server " + remoteClient.getHost() + ":" + remoteClient.getPort());
                connectionState.set(ConnectionState.Connected);
                fireStateChange(ConnectionState.Connected);
            }
        } catch (Exception e) {
            if (connectionState.get() == ConnectionState.Connected) {
                logWarnOptionalException("Unexpected disconnection from server", e);
                connectionState.set(ConnectionState.Disconnected);
                fireStateChange(ConnectionState.Disconnected);
            } else {
                logWarnOptionalException("Unable to connect to server " + remoteClient.getHost() + ":" + remoteClient.getPort() + ", try again...", e);
                if (connectionState.get() != ConnectionState.Disconnected) {
                    connectionState.set(ConnectionState.Disconnected);
                    fireStateChange(ConnectionState.Disconnected);
                }
            }
        } finally {
            try {
                Thread.sleep(keepAliveDelay.get());
            } catch (InterruptedException ignore) {
            }
        }
    }

    private void fireStateChange(final ConnectionState state) {
        for (final Consumer<ConnectionState> l : stateChangedListenerList) {
            l.accept(state);
        }
    }

    private static void logWarnOptionalException(String msg, Exception e) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.warn(msg, e);
        } else {
            LOGGER.warn(msg);
        }
    }
}
