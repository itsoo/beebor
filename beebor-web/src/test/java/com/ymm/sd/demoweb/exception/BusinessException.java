package com.ymm.sd.demoweb.exception;

/**
 * BusinessException
 *
 * @author zxy
 * @version 0.0.1
 * @date 2021/5/14 18:02
 */
public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }
}
