import org.pcsoft.framework.jremote.io.api.ServiceClientPlugin;
import org.pcsoft.framework.jremote.io.impl.rmi.RmiPlugin;

module pcsoft.jremote.io.impl.rmi {
    requires pcsoft.jremote.io.api;
    requires java.rmi;

    provides ServiceClientPlugin with RmiPlugin;
}