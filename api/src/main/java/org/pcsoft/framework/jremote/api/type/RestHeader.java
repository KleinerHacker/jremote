package org.pcsoft.framework.jremote.api.type;

public final class RestHeader {
    private final String name;
    private final String value;

    public RestHeader(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}
