module pcsoft.jremote.core {
    requires pcsoft.jremote.api;
    requires pcsoft.jremote.ext.np.api;
    requires pcsoft.jremote.ext.config.api;
    requires pcsoft.jremote.commons;
    requires org.slf4j;
    requires java.rmi;
    requires org.apache.commons.lang3;

    exports org.pcsoft.framework.jremote.core;
}