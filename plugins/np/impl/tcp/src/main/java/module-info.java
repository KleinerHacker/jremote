import org.pcsoft.framework.jremote.np.api.NetworkProtocolPlugin;
import org.pcsoft.framework.jremote.np.impl.tcp.TcpPlugin;

module pcsoft.jremote.io.impl.tcp {
    requires pcsoft.jremote.io.api;

    provides NetworkProtocolPlugin with TcpPlugin;
}