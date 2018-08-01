import org.pcsoft.framework.jremote.io.api.IoPlugin;
import org.pcsoft.framework.jremote.io.impl.tcp.TcpPlugin;

module pcsoft.jremote.io.impl.tcp {
    requires pcsoft.jremote.io.api;

    provides IoPlugin with TcpPlugin;
}