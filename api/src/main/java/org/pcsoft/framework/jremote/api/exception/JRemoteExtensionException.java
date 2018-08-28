package org.pcsoft.framework.jremote.api.exception;

public abstract class JRemoteExtensionException extends JRemoteException {
    public JRemoteExtensionException() {
    }

    public JRemoteExtensionException(String message) {
        super(message);
    }

    public JRemoteExtensionException(String message, Throwable cause) {
        super(message, cause);
    }

    public JRemoteExtensionException(Throwable cause) {
        super(cause);
    }
}
