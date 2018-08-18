package org.pcsoft.framework.jremote.core.internal.handler;

import org.pcsoft.framework.jremote.api.PushModelProperty;
import org.pcsoft.framework.jremote.api.Push;
import org.pcsoft.framework.jremote.api.RemotePushModel;
import org.pcsoft.framework.jremote.api.exception.JRemoteAnnotationException;
import org.pcsoft.framework.jremote.api.exception.JRemoteExecutionException;
import org.pcsoft.framework.jremote.commons.AnnotationUtils;
import org.pcsoft.framework.jremote.commons.ReflectionUtils;
import org.pcsoft.framework.jremote.core.Client;
import org.pcsoft.framework.jremote.core.internal.proxy.ProxyFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Handler to push all model data to client with given values (from implementation)
 */
public final class PushModelHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(PushModelHandler.class);

    private final Object model;

    /**
     * Creates handler
     *
     * @param model Implementation of a {@link RemotePushModel} with values to push to client
     */
    public PushModelHandler(Object model) {
        if (ReflectionUtils.findInterfaces(model.getClass(), AnnotationUtils::isRemoteModel).isEmpty())
            throw new JRemoteAnnotationException("Given object is not a remote model: " + model.getClass().getName());

        this.model = model;
    }

    /**
     * Push all data to a concrete client. Search in the given model for {@link PushModelProperty} methods and invoke there. At the end
     * the push client and the push method is searched for and invoke with get value from model.
     *
     * @param client Client to push with data
     */
    public void pushModelData(Client client) {
        //Map of push client proxies (for re-use)
        final Map<Class<?>, Object> pushClientProxyMap = new HashMap<>();

        //Find all property methods
        final List<Method> propertyMethods = AnnotationUtils.getModelProperties(model.getClass());
        for (final Method propertyMethod : propertyMethods) {
            final PushModelProperty modelProperty = propertyMethod.getAnnotation(PushModelProperty.class);
            assert modelProperty != null;

            //Create a push invoke object (containing a method to invoke push method)
            final PushInvoke pushInvoke = createPushInvoke(client, modelProperty, propertyMethod, pushClientProxyMap);
            if (pushInvoke == null) return;

            //Get value from model
            final Object value;
            try {
                value = propertyMethod.invoke(model);
            } catch (IllegalAccessException e) {
                throw new JRemoteAnnotationException(String.format("Unable to invoke model property method %s#%s: private access denied",
                        propertyMethod.getDeclaringClass().getName(), propertyMethod.getName()), e);
            } catch (InvocationTargetException e) {
                throw new JRemoteExecutionException(String.format("Unable to invoke model property method %s#%s: method throws an exception",
                        propertyMethod.getDeclaringClass().getName(), propertyMethod.getName()), e);
            }

            //Invoke push method
            pushInvoke.invoke(value);
        }
    }

    /**
     * Creates a push invoke object to call found method directly via {@link PushInvoke#invoke(Object)} method
     *
     * @param client             Client to push data to
     * @param modelProperty      Property in {@link RemotePushModel}
     * @param propertyMethod     Method of property in {@link RemotePushModel}
     * @param pushClientProxyMap Map of proxies (only for re-use, performance)
     * @return The created push invoke
     */
    private PushInvoke createPushInvoke(final Client client, final PushModelProperty modelProperty, final Method propertyMethod,
                                        final Map<Class<?>, Object> pushClientProxyMap) {
        if (!pushClientProxyMap.containsKey(modelProperty.sourcePushClass())) {
            final Object proxy = ProxyFactory.buildRemoteClientProxy(modelProperty.sourcePushClass(), client.getHost(), client.getPort());
            pushClientProxyMap.put(modelProperty.sourcePushClass(), proxy);
        }

        //Search for push client (see annotation value)
        final Object pushClient = pushClientProxyMap.get(modelProperty.sourcePushClass());
        if (pushClient == null) {
            LOGGER.warn("Unable to find push client for " + modelProperty.sourcePushClass() + ", ignore model data push");
            return null;
        }
        //Find push method, based on push class
        final Method pushMethod = Arrays.stream(modelProperty.sourcePushClass().getDeclaredMethods())
                .filter(m -> m.getName().equals(modelProperty.sourcePushMethod()))
                .findFirst().orElse(null);
        if (pushMethod == null) {
            LOGGER.warn(String.format("Unable to find push method %s#%s, defined in %s#%s",
                    modelProperty.sourcePushClass().getName(), modelProperty.sourcePushMethod(),
                    propertyMethod.getDeclaringClass().getName(), propertyMethod.getName()));
            return null;
        }
        //Extract push annotation
        final Push push = pushMethod.getAnnotation(Push.class);
        assert push != null;

        //Create push invoke for external use
        return new PushInvoke(pushClient, pushMethod, push);
    }

    //region Helper Classes

    /**
     * A push invoke class to invoke a concrete push method
     */
    private static final class PushInvoke {
        private final Object pushClient;
        private final Method pushMethod;
        private final Push push;

        private PushInvoke(Object pushClient, Method pushMethod, Push push) {
            this.pushClient = pushClient;
            this.pushMethod = pushMethod;
            this.push = push;
        }

        /**
         * Invoke the push method with given value
         *
         * @param value Value to push
         */
        private void invoke(Object value) {
            switch (push.type()) {
                case Simple:
                case CompleteList:
                    pushToSimpleField(pushClient, pushMethod, value);
                    break;
                case SingleListItem:
                    //TODO
                    break;
                default:
                    throw new RuntimeException();
            }
        }

        private void pushToSimpleField(Object pushClient, Method pushMethod, Object value) {
            try {
                pushMethod.invoke(pushClient, value);
            } catch (IllegalAccessException e) {
                throw new JRemoteAnnotationException(String.format("Unable to invoke push method %s#%s: private access denied",
                        pushMethod.getDeclaringClass().getName(), pushMethod.getName()), e);
            } catch (InvocationTargetException e) {
                throw new JRemoteExecutionException(String.format("Unable to invoke push method %s#%s: method throws an exception",
                        pushMethod.getDeclaringClass().getName(), pushMethod.getName()), e);
            }
        }
    }

    //endregion
}
