package org.pcsoft.framework.jremote.core.internal.type;

import org.pcsoft.framework.jremote.api.exception.JRemoteAnnotationException;
import org.pcsoft.framework.jremote.api.exception.JRemoteExecutionException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Abstract base class for all client wrappers
 */
public abstract class ClientWrapper {
    protected final Object clientProxy;

    public ClientWrapper(Object clientProxy) {
        this.clientProxy = clientProxy;
    }

    /**
     * Find the fit method via method finder function, assert the found function and invoke it.
     *
     * @param methodFinder Finder function to find method to invoke
     * @param assertion    Assertion predicate to assert function signature
     * @param args         Arguments to use for invocation
     * @return Result value of invocation
     */
    protected final Object findAndInvokeMethod(final Predicate<Method> methodFinder, final Predicate<Method> assertion, Object... args) {
        final Method method = Stream.of(clientProxy.getClass().getInterfaces()[0].getDeclaredMethods())
                .filter(methodFinder).findFirst().orElse(null);
        if (method == null)
            throw new IllegalStateException("Unable to find method in client proxy, see method finder");
        if (!assertion.test(method))
            throw new IllegalStateException(String.format("Found method %s#%s, but with wrong signature",
                    method.getDeclaringClass().getName(), method.getName()));

        try {
            return method.invoke(clientProxy, args);
        } catch (IllegalAccessException e) {
            throw new JRemoteAnnotationException(String.format("Unable to call not public method: %s#%s",
                    method.getDeclaringClass().getName(), method.getName()), e);
        } catch (InvocationTargetException e) {
            throw new JRemoteExecutionException(String.format("Invocation of method %s#%s throws an exception",
                    method.getDeclaringClass().getName(), method.getName()), e);
        }
    }
}
