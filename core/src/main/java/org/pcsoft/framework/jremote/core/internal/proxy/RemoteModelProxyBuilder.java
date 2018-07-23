package org.pcsoft.framework.jremote.core.internal.proxy;

import org.pcsoft.framework.jremote.api.ModelProperty;
import org.pcsoft.framework.jremote.api.RemoteModel;
import org.pcsoft.framework.jremote.api.exception.JRemoteAnnotationException;
import org.pcsoft.framework.jremote.core.internal.type.PushMethodKey;

import java.lang.reflect.Proxy;
import java.util.Map;

/**
 * Creator for a {@link org.pcsoft.framework.jremote.api.RemoteModel} annotated interface
 */
final class RemoteModelProxyBuilder {

    /**
     * Creates a new proxy for the given interface. Data of map are returned by getter methods of model.
     * @param clazz
     * @param dataMap
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    static <T>T buildProxy(Class<T> clazz, Map<PushMethodKey, Object> dataMap) {
        if (clazz.getAnnotation(RemoteModel.class) == null)
            throw new JRemoteAnnotationException(String.format("Unable to find annotation %s on class %s", RemoteModel.class.getName(), clazz.getName()));

        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, (proxy, method, args) -> {
            final ModelProperty modelProperty = method.getAnnotation(ModelProperty.class);
            if (modelProperty == null) {
                if (method.isDefault())
                    return method.invoke(proxy, args);

                throw new JRemoteAnnotationException(String.format("Found not default method with missing annotation %s on %s#%s",
                        ModelProperty.class.getName(), clazz.getName(), method.getName()));
            } else {
                if (method.getParameterCount() > 0 || method.getReturnType() == void.class)
                    throw new JRemoteAnnotationException(String.format("Method signature wrong: need a parameter-less method with a concrete return value: %s#%s",
                            clazz.getName(), method.getName()));
            }

            return dataMap.get(new PushMethodKey(modelProperty.sourcePushClass(), modelProperty.sourcePushMethod()));
        });
    }

    private RemoteModelProxyBuilder() {
    }
}
