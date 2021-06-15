package org.beeborframework.ioc.exception;

import org.beeborframework.core.util.StringUtils;

/**
 * NoSuchBeanException
 *
 * @author zxy
 * @version 0.0.1
 * @date 2021/6/13 11:43
 */
public class NoSuchBeanException extends RuntimeException {

    public NoSuchBeanException(String beanName) {
        super(StringUtils.getFormatString("Cannot found bean of name: [{}]", beanName));
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
