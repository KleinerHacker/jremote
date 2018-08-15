package org.pcsoft.framework.jremote.core.internal.proxy;

import org.pcsoft.framework.jremote.api.exception.JRemoteAnnotationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Abstract base class for all proxy builder
 *
 * @param <A> Type of used method annotation
 * @param <D> Type of data object for proxy
 */
abstract class ProxyBuilder<A extends Annotation, D> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProxyBuilder.class);

    private final Class<A> methodAnnotationClass;

    ProxyBuilder(Class<A> methodAnnotationClass) {
        this.methodAnnotationClass = methodAnnotationClass;
    }

    /**
     * Creates a new proxy for the given interface. Data of map are returned by getter methods of model.
     *
     * @param clazz
     * @param data
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    final <T> T buildProxy(Class<T> clazz, D data) {
        LOGGER.debug("Create " + getProxyName() + " proxy for " + clazz.getName());

        validate(clazz);

        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, (proxy, method, args) -> {
            LOGGER.debug(String.format("Call " + getProxyName() + " method %s#%s", clazz.getName(), method.getName()));

            final A annotation = method.getAnnotation(methodAnnotationClass);
            assert annotation != null || method.isDefault();

            if (annotation == null) {
                if (method.isDefault())
                    return MethodHandles.privateLookupIn(method.getDeclaringClass(), MethodHandles.lookup())
                            .unreflectSpecial(method, method.getDeclaringClass())
                            .bindTo(proxy)
                            .invokeWithArguments(args);

                assert false;
            } else {
                assertMethod(annotation, clazz, method, args);
            }

            return invokeMethod(annotation, data, clazz, method, args);
        });
    }

    /**
     * Run validation for class
     *
     * @param clazz
     */
    protected abstract void validate(Class<?> clazz) throws JRemoteAnnotationException;

    /**
     * Run additional assertions (use <code>assert</code>), already validated in {@link #validate(Class)}, optional
     *
     * @param annotation
     * @param method
     * @param args
     */
    protected void assertMethod(A annotation, Class<?> clazz, Method method, Object[] args) {
        //Empty, optional to overwrite
    }

    /**
     * Invoke the method logic
     *
     * @param annotation Found method annotation, see A of class
     * @param data       Data (map) to use for invocation
     * @param clazz      Class of proxy
     * @param method     Method to invoke
     * @param args       Args of methods
     * @return Return value of method
     */
    protected abstract Object invokeMethod(A annotation, D data, Class<?> clazz, Method method, Object[] args);

    /**
     * Returns the name of proxy (logging only)
     *
     * @return
     */
    protected abstract String getProxyName();
}
