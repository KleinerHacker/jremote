package org.pcsoft.framework.jremote.core;

public interface RemoteBuilder<T extends Remote> {
    T build();
}
