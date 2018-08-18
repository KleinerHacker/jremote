package org.pcsoft.framework.jremote.core.internal.proxy;

import org.pcsoft.framework.jremote.api.PushModelProperty;
import org.pcsoft.framework.jremote.api.RemotePushModel;
import org.pcsoft.framework.jremote.api.exception.JRemoteAnnotationException;
import org.pcsoft.framework.jremote.core.internal.type.MethodKey;
import org.pcsoft.framework.jremote.core.internal.validation.Validator;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * Creator for a {@link RemotePushModel} annotated interface
 */
final class RemotePushModelProxyBuilder extends ProxyBuilder<PushModelProperty, Map<MethodKey, Object>> {
    private static final RemotePushModelProxyBuilder INSTANCE = new RemotePushModelProxyBuilder();

    public static RemotePushModelProxyBuilder getInstance() {
        return INSTANCE;
    }

    @Override
    protected void validate(Class<?> clazz) throws JRemoteAnnotationException {
        Validator.validateForRemotePushModel(clazz);
    }

    @Override
    protected void assertMethod(PushModelProperty annotation, Class<?> clazz, Method method, Object[] args) {
        assert method.getParameterCount() == 0 && method.getReturnType() != void.class;
    }

    @Override
    protected Object invokeMethod(PushModelProperty modelProperty, Map<MethodKey, Object> dataMap, Class<?> clazz, Method method, Object[] args) {
        final MethodKey key = new MethodKey(modelProperty.sourcePushClass(), modelProperty.sourcePushMethod());
        final Object value = dataMap.get(key);
        if (value == null) {
            if (method.getReturnType() == char.class)
                return '\u0000';
            if (method.getReturnType() == byte.class || method.getReturnType() == short.class || method.getReturnType() == int.class ||
                    method.getReturnType() == long.class)
                return 0;
            if (method.getReturnType() == double.class || method.getReturnType() == float.class)
                return 0.0;
            if (method.getReturnType() == boolean.class)
                return false;
        }

        return value;
    }

    @Override
    protected String getProxyName() {
        return "Remote Model";
    }

    private RemotePushModelProxyBuilder() {
        super(PushModelProperty.class);
    }
}
