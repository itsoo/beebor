package org.beeborframework.ioc.lang;

import java.lang.annotation.*;

/**
 * Bean
 *
 * @author zxy
 * @version 0.0.1
 * @date 2021/6/12 17:55
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Bean {

    /**
     * Bean name
     *
     * @return String
     */
    String value() default "";

    /**
     * Is config-class and this class is not bean
     *
     * @return boolean
     */
    boolean isConfig() default false;

}
