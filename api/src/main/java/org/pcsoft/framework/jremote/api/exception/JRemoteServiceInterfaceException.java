package org.pcsoft.framework.jremote.api.exception;

public class JRemoteServiceInterfaceException extends JRemoteException {
    public JRemoteServiceInterfaceException() {
    }

    public JRemoteServiceInterfaceException(String message) {
        super(message);
    }

    public JRemoteServiceInterfaceException(String message, Throwable cause) {
        super(message, cause);
    }

    public JRemoteServiceInterfaceException(Throwable cause) {
        super(cause);
    }
}
