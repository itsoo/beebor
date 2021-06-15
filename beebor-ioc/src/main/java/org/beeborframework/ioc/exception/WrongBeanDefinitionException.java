package org.beeborframework.ioc.exception;

import org.beeborframework.core.util.StringUtils;

import java.lang.reflect.Method;

/**
 * WrongBeanDefinitionException
 *
 * @author zxy
 * @version 0.0.1
 * @date 2021/6/13 12:01
 */
public class WrongBeanDefinitionException extends RuntimeException {

    public WrongBeanDefinitionException(Method method) {
        super(StringUtils.getFormatString("The wrong of the bean definition: [{}#{}]",
                method.getDeclaringClass().getCanonicalName(), method.getName()));
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
