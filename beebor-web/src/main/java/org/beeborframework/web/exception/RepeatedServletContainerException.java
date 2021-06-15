package org.beeborframework.web.exception;

/**
 * RepeatedServletContainerException
 *
 * @author zxy
 * @version 0.0.1
 * @date 2021/5/13 19:52
 */
public class RepeatedServletContainerException extends RuntimeException {

    public RepeatedServletContainerException() {
        super("The container is already running");
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
