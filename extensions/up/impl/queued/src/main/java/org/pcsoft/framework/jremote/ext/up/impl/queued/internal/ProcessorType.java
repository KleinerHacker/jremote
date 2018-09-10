package org.pcsoft.framework.jremote.ext.up.impl.queued.internal;

public enum ProcessorType {
    ModelObserver(
            "Model/Observer",
            InvocationConfiguration.getInstance().getModelInvocationCount(),
            InvocationConfiguration.getInstance().getModelInvocationDelay(),
            InvocationConfiguration.getInstance().getModelInvocationBufferSize()
    ),
    Event(
            "Event",
            InvocationConfiguration.getInstance().getEventInvocationCount(),
            InvocationConfiguration.getInstance().getEventInvocationDelay(),
            InvocationConfiguration.getInstance().getEventInvocationBufferSize()
    ),
    ;

    private final String debugName;
    private final int count, delay, bufferSize;

    ProcessorType(String debugName, int count, int delay, int bufferSize) {
        this.debugName = debugName;
        this.count = count;
        this.delay = delay;
        this.bufferSize = bufferSize;
    }

    public String getDebugName() {
        return debugName;
    }

    public int getCount() {
        return count;
    }

    public int getDelay() {
        return delay;
    }

    public int getBufferSize() {
        return bufferSize;
    }
}