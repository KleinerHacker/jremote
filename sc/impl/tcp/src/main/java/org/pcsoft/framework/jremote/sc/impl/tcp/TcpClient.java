package org.pcsoft.framework.jremote.sc.impl.tcp;

import org.pcsoft.framework.jremote.sc.api.ClientBase;

import java.io.IOException;
import java.lang.reflect.Method;

public final class TcpClient extends ClientBase {
    @Override
    public void open(String host, int port) throws IOException {

    }

    @Override
    public Object invokeRemote(Method method, Object... params) throws IOException {
        return null;
    }

    @Override
    public void close() throws IOException {

    }
}
