package org.pcsoft.framework.jremote.api.exception;

public class JRemoteConfigurationException extends JRemoteException {
    public JRemoteConfigurationException() {
    }

    public JRemoteConfigurationException(String message) {
        super(message);
    }

    public JRemoteConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }

    public JRemoteConfigurationException(Throwable cause) {
        super(cause);
    }
}
