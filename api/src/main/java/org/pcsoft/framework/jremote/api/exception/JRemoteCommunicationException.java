package org.pcsoft.framework.jremote.api.exception;

public class JRemoteCommunicationException extends JRemoteException {
    public JRemoteCommunicationException() {
    }

    public JRemoteCommunicationException(String message) {
        super(message);
    }

    public JRemoteCommunicationException(String message, Throwable cause) {
        super(message, cause);
    }

    public JRemoteCommunicationException(Throwable cause) {
        super(cause);
    }
}
