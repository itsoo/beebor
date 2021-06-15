package org.beeborframework.web.exception;

/**
 * UnsupportedDispatcherException
 *
 * @author zxy
 * @version 0.0.1
 * @date 2021/5/13 20:52
 */
public class UnsupportedDispatcherException extends RuntimeException {

    private final int sc;

    private final String message;


    public UnsupportedDispatcherException(int sc, String message) {
        this.sc = sc;
        this.message = message;
    }

    public int getSc() {
        return sc;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
