package org.pcsoft.framework.jremote.core.internal.validation;

import org.pcsoft.framework.jremote.api.exception.JRemoteAnnotationException;

public final class Validator {
    public static void validateForRemoteService(Class<?> clazz) throws JRemoteAnnotationException {
        RemoteServiceAnnotationValidator.getInstance().validate(clazz);
    }

    public static void validateForRemotePushService(Class<?> clazz) throws JRemoteAnnotationException {
        RemotePushServiceAnnotationValidator.getInstance().validate(clazz);
    }

    public static void validateForRemoteModel(Class<?> clazz) throws JRemoteAnnotationException {
        RemoteModelAnnotationValidator.getInstance().validate(clazz);
    }

    public static void validateForRemoteObserver(Class<?> clazz) throws JRemoteAnnotationException {
        RemoteObserverAnnotationValidator.getInstance().validate(clazz);
    }

    private Validator() {
    }
}
