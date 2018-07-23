package org.pcsoft.framework.jremote.core;

import java.io.Closeable;

public interface Remote extends AutoCloseable {
    void open();
}
