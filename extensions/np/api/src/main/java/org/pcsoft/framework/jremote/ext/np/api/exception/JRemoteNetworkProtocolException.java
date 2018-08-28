package org.pcsoft.framework.jremote.ext.np.api.exception;

import org.pcsoft.framework.jremote.api.exception.JRemoteExtensionException;

public class JRemoteNetworkProtocolException extends JRemoteExtensionException {
    public JRemoteNetworkProtocolException() {
    }

    public JRemoteNetworkProtocolException(String message) {
        super(message);
    }

    public JRemoteNetworkProtocolException(String message, Throwable cause) {
        super(message, cause);
    }

    public JRemoteNetworkProtocolException(Throwable cause) {
        super(cause);
    }
}
