package org.pcsoft.framework.jremote.io.impl.tcp.internal;

import org.pcsoft.framework.jremote.io.impl.tcp.internal.type.Invocation;
import org.pcsoft.framework.jremote.io.impl.tcp.internal.type.Type;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.function.Function;

public final class TcpServer {
    private final ServerSocket serverSocket;
    private final Function<Invocation, Object> invocationCallback;

    public TcpServer(ServerSocket serverSocket, Function<Invocation, Object> invocationCallback) {
        this.serverSocket = serverSocket;
        this.invocationCallback = invocationCallback;
    }

    public void acceptAndHandle() throws IOException {
        final Socket socket = serverSocket.accept();
        readData(socket);
        writeData(socket);
    }

    private void readData(final Socket socket) throws IOException {
        try (final DataInputStream in = new DataInputStream(socket.getInputStream())) {
            final String methodName = readString(in);
            final Type returnType = readType(in);
            final Invocation.Parameter[] parameters = readParameters(in);

            invocationCallback.apply(new Invocation(methodName, returnType, parameters));
        }
    }

    private void writeData(final Socket socket) {
        //TODO
    }

    private String readString(DataInputStream in) throws IOException {
        final int strLength = in.readInt();
        final byte[] strBytes = new byte[strLength];
        in.readFully(strBytes);

        return new String(strBytes, StandardCharsets.UTF_8);
    }

    private Type readType(DataInputStream in) throws IOException {
        final byte value = in.readByte();
        return Type.fromValue(value);
    }

    private Invocation.Parameter[] readParameters(DataInputStream in) throws IOException {
        final int paramCount = in.readInt();
        final Invocation.Parameter[] parameters = new Invocation.Parameter[paramCount];

        for (int i = 0; i < paramCount; i++) {
            final byte typeValue = in.readByte();
            final Type type = Type.fromValue(typeValue);

            //TODO
        }

        return parameters;
    }

}
