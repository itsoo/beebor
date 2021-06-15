package org.beeborframework.web.lang.component;

import java.lang.annotation.*;

/**
 * Service
 *
 * @author zxy
 * @version 0.0.1
 * @date 2021/5/11 12:57
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Service {

    /**
     * Bean name
     *
     * @return String
     */
    String value() default "";

}