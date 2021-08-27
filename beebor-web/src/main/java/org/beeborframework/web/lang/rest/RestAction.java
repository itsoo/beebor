package org.beeborframework.web.lang.rest;

import org.beeborframework.ioc.lang.Bean;

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
@Bean
public @interface RestAction {

    /**
     * Bean name
     *
     * @return String
     */
    String value() default "";

    /**
     * Mapping path
     *
     * @return String of Array
     */
    String[] path() default "";

}
