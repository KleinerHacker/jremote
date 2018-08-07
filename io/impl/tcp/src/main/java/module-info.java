import org.pcsoft.framework.jremote.io.api.ServiceClientPlugin;
import org.pcsoft.framework.jremote.io.impl.tcp.TcpPlugin;

module pcsoft.jremote.io.impl.tcp {
    requires pcsoft.jremote.io.api;

    provides ServiceClientPlugin with TcpPlugin;
}