import org.pcsoft.framework.jremote.sc.api.ServiceClientPlugin;

module pcsoft.jremote.core {
    requires pcsoft.jremote.api;
    requires pcsoft.jremote.io.api;
    requires pcsoft.jremote.commons;
    requires org.slf4j;
    requires java.rmi;

    exports org.pcsoft.framework.jremote.core;

    uses ServiceClientPlugin;
}