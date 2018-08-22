package org.pcsoft.framework.jremote.core.internal.validation;

import org.pcsoft.framework.jremote.api.exception.JRemoteAnnotationException;

public final class Validator {
    public static void validateForRemoteService(Class<?> clazz) throws JRemoteAnnotationException {
        RemoteServiceAnnotationValidator.getInstance().validate(clazz);
    }

    public static void validateForRemoteBroadcastService(Class<?> clazz) throws JRemoteAnnotationException {
        RemoteBroadcastServiceAnnotationValidator.getInstance().validate(clazz);
    }

    public static void validateForRemotePushService(Class<?> clazz) throws JRemoteAnnotationException {
        RemotePushServiceAnnotationValidator.getInstance().validate(clazz);
    }

    public static void validateForRemoteEventService(Class<?> clazz) throws JRemoteAnnotationException {
        RemoteEventServiceAnnotationValidator.getInstance().validate(clazz);
    }

    public static void validateForRemotePushModel(Class<?> clazz) throws JRemoteAnnotationException {
        RemotePushModelAnnotationValidator.getInstance().validate(clazz);
    }

    public static void validateForRemotePushObserver(Class<?> clazz) throws JRemoteAnnotationException {
        RemotePushObserverAnnotationValidator.getInstance().validate(clazz);
    }

    public static void validateForRemoteEventObserver(Class<?> clazz) throws JRemoteAnnotationException {
        RemoteEventReceiverAnnotationValidator.getInstance().validate(clazz);
    }

    public static void validateForRemoteRegistrationService(Class<?> clazz) throws JRemoteAnnotationException {
        RemoteRegistrationServiceAnnotationValidator.getInstance().validate(clazz);
    }

    public static void validateForRemoteKeepAliveService(Class<?> clazz) throws JRemoteAnnotationException {
        RemoteKeepAliveServiceAnnotationValidator.getInstance().validate(clazz);
    }

    public static void validateForRemoteControlService(Class<?> clazz) throws JRemoteAnnotationException {
        RemoteControlServiceAnnotationValidator.getInstance().validate(clazz);
    }

    private Validator() {
    }
}
