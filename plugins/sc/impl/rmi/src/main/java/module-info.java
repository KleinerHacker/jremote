import org.pcsoft.framework.jremote.sc.api.ServiceClientPlugin;
import org.pcsoft.framework.jremote.sc.impl.rmi.RmiPlugin;

module pcsoft.jremote.io.impl.rmi {
    requires pcsoft.jremote.io.api;
    requires java.rmi;
    requires org.slf4j;
    requires pcsoft.jremote.commons;
    requires commons.lang;

    provides ServiceClientPlugin with RmiPlugin;
}