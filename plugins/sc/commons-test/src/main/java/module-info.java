open module pcsoft.jremote.io.commons.test {
    requires commons.lang;
    requires java.logging;
    requires pcsoft.jremote.core;
    requires org.junit.jupiter.api;
    requires pcsoft.jremote.api;
    requires java.rmi;

    exports org.pcsoft.framework.jremote.sc.commons.test;
}