package org.pcsoft.framework.jremote.tutorial.type;

import java.util.concurrent.atomic.AtomicInteger;

public final class GreetingManager {
    private static final AtomicInteger COUNTER = new AtomicInteger(0);

    public static int addGreeting() {
        return COUNTER.incrementAndGet();
    }

    public static int getGreetings() {
        return COUNTER.get();
    }

    private GreetingManager() {
    }
}
