import org.pcsoft.framework.jremote.io.api.ServiceClientPlugin;

module pcsoft.jremote.core {
    requires pcsoft.jremote.api;
    requires pcsoft.jremote.io.api;
    requires org.slf4j;
    requires java.rmi;

    exports org.pcsoft.framework.jremote.core;

    uses ServiceClientPlugin;
}