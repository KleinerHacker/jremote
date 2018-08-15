package org.pcsoft.framework.jremote.core.internal.proxy;

import org.pcsoft.framework.jremote.api.ModelProperty;
import org.pcsoft.framework.jremote.api.exception.JRemoteAnnotationException;
import org.pcsoft.framework.jremote.core.internal.type.PushMethodKey;
import org.pcsoft.framework.jremote.core.internal.validation.Validator;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * Creator for a {@link org.pcsoft.framework.jremote.api.RemoteModel} annotated interface
 */
final class RemoteModelProxyBuilder extends ProxyBuilder<ModelProperty, Map<PushMethodKey, Object>> {
    private static final RemoteModelProxyBuilder INSTANCE = new RemoteModelProxyBuilder();

    public static RemoteModelProxyBuilder getInstance() {
        return INSTANCE;
    }

    @Override
    protected void validate(Class<?> clazz) throws JRemoteAnnotationException {
        Validator.validateForRemoteModel(clazz);
    }

    @Override
    protected void assertMethod(ModelProperty annotation, Class<?> clazz, Method method, Object[] args) {
        assert method.getParameterCount() == 0 && method.getReturnType() != void.class;
    }

    @Override
    protected Object invokeMethod(ModelProperty modelProperty, Map<PushMethodKey, Object> dataMap, Class<?> clazz, Method method, Object[] args) {
        final PushMethodKey key = new PushMethodKey(modelProperty.sourcePushClass(), modelProperty.sourcePushMethod());
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

    private RemoteModelProxyBuilder() {
        super(ModelProperty.class);
    }
}
