package org.pcsoft.framework.jremote.io.impl.tcp.internal.type;

import java.util.function.IntFunction;
import java.util.stream.Stream;

public final class Invocation {
    private final String methodName;
    private final Type returnType;
    private final Parameter[] parameters;

    public Invocation(String methodName, Type returnType, Parameter... parameters) {
        this.methodName = methodName;
        this.returnType = returnType;
        this.parameters = parameters;
    }

    public String getMethodName() {
        return methodName;
    }

    public Type getReturnType() {
        return returnType;
    }

    public Parameter[] getParameters() {
        return parameters;
    }

    public Class<?>[] getParameterClasses() {
        return Stream.of(parameters)
                .map(param -> param.type.getClazz())
                .toArray((IntFunction<Class<?>[]>) Class[]::new);
    }

    public Object[] getParameterValues() {
        return Stream.of(parameters)
                .map(param -> param.value)
                .toArray(Object[]::new);
    }

    public static final class Parameter {
        private final Type type;
        private final Object value;

        public Parameter(Type type, Object value) {
            this.type = type;
            this.value = value;
        }

        public Type getType() {
            return type;
        }

        public Object getValue() {
            return value;
        }
    }
}
