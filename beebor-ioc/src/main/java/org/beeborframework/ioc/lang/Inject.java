package org.beeborframework.ioc.lang;

import org.beeborframework.ioc.exception.NoSuchBeanException;

import java.lang.annotation.*;

/**
 * Inject
 *
 * @author zxy
 * @version 0.0.1
 * @date 2021/6/12 17:41
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Inject {

    /**
     * Dependence bean name
     *
     * @return String
     */
    String value() default "";

    /**
     * Is required, miss this bean will throw {@link NoSuchBeanException}
     *
     * @return boolean
     */
    boolean require() default true;

}
