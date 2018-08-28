module pcsoft.jremote.io.impl.rmi {
    requires pcsoft.jremote.io.api;
    requires java.rmi;
    requires org.slf4j;
    requires pcsoft.jremote.commons;
    requires org.apache.commons.lang3;

    exports org.pcsoft.framework.jremote.ext.np.impl.rmi;
}