package org.pcsoft.framework.jremote.ext.up.impl.queued.internal;

import org.pcsoft.framework.jremote.api.type.PushItemUpdate;
import org.pcsoft.framework.jremote.commons.type.MethodKey;

import java.util.Objects;

public final class UpdateKey implements Comparable<UpdateKey> {
    private final MethodKey methodKey;
    private final PushItemUpdate update;
    private final Object value;

    public UpdateKey(MethodKey methodKey, PushItemUpdate update, Object value) {
        this.methodKey = methodKey;
        this.update = update;
        this.value = update == null ? null : value; //Only used in case of single item list update
    }

    public MethodKey getMethodKey() {
        return methodKey;
    }

    public PushItemUpdate getUpdate() {
        return update;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public int compareTo(UpdateKey o) {
        return (this.methodKey.toString(false) + "$" + this.update)
                .compareTo(o.methodKey.toString(false) + "$" + o.update);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UpdateKey updateKey = (UpdateKey) o;
        return Objects.equals(methodKey, updateKey.methodKey) &&
                update == updateKey.update &&
                Objects.equals(value, updateKey.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(methodKey, update, value);
    }

    @Override
    public String toString() {
        return "UpdateKey{" +
                "methodKey=" + methodKey +
                ", update=" + update +
                ", value=" + value +
                '}';
    }
}
