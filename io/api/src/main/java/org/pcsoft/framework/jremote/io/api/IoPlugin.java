package org.pcsoft.framework.jremote.io.api;

public interface IoPlugin {
    Class<? extends Service> getServiceClass();
    Class<? extends Client> getClientClass();
}
