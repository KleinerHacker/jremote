import org.pcsoft.framework.jremote.sc.api.ServiceClientPlugin;
import org.pcsoft.framework.jremote.sc.impl.tcp.TcpPlugin;

module pcsoft.jremote.io.impl.tcp {
    requires pcsoft.jremote.io.api;

    provides ServiceClientPlugin with TcpPlugin;
}