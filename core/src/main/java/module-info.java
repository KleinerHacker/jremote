import org.pcsoft.framework.jremote.io.api.IoPlugin;

module pcsoft.jremote.core {
    requires pcsoft.jremote.api;
    requires pcsoft.jremote.io.api;
    requires slf4j.api;

    exports org.pcsoft.framework.jremote.core;

    uses IoPlugin;
}