package org.beeborframework.core.converter;

import lombok.SneakyThrows;
import org.beeborframework.core.util.ArrayUtils;
import org.beeborframework.core.util.JsonUtils;
import org.beeborframework.core.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

/**
 * ObjectTypeConverter
 *
 * @author zxy
 * @version 0.0.1
 * @date 2021/5/15 12:26
 */
public class ObjectTypeConverter implements TypeConverter {

    public static final ObjectTypeConverter INSTANCE = new ObjectTypeConverter();


    private ObjectTypeConverter() {}

    @Override
    public boolean parsable(Class<?> objClass) {
        return JsonUtils.isConvertibleClass(objClass);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object convert(Class<?> objClass, Object obj) {
        if (obj instanceof HttpServletRequest) {
            return convert(objClass, ((HttpServletRequest) obj).getParameterMap());
        } else if (obj instanceof Map) {
            return convert(objClass, (Map<String, String[]>) obj);
        } else {
            return null;
        }
    }

    @SneakyThrows(ReflectiveOperationException.class)
    private Object convert(Class<?> objClass, Map<String, String[]> map) {
        Object result = objClass.newInstance();
        Field[] fields = objClass.getDeclaredFields();
        for (Field field : fields) {
            setAnyTypeFieldValue(result, field, map.get(field.getName()));
        }

        return result;
    }

    private void setAnyTypeFieldValue(Object obj, Field field, String[] values) {
        if (List.class.isAssignableFrom(field.getType())) {
            setCollectionTypeFieldValue(obj, field, values, new ArrayList<>());
        } else if (Set.class.isAssignableFrom(field.getType())) {
            setCollectionTypeFieldValue(obj, field, values, new LinkedHashSet<>());
        } else if (field.getType().isArray()) {
            setArrayTypeFieldValue(obj, field, values);
        } else if (!JsonUtils.isConvertibleClass(field.getType())) {
            setBasicTypeFieldValue(obj, field, values);
        }
    }

    private void setCollectionTypeFieldValue(Object obj, Field field, String[] values, Collection<Object> coll) {
        Type[] types = ((ParameterizedType) field.getGenericType()).getActualTypeArguments();
        if (ArrayUtils.isNotEmpty(types) && ArrayUtils.isNotEmpty(values)) {
            for (String value : values) {
                coll.add(BasicConvertChain.convert(((Class<?>) types[0]), value));
            }

            ObjectUtils.setFieldValue(obj, field.getName(), coll);
        }
    }

    private void setArrayTypeFieldValue(Object obj, Field field, String[] values) {
        Class<?> arrayClass = field.getType().getComponentType();
        Object array = Array.newInstance(arrayClass, values.length);
        for (int i = 0; i < values.length; i++) {
            Array.set(array, i, BasicConvertChain.convert(arrayClass, values[i]));
        }

        ObjectUtils.setFieldValue(obj, field.getName(), array);
    }

    private void setBasicTypeFieldValue(Object obj, Field field, String[] values) {
        if (!ArrayUtils.isEmpty(values)) {
            Object value = BasicConvertChain.convert(field.getType(), values[0]);
            ObjectUtils.setFieldValue(obj, field.getName(), value);
        }
    }
}
