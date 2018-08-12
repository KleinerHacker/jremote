module pcsoft.jremote.api {
    exports org.pcsoft.framework.jremote.api;
    exports org.pcsoft.framework.jremote.api.type;
    exports org.pcsoft.framework.jremote.api.exception;

    //Internal exports
    exports org.pcsoft.framework.jremote.api.internal to pcsoft.jremote.core, pcsoft.jremote.io.api;
}