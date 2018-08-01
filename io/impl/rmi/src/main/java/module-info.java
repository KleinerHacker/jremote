import org.pcsoft.framework.jremote.io.api.IoPlugin;
import org.pcsoft.framework.jremote.io.impl.rmi.RmiPlugin;

module pcsoft.jremote.io.impl.rmi {
    requires pcsoft.jremote.io.api;
    requires java.rmi;

    provides IoPlugin with RmiPlugin;
}