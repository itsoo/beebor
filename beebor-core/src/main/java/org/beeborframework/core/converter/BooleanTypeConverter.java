package org.beeborframework.core.converter;

import java.util.Optional;

/**
 * BooleanTypeConverter
 *
 * @author zxy
 * @version 0.0.1
 * @date 2021/5/14 21:11
 */
public class BooleanTypeConverter implements TypeConverter {

    private final TypeConverter next;


    public BooleanTypeConverter(TypeConverter next) {
        this.next = next;
    }

    @Override
    public boolean parsable(Class<?> objClass) {
        return Boolean.class.isAssignableFrom(objClass);
    }

    @Override
    public Object convert(Class<?> objClass, Object obj) {
        return parsable(objClass)
                ? Boolean.valueOf(obj.toString())
                : Optional.ofNullable(next).map(t -> t.convert(objClass, obj)).orElse(null);
    }
}
