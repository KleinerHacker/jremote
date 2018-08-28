package org.pcsoft.framework.jremote.ext.config.api.exception;

import org.pcsoft.framework.jremote.api.exception.JRemoteExtensionException;

public class JRemoteConfigurationException extends JRemoteExtensionException {
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
