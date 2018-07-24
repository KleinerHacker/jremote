import org.pcsoft.framework.jremote.io.api.Service;
import org.pcsoft.framework.jremote.io.impl.rmi.RmiService;

module pcsoft.jremote.io.impl.rmi {
    exports org.pcsoft.framework.jremote.io.impl.rmi;

    requires pcsoft.jremote.io.api;
    requires java.rmi;

    provides Service with RmiService;
}