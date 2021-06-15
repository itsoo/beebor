package org.beeborframework.core.converter;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * ObjectTypeConverter
 *
 * @author zxy
 * @version 0.0.1
 * @date 2021/5/18 13:29
 */
public class ObjectTypeConverterTests {

    @Test
    public void testConvert() {
        Map<String, String[]> map = new HashMap<>();
        map.put("name", new String[]{"zhangsan"});
        map.put("age", new String[]{"20"});
        map.put("phone", new String[]{"10086"});
        map.put("alias", new String[]{"sm", "z3"});
        map.put("hats", new String[]{"2", "5", "6"});

        Object result = ObjectTypeConverter.INSTANCE.convert(FooObject.class, map);
        System.out.println(result);
    }
}
