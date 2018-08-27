import org.pcsoft.framework.jremote.np.api.NetworkProtocolPlugin;
import org.pcsoft.framework.jremote.np.impl.rmi.RmiPlugin;

module pcsoft.jremote.io.impl.rmi {
    requires pcsoft.jremote.io.api;
    requires java.rmi;
    requires org.slf4j;
    requires pcsoft.jremote.commons;
    requires org.apache.commons.lang3;

    provides NetworkProtocolPlugin with RmiPlugin;
}