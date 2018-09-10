package org.pcsoft.framework.jremote.ext.up.impl.queued.internal;

import org.pcsoft.framework.jremote.commons.type.DaemonThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Represent a processor to invoke queued runnables
 */
public final class InvocationProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(InvocationProcessor.class);

    private final ExecutorService executorService;
    private final SortedMap<UpdateKey, Runnable> queueMap = new TreeMap<>();
    private final AtomicBoolean stopInvocation = new AtomicBoolean(false);
    private final AtomicBoolean started = new AtomicBoolean(false);

    private final ProcessorType type;
    private final String debugName;

    public InvocationProcessor(ProcessorType type) {
        this.type = type;
        debugName = "Invocation Processor [" + type.getDebugName() + "]";
        executorService = Executors.newSingleThreadExecutor(new DaemonThreadFactory(debugName));
    }

    public void start() {
        if (isStarted())
            throw new IllegalStateException("Invocation processor already started");

        stopInvocation.set(false);

        started.set(true);
        executorService.submit(this::runInvocation);
    }

    public void stop() {
        if (!isStarted())
            throw new IllegalStateException("Invocation processor already stopped");

        stopInvocation.set(true);
    }

    public boolean isStarted() {
        return started.get();
    }

    public void addInvocation(UpdateKey updateKey, Runnable runnable) {
        synchronized (queueMap) {
            queueMap.put(updateKey, runnable);
            if (type.getBufferSize() > 0) {
                if (queueMap.size() > type.getBufferSize()) {
                    queueMap.remove(queueMap.firstKey());
                    LOGGER.warn(debugName + " - Queue Buffer Size of " + type.getBufferSize() + " is overflowed, remove oldest element!");
                }
            }
        }
    }

    public ProcessorType getType() {
        return type;
    }

    private void runInvocation() {
        try {
            while (!stopInvocation.get()) {
                InvocationLogic.runInvocation(queueMap, type);
                Thread.sleep(type.getDelay());
            }
        } catch (Throwable e) {
            LOGGER.error("Unexpected exception in " + debugName, e);
        } finally {
            started.set(false);
        }
    }


}
