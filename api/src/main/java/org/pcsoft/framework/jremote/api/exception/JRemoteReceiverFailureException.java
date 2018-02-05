package org.pcsoft.framework.jremote.api.exception;

public class JRemoteReceiverFailureException extends JRemoteException {
    private final int code;

    public JRemoteReceiverFailureException(String message, int code) {
        super(message);
        this.code = code;
    }
}
