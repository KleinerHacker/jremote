package org.pcsoft.framework.jremote.core.internal.proxy;

import org.apache.commons.lang3.ArrayUtils;
import org.pcsoft.framework.jremote.api.Push;
import org.pcsoft.framework.jremote.api.PushModelProperty;
import org.pcsoft.framework.jremote.api.exception.JRemoteAnnotationException;
import org.pcsoft.framework.jremote.api.exception.JRemoteExecutionException;
import org.pcsoft.framework.jremote.api.type.PushChangedListener;
import org.pcsoft.framework.jremote.api.type.PushItemUpdate;
import org.pcsoft.framework.jremote.api.type.PushMethodType;
import org.pcsoft.framework.jremote.commons.type.MethodKey;
import org.pcsoft.framework.jremote.core.internal.validation.Validator;
import org.pcsoft.framework.jremote.ext.up.api.UpdatePolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Stream;

final class RemotePushServiceProxyBuilder extends ProxyBuilder<Push, RemotePushServiceProxyBuilder.DataHolder> {
    private static final RemotePushServiceProxyBuilder INSTANCE = new RemotePushServiceProxyBuilder();
    private static final Logger LOGGER = LoggerFactory.getLogger(RemotePushServiceProxyBuilder.class);

    public static RemotePushServiceProxyBuilder getInstance() {
        return INSTANCE;
    }

    @Override
    protected void validate(Class<?> clazz) throws JRemoteAnnotationException {
        Validator.validateForRemoteService(clazz);
        Validator.validateForRemotePushService(clazz);
    }

    @Override
    protected void assertMethod(Push push, Class<?> clazz, Method method, Object[] args) {
        switch (push.type()) {
            case Default:
                assert method.getParameterCount() == 1 && method.getReturnType() == void.class;
                break;
            case SingleListItem:
                assert method.getParameterCount() == 2 && method.getReturnType() == void.class;
                break;
            default:
                throw new RuntimeException();
        }
    }

    @Override
    protected Object invokeMethod(Push push, DataHolder data, Class<?> clazz, Method method, Object[] args) {
        final MethodKey key = new MethodKey(push.modelClass(), push.property());
        final PushItemUpdate pushItemUpdate = extractPushItemUpdate(push.type(), args);
        final Class<?> targetType = findTypeFor(push, method, data.getRemoteModelClassesSupplier().get());

        //Run via update policy. The algorithm can run given callback at any time (on own thread or directly)
        data.getUpdatePolicy().runModelUpdateAndObserverInvocation(key, pushItemUpdate, args[0], () -> {
            updateData(push, key, targetType, args, data.getDataMap());
            fireChange(key, data.getListenerMap());
        });

        return null;
    }

    @Override
    protected String getProxyName() {
        return "Remote Push Service";
    }

    private static Class<?> findTypeFor(Push push, Method pushMethod, Class<?>[] modelClasses) {
        final Method propertyMethod = Arrays.stream(modelClasses)
                .filter(c -> c == push.modelClass())
                .map(Class::getDeclaredMethods)
                .flatMap(Stream::of)
                .filter(m -> m.getAnnotation(PushModelProperty.class) != null)
                .filter(m -> m.getAnnotation(PushModelProperty.class).value().equals(push.property()))
                .findFirst().orElseThrow(() -> new JRemoteAnnotationException(String.format("Unable to find push target %s > %s: %s#%s",
                        push.modelClass().getName(), push.property(), pushMethod.getDeclaringClass().getName(), pushMethod.getName())));

        return propertyMethod.getReturnType();
    }

    private static void fireChange(MethodKey key, Map<MethodKey, List<PushChangedListener>> listenerMap) {
        if (listenerMap.containsKey(key)) {
            LOGGER.debug("> Fire change event for " + key.toString(false));
            for (final PushChangedListener listener : listenerMap.get(key)) {
                listener.onChange();
            }
        } else {
            LOGGER.trace("> Ignore fire change event, key not found: " + key.toString(false));
        }
    }

    private static void updateData(Push push, MethodKey key, Class<?> targetType, Object[] args, Map<MethodKey, Object> dataMap) {
        LOGGER.debug("> Setup value into model for " + key.toString(false));
        switch (push.type()) {
            case Default:
                if (Collection.class.isAssignableFrom(targetType)) {
                    if (!dataMap.containsKey(key)) {
                        try {
                            dataMap.put(key, createCollection(targetType));
                        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException e) {
                            throw new JRemoteAnnotationException(String.format("Unable to find a public empty constructor or class is abstract for %s, see %s > %s",
                                    targetType.getName(), push.modelClass().getName(), push.property()), e);
                        } catch (InvocationTargetException e) {
                            throw new JRemoteExecutionException(String.format("Unable to create new instance (throws exception) of collection %s, see %s > %s",
                                    targetType.getName(), push.modelClass().getName(), push.property()), e);
                        }
                    }
                    handleCompleteCollectionPush((Collection) dataMap.get(key), args[0]);
                } else if (targetType.isArray()) {
                    if (!dataMap.containsKey(key)) {
                        dataMap.put(key, Array.newInstance(Object.class, 0));
                    }
                    dataMap.put(key, handleCompleteArrayPush((Object[]) dataMap.get(key), args[0]));
                } else {
                    handleDefaultPush(dataMap, args[0], key);
                }
                break;
            case SingleListItem:
                if (dataMap.get(key) instanceof Collection) {
                    handleSingleCollectionItemPush((Collection) dataMap.get(key), args[0], (PushItemUpdate) args[1], push);
                } else if (dataMap.get(key).getClass().isArray()) {
                    dataMap.put(key, handleSingleArrayItemPush((Object[]) dataMap.get(key), args[0], (PushItemUpdate) args[1], push));
                } else
                    throw new JRemoteAnnotationException("List pushes only allowed for collections and arrays: " + push.modelClass().getName() + " > " + push.property());
                break;
            default:
                throw new RuntimeException();
        }
    }

