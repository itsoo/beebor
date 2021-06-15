package org.beeborframework.core.converter;

import java.util.Optional;

/**
 * FloatTypeConverter
 *
 * @author zxy
 * @version 0.0.1
 * @date 2021/5/14 21:11
 */
public class FloatTypeConverter implements TypeConverter {

    private final TypeConverter next;


    public FloatTypeConverter(TypeConverter next) {
        this.next = next;
    }

    @Override
    public boolean parsable(Class<?> objClass) {
        return Float.class.isAssignableFrom(objClass);
    }

    @Override
    public Object convert(Class<?> objClass, Object obj) {
        return parsable(objClass)
                ? Float.valueOf(obj.toString())
                : Optional.ofNullable(next).map(t -> t.convert(objClass, obj)).orElse(null);
    }
}
