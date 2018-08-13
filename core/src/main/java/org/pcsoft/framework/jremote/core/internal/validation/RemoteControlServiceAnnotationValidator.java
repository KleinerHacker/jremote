package org.pcsoft.framework.jremote.core.internal.validation;

import org.pcsoft.framework.jremote.api.ControlMethod;
import org.pcsoft.framework.jremote.api.RemoteControlService;

import java.lang.annotation.Annotation;

final class RemoteControlServiceAnnotationValidator extends SimpleAnnotationValidator {
    private static final RemoteControlServiceAnnotationValidator INSTANCE = new RemoteControlServiceAnnotationValidator();

    public static RemoteControlServiceAnnotationValidator getInstance() {
        return INSTANCE;
    }

    @Override
    protected String getServiceName() {
        return "Remote Control Service";
    }

    @Override
    protected String getServiceMethodName() {
        return "Remote Control Method";
    }

    @Override
    protected Class<? extends Annotation> getRemoteServiceAnnotation() {
        return RemoteControlService.class;
    }

    @Override
    protected Class<? extends Annotation> getRemoteMethodAnnotation() {
        return ControlMethod.class;
    }

    private RemoteControlServiceAnnotationValidator() {
    }
}
