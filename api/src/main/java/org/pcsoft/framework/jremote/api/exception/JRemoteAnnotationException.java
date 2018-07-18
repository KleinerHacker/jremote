package org.pcsoft.framework.jremote.api.exception;

public class JRemoteAnnotationException extends JRemoteException {
    public JRemoteAnnotationException() {
    }

    public JRemoteAnnotationException(String message) {
        super(message);
    }

    public JRemoteAnnotationException(String message, Throwable cause) {
        super(message, cause);
    }

    public JRemoteAnnotationException(Throwable cause) {
        super(cause);
    }
}
