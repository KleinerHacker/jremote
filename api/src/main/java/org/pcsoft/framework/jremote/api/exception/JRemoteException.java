package org.pcsoft.framework.jremote.api.exception;

public class JRemoteException extends RuntimeException {
    public JRemoteException() {
    }

    public JRemoteException(String message) {
        super(message);
    }

    public JRemoteException(String message, Throwable cause) {
        super(message, cause);
    }

    public JRemoteException(Throwable cause) {
        super(cause);
    }
}
