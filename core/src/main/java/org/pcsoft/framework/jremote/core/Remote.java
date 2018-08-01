package org.pcsoft.framework.jremote.core;

import java.io.IOException;

public interface Remote extends AutoCloseable {
    void open() throws IOException;

    String getHost();
    int getPort();
}
