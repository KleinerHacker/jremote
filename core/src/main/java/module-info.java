module pcsoft.jremote.core {
    requires pcsoft.jremote.api;
    requires pcsoft.jremote.ext.np.api;
    requires pcsoft.jremote.ext.up.api;
    requires pcsoft.jremote.ext.config.api;
    requires pcsoft.jremote.commons;
    requires pcsoft.jremote.ext.np.impl.rmi;
    requires pcsoft.jremote.ext.up.impl.def;
    requires org.slf4j;
    requires java.rmi;
    requires org.apache.commons.lang3;

    exports org.pcsoft.framework.jremote.core;
}