package org.pcsoft.framework.jremote.api.builder;

import org.pcsoft.framework.jremote.api.RemoteBase;
import org.pcsoft.framework.jremote.api.interf.RemoteRestService;

public interface RemoteBuilderBase<T extends RemoteBase, B extends RemoteBuilderBase<T, B>> {
    B withRestService(Class<? extends RemoteRestService> restServiceClass);

    T build();
}
