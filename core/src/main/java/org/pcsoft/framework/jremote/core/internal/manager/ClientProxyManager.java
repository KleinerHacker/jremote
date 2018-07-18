package org.pcsoft.framework.jremote.core.internal.manager;

import org.pcsoft.framework.jremote.core.internal.type.PushMethodKey;

import java.util.HashMap;
import java.util.Map;

public final class ClientProxyManager {
    private final Map<Class<?>, Object> modelProxyMap = new HashMap<>();

    private final Map<PushMethodKey, Object> propertyValueMap = new HashMap<>();

    public void addRemoteModelProxy(Class<?> clazz) {

    }
}
