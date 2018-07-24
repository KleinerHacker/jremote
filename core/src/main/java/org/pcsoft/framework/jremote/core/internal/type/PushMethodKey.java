package org.pcsoft.framework.jremote.core.internal.type;

import java.util.Objects;

public final class PushMethodKey {
    private final Class<?> pushClass;
    private final String pushMethod;

    public PushMethodKey(Class<?> pushClass, String pushMethod) {
        this.pushClass = pushClass;
        this.pushMethod = pushMethod;
    }

    public Class<?> getPushClass() {
        return pushClass;
    }

    public String getPushMethod() {
        return pushMethod;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PushMethodKey that = (PushMethodKey) o;
        return Objects.equals(pushClass, that.pushClass) &&
                Objects.equals(pushMethod, that.pushMethod);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pushClass, pushMethod);
    }

    public String toString(boolean full) {
        if (full)
            return "PushMethodKey{" +
                    "pushClass=" + pushClass +
                    ", pushMethod='" + pushMethod + '\'' +
                    '}';

        return pushClass.getName() + "#" + pushMethod;
    }

    @Override
    public String toString() {
        return toString(true);
    }
}
