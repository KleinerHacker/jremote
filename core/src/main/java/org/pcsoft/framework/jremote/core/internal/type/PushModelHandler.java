package org.pcsoft.framework.jremote.core.internal.type;

import org.pcsoft.framework.jremote.api.ModelProperty;
import org.pcsoft.framework.jremote.api.Push;
import org.pcsoft.framework.jremote.api.exception.JRemoteAnnotationException;
import org.pcsoft.framework.jremote.api.exception.JRemoteExecutionException;
import org.pcsoft.framework.jremote.commons.AnnotationUtils;
import org.pcsoft.framework.jremote.commons.ReflectionUtils;
import org.pcsoft.framework.jremote.core.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

/**
 * Handler to push all model data to client with given values (from implementation)
 */
public final class PushModelHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(PushModelHandler.class);

    private final Object model;
    private final Function<Class<?>, Object> pushProxyFunc;

    /**
     * Creates handler
     *
     * @param model         Implementation of a {@link org.pcsoft.framework.jremote.api.RemoteModel} with values to push to client
     * @param pushProxyFunc Function to use for searching push client
     */
    public PushModelHandler(Object model, Function<Class<?>, Object> pushProxyFunc) {
        if (ReflectionUtils.findInterfaces(model.getClass(), AnnotationUtils::isRemoteModel).isEmpty())
            throw new JRemoteAnnotationException("Given object is not a remote model: " + model.getClass().getName());

        this.model = model;
        this.pushProxyFunc = pushProxyFunc;
    }

    /**
     * Push all data to a concrete client. Search in the given model for {@link ModelProperty} methods and invoke there. At the end
     * the push client and the push method is searched for and invoke with get value from model.
     *
     * @param client Client to push with data
     */
    public void pushModelData(Client client) {
        //Find all property methods
        final List<Method> propertyMethods = AnnotationUtils.getModelProperties(model.getClass());
        for (final Method propertyMethod : propertyMethods) {
            final ModelProperty modelProperty = propertyMethod.getAnnotation(ModelProperty.class);
            assert modelProperty != null;

            //Search for push client (see annotation value)
            final Object pushClient = pushProxyFunc.apply(modelProperty.sourcePushClass());
            if (pushClient == null) {
                LOGGER.warn("Unable to find push client for " + modelProperty.sourcePushClass() + ", ignore model data push");
                return;
            }
            //Find push method, based on push class
            final Method pushMethod = Arrays.stream(modelProperty.sourcePushClass().getDeclaredMethods())
                    .filter(m -> m.getName().equals(modelProperty.sourcePushMethod()))
                    .findFirst().orElse(null);
            if (pushMethod == null) {
                LOGGER.warn(String.format("Unable to find push method %s#%s, defined in %s#%s",
                        modelProperty.sourcePushClass().getName(), modelProperty.sourcePushMethod(),
                        propertyMethod.getDeclaringClass().getName(), propertyMethod.getName()));
                return;
            }
            final Push push = pushMethod.getAnnotation(Push.class);
            assert push != null;

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
            //Push value from model to client
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
