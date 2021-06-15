package com.ymm.sd.demoweb.exception;

/**
 * UniqueCheckedException
 *
 * @author zxy
 * @version 0.0.1
 * @date 2021/5/14 18:02
 */
public class UniqueCheckedException extends BusinessException {

    public UniqueCheckedException(String message) {
        super(message);
    }
}
