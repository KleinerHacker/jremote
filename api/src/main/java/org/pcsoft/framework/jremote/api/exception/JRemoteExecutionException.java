package org.pcsoft.framework.jremote.api.exception;

public class JRemoteExecutionException extends JRemoteException {
    public JRemoteExecutionException() {
    }

    public JRemoteExecutionException(String message) {
        super(message);
    }

    public JRemoteExecutionException(String message, Throwable cause) {
        super(message, cause);
    }

    public JRemoteExecutionException(Throwable cause) {
        super(cause);
    }
}
