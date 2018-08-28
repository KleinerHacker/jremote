package org.pcsoft.framework.jremote.ext.np.impl.tcp.internal.type;

import java.util.stream.Stream;

public enum Type {
    Void((byte) 0, void.class),
    Byte((byte) 1, byte.class),
    ByteType((byte) 2, Byte.class),
    Short((byte) 3, short.class),
    ShortType((byte) 4, Short.class),
    Integer((byte) 5, int.class),
    IntegerType((byte) 6, Integer.class),
    Long((byte) 7, long.class),
    LongType((byte) 8, Long.class),
    Double((byte) 9, double.class),
    DoubleType((byte) 10, Double.class),
    Float((byte) 11, float.class),
    FloatType((byte) 12, Float.class),
    SignedByte((byte) 21, byte.class),
    SignedByteType((byte) 22, Byte.class),
    UnsignedShort((byte) 23, short.class),
    UnsignedShortType((byte) 24, Short.class),
    UnsignedInteger((byte) 25, int.class),
    UnsignedIntegerType((byte) 26, Integer.class),
    UnsignedLong((byte) 27, long.class),
    UnsignedLongType((byte) 28, Long.class),
    UnsignedDouble((byte) 29, double.class),
    UnsignedDoubleType((byte) 30, Double.class),
    UnsignedFloat((byte) 31, float.class),
    UnsignedFloatType((byte) 32, Float.class),
    Character((byte) 100, char.class),
    CharacterType((byte) 101, Character.class),
    String((byte) 110, String.class),
    Complex((byte) 255, Object.class);

    public static Type fromValue(byte value) {
        return Stream.of(values())
                .filter(item -> item.value == value)
                .findFirst().orElse(null);
    }

    private final byte value;
    private final Class<?> clazz;

    Type(byte value, Class<?> clazz) {
        this.value = value;
        this.clazz = clazz;
    }

    public byte getValue() {
        return value;
    }

    public Class<?> getClazz() {
        return clazz;
    }
}
