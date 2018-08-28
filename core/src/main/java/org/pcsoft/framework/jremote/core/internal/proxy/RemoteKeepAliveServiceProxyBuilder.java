package org.pcsoft.framework.jremote.core.internal.proxy;

import org.pcsoft.framework.jremote.api.exception.JRemoteAnnotationException;
import org.pcsoft.framework.jremote.core.internal.registry.ClientRegistry;
import org.pcsoft.framework.jremote.core.internal.validation.Validator;
import org.pcsoft.framework.jremote.ext.np.api.annotation.KeepAlive;

import java.lang.reflect.Method;

final class RemoteKeepAliveServiceProxyBuilder extends ProxyBuilder<KeepAlive, ClientRegistry> {
    private static final RemoteKeepAliveServiceProxyBuilder INSTANCE = new RemoteKeepAliveServiceProxyBuilder();

    public static RemoteKeepAliveServiceProxyBuilder getInstance() {
        return INSTANCE;
    }

    private RemoteKeepAliveServiceProxyBuilder() {
        super(KeepAlive.class);
    }

    @Override
    protected void validate(Class<?> clazz) throws JRemoteAnnotationException {
        Validator.validateForRemoteService(clazz);
        Validator.validateForRemoteKeepAliveService(clazz);
    }

    @Override
    protected void assertMethod(KeepAlive keepAlive, Class<?> clazz, Method method, Object[] args) {
        assert args.length == 1;
        assert args[0] instanceof String;
        assert method.getReturnType() == boolean.class || method.getReturnType() == Boolean.class;
    }

    @Override
    protected Object invokeMethod(KeepAlive keepAlive, ClientRegistry clientRegistry, Class<?> clazz, Method method, Object[] args) {
        return clientRegistry.containsClient((String) args[0]);
    }

    @Override
    protected String getProxyName() {
        return "Keep Alive Service";
    }
}