    private static Object createCollection(Class<?> targetType) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        if (!Modifier.isAbstract(targetType.getModifiers()) && !Modifier.isInterface(targetType.getModifiers()))
            return targetType.getConstructor().newInstance();
        else if (List.class.isAssignableFrom(targetType))
            return new ArrayList();
        else if (Set.class.isAssignableFrom(targetType))
            return new HashSet();
        else
            throw new InstantiationException("Unsupported collection type: " + targetType.getName() + ", is abstract or interface");
    }

    private static Object[] handleSingleArrayItemPush(Object[] array, Object item, PushItemUpdate itemUpdate, Push push) {
        switch (itemUpdate) {
            case Add:
                return ArrayUtils.add(array, item);
            case Update:
                handleArrayUpdate(array, item, push);
                return array;
            case Remove:
                return ArrayUtils.remove(array, ArrayUtils.indexOf(array, item));
            default:
                throw new RuntimeException();
        }
    }

    private static void handleArrayUpdate(Object[] array, Object item, Push push) {
        final int index = ArrayUtils.indexOf(array, item);
        if (index < 0) {
            LOGGER.warn("Unable to find item in array to update model: " + push.modelClass().getName() + " > " + push.property());
            LOGGER.warn("Ignore update");

            return;
        }

        array[index] = item;
    }

    private static Object[] handleCompleteArrayPush(Object[] array, Object item) {
        if (!item.getClass().isArray())
            throw new JRemoteExecutionException("Wrong object item received: " + item + ", expected is array");

        if (item.getClass().getComponentType() == byte.class) {
            return ArrayUtils.toObject((byte[]) item);
        } else if (item.getClass().getComponentType() == short.class) {
            return ArrayUtils.toObject((short[]) item);
        } else if (item.getClass().getComponentType() == int.class) {
            return ArrayUtils.toObject((int[]) item);
        } else if (item.getClass().getComponentType() == long.class) {
            return ArrayUtils.toObject((long[]) item);
        } else if (item.getClass().getComponentType() == double.class) {
            return ArrayUtils.toObject((double[]) item);
        } else if (item.getClass().getComponentType() == float.class) {
            return ArrayUtils.toObject((float[]) item);
        } else if (item.getClass().getComponentType() == boolean.class) {
            return ArrayUtils.toObject((boolean[]) item);
        } else if (item.getClass().getComponentType() == char.class) {
            return ArrayUtils.toObject((char[]) item);
        }

        return (Object[]) item;
    }

    @SuppressWarnings("unchecked")
    private static void handleSingleCollectionItemPush(Collection collection, Object item, PushItemUpdate itemUpdate, Push push) {
        switch (itemUpdate) {
            case Add:
                collection.add(item);
                break;
            case Update:
                handleCollectionUpdate(collection, item, push);
                break;
            case Remove:
                collection.remove(item);
                break;
            default:
                throw new RuntimeException();
        }
    }

    @SuppressWarnings("unchecked")
    private static void handleCollectionUpdate(Collection collection, Object item, Push push) {
        if (collection instanceof List) {
            final int index = ((List) collection).indexOf(item);
            if (index < 0) {
                LOGGER.warn("Unable to find item in list to update model: " + push.modelClass().getName() + " > " + push.property());
                LOGGER.warn("Ignore update");

                return;
            }
            collection.remove(item);
            ((List) collection).add(index, item);
        } else {
            collection.remove(item);
            collection.add(item);
        }
    }

    @SuppressWarnings("unchecked")
    private static void handleCompleteCollectionPush(Collection collection, Object item) {
        collection.clear();
        collection.addAll((Collection) item);
    }

    private static void handleDefaultPush(Map<MethodKey, Object> dataMap, Object data, MethodKey key) {
        dataMap.put(key, data);
    }

    private static PushItemUpdate extractPushItemUpdate(PushMethodType type, Object[] args) {
        if (type == PushMethodType.SingleListItem)
            return (PushItemUpdate) args[1];

        return null;
    }

    private RemotePushServiceProxyBuilder() {
        super(Push.class);
    }

    //<editor-fold desc="Helper Classes">
    static final class DataHolder {
        private final Map<MethodKey, Object> dataMap;
        private final Map<MethodKey, List<PushChangedListener>> listenerMap;
        private final Supplier<Class<?>[]> remoteModelClassesSupplier;

        private final UpdatePolicy updatePolicy;

        public DataHolder(Map<MethodKey, Object> dataMap, Map<MethodKey, List<PushChangedListener>> listenerMap, Supplier<Class<?>[]> remoteModelClassesSupplier, UpdatePolicy updatePolicy) {
            this.dataMap = dataMap;
            this.listenerMap = listenerMap;
            this.remoteModelClassesSupplier = remoteModelClassesSupplier;
            this.updatePolicy = updatePolicy;
        }

        public Map<MethodKey, Object> getDataMap() {
            return dataMap;
        }

        public Map<MethodKey, List<PushChangedListener>> getListenerMap() {
            return listenerMap;
        }

        public Supplier<Class<?>[]> getRemoteModelClassesSupplier() {
            return remoteModelClassesSupplier;
        }

        public UpdatePolicy getUpdatePolicy() {
            return updatePolicy;
        }
    }
    //</editor-fold>
}
