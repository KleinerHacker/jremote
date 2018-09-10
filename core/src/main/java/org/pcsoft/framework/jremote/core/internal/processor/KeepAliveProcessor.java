package org.pcsoft.framework.jremote.core.internal.processor;

import org.pcsoft.framework.jremote.core.ClientState;
import org.pcsoft.framework.jremote.core.RemoteClient;
import org.pcsoft.framework.jremote.core.internal.type.RemoteKeepAliveClientWrapper;
import org.pcsoft.framework.jremote.core.internal.type.RemoteRegistrationClientWrapper;
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
    private final AtomicReference<ClientState> connectionState = new AtomicReference<>(ClientState.Unknown);
    private final List<Consumer<ClientState>> stateChangedListenerList = new ArrayList<>();

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

    public ClientState getConnectionState() {
        return connectionState.get();
    }

    public void addStateChangeListener(Consumer<ClientState> l) {
        stateChangedListenerList.add(l);
    }

    public void removeStateChangeListener(Consumer<ClientState> l) {
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

        final int timeout = (int) (keepAliveDelay.get() * KEEP_ALIVE_SHUTDOWN_TIMEOUT_FACTOR);
        keepAliveCanceled.set(true);
        keepAliveExecutor.shutdown();
        try {
            final boolean terminated = keepAliveExecutor.awaitTermination(timeout, TimeUnit.MILLISECONDS);
            if (!terminated) {
                LOGGER.warn("Keep alive thread could not finished after " + timeout + " ms");
            }
        } catch (InterruptedException ignored) {
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
                registrationClientWrapper.register(remoteClient.getUuid().toString(), remoteClient.getHost(), remoteClient.getOwnPort());
            }

            if (connectionState.get() != ClientState.Connected) {
                LOGGER.info("Connect client to server " + remoteClient.getHost() + ":" + remoteClient.getPort());
                connectionState.set(ClientState.Connected);
                fireStateChange();
            }
        } catch (Exception e) {
            if (connectionState.get() == ClientState.Connected) {
                logWarnOptionalException("Unexpected disconnection from server", e);
                connectionState.set(ClientState.Disconnected);
                fireStateChange();
            } else {
                logWarnOptionalException("Unable to connect to server " + remoteClient.getHost() + ":" + remoteClient.getPort() + ", try again...", e);
                if (connectionState.get() != ClientState.Disconnected) {
                    connectionState.set(ClientState.Disconnected);
                    fireStateChange();
                }
            }
        } finally {
            try {
                Thread.sleep(keepAliveDelay.get());
            } catch (InterruptedException ignore) {
            }
        }
    }

    private void fireStateChange() {
        for (final Consumer<ClientState> l : stateChangedListenerList) {
            l.accept(connectionState.get());
        }
    }

    private static void logWarnOptionalException(String msg, Exception e) {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.warn(msg, e);
        } else {
            LOGGER.warn(msg);
        }
    }
}
