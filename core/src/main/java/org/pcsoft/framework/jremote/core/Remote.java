package org.pcsoft.framework.jremote.core;

import java.io.IOException;
import java.util.function.Consumer;

public interface Remote extends AutoCloseable {
    void open() throws IOException;

    ConnectionState getState();
    void addStateChangeListener(Consumer<ConnectionState> l);
    void removeStateChangeListener(Consumer<ConnectionState> l);

    String getHost();
    int getPort();
}
