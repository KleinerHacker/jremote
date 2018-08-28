package org.pcsoft.framework.jremote.ext.np.impl.tcp.internal;

import org.pcsoft.framework.jremote.ext.np.api.ServiceBase;
import org.pcsoft.framework.jremote.ext.np.impl.tcp.internal.logic.TcpServer;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public final class TcpService extends ServiceBase {
    private static final ExecutorService EXECUTOR = Executors.newCachedThreadPool();

    private final AtomicBoolean interrupt = new AtomicBoolean(false);
    private ServerSocket serverSocket = null;

    public TcpService(Object serviceImplementation) {
        super(serviceImplementation);
    }

    @Override
    public void open(String host, int port) throws IOException {
        interrupt.set(false);

        serverSocket = new ServerSocket(port, 100, InetAddress.getByName(host));
        EXECUTOR.submit(() -> {
            try {
                final TcpServer tcpServer = new TcpServer(serverSocket, invocation -> {
                    try {
                        final Method method = serviceImplementation.getClass().getDeclaredMethod(invocation.getMethodName(), invocation.getParameterClasses());
                        if (method.getReturnType() != invocation.getReturnType().getClazz())
                            throw new IllegalStateException(String.format("Wrong return type found: expected is %s, but found was %s",
                                    invocation.getReturnType().getClazz().getName(), method.getReturnType().getName()));

                        return method.invoke(serviceImplementation, invocation.getParameterValues());
                    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                        throw new IllegalStateException("Unable to invoke function " + invocation.getMethodName(), e);
                    }
                });
                while (!interrupt.get()) {
                    tcpServer.acceptAndHandle();
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void close() throws IOException {
        interrupt.set(true);
        serverSocket.close();
    }
}
