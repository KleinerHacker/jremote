package org.pcsoft.framework.jremote.core.internal.type;

import java.util.Objects;

public final class MethodKey {
    private final Class<?> clazz;
    private final String method;

    public MethodKey(Class<?> clazz, String method) {
        this.clazz = clazz;
        this.method = method;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public String getMethod() {
        return method;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MethodKey that = (MethodKey) o;
        return Objects.equals(clazz, that.clazz) &&
                Objects.equals(method, that.method);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clazz, method);
    }

    public String toString(boolean full) {
        if (full)
            return "MethodKey{" +
                    "clazz=" + clazz +
                    ", method='" + method + '\'' +
                    '}';

        return clazz.getName() + "#" + method;
    }

    @Override
    public String toString() {
        return toString(true);
    }
}
