package org.pcsoft.framework.jremote.commons.type;

import java.util.Objects;

public final class MethodKey {
    private final Class<?> clazz;
    private final String identifier;

    public MethodKey(Class<?> clazz, String identifier) {
        this.clazz = clazz;
        this.identifier = identifier;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public String getIdentifier() {
        return identifier;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MethodKey that = (MethodKey) o;
        return Objects.equals(clazz, that.clazz) &&
                Objects.equals(identifier, that.identifier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clazz, identifier);
    }

    public String toString(boolean full) {
        if (full)
            return "MethodKey{" +
                    "clazz=" + clazz +
                    ", identifier='" + identifier + '\'' +
                    '}';

        return clazz.getName() + "#" + identifier;
    }

    @Override
    public String toString() {
        return toString(true);
    }
}
