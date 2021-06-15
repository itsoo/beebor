package org.beeborframework.core.converter;

/**
 * TypeConverter
 *
 * @author zxy
 * @version 0.0.1
 * @date 2021/5/14 21:11
 */
public interface TypeConverter {

    /**
     * Can parsed
     *
     * @param objClass Class
     * @return boolean
     */
    boolean parsable(Class<?> objClass);

    /**
     * Type convert
     *
     * @param objClass Class
     * @param obj      Object
     * @return Object
     */
    Object convert(Class<?> objClass, Object obj);

}
