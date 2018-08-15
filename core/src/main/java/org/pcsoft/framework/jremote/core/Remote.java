package org.pcsoft.framework.jremote.core;

import java.io.IOException;
import java.util.function.Consumer;

public interface Remote<S extends State> extends AutoCloseable {
    void open() throws IOException;

    S getState();
    void addStateChangeListener(Consumer<S> l);
    void removeStateChangeListener(Consumer<S> l);

    String getHost();
    int getPort();
}
