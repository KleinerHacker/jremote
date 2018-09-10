package org.pcsoft.framework.jremote.commons.type;

import java.util.concurrent.ThreadFactory;

public final class DaemonThreadFactory implements ThreadFactory {
    private final String name;

    public DaemonThreadFactory() {
        this("");
    }

    public DaemonThreadFactory(String name) {
        this.name = name;
    }

    @Override
    public Thread newThread(Runnable r) {
        final Thread thread = new Thread(r);
        thread.setName(name);
        thread.setDaemon(true);

        return thread;
    }
}
