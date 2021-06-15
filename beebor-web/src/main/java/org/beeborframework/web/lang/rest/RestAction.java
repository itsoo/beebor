package org.beeborframework.web.lang.rest;

import java.lang.annotation.*;

/**
 * RestAction
 *
 * @author zxy
 * @version 0.0.1
 * @date 2021/5/11 12:57
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RestAction {

    /**
     * Bean name
     *
     * @return String
     */
    String name() default "";

    /**
     * Mapping path
     *
     * @return String of Array
     */
    String[] value() default "";

}
