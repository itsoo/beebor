package org.beeborframework.core.converter;

import java.math.BigInteger;
import java.util.Optional;

/**
 * LongTypeConverter
 *
 * @author zxy
 * @version 0.0.1
 * @date 2021/5/14 21:11
 */
public class LongTypeConverter implements TypeConverter {

    private final TypeConverter next;


    public LongTypeConverter(TypeConverter next) {
        this.next = next;
    }

    @Override
    public boolean parsable(Class<?> objClass) {
        return Long.class.isAssignableFrom(objClass) || BigInteger.class.isAssignableFrom(objClass);
    }

    @Override
    public Object convert(Class<?> objClass, Object obj) {
        return parsable(objClass)
                ? Long.valueOf(obj.toString())
                : Optional.ofNullable(next).map(t -> t.convert(objClass, obj)).orElse(null);
    }
}
