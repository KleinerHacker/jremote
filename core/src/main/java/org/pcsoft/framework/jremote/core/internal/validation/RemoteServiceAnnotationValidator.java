package org.pcsoft.framework.jremote.core.internal.validation;

class RemoteServiceAnnotationValidator extends RemoteServiceAnnotationValidatorBase {
    private static final RemoteServiceAnnotationValidator INSTANCE = new RemoteServiceAnnotationValidator();

    static RemoteServiceAnnotationValidator getInstance() {
        return INSTANCE;
    }

    private RemoteServiceAnnotationValidator() {
    }
}
