package org.beeborframework.web.exception;

import org.beeborframework.core.util.StringUtils;
import org.beeborframework.web.handler.HandleInvoker;

/**
 * RepeatedHandlerMappingException
 *
 * @author zxy
 * @version 0.0.1
 * @date 2021/5/13 15:28
 */
public class RepeatedHandlerMappingException extends RuntimeException {

    public RepeatedHandlerMappingException(HandleInvoker exists, HandleInvoker target) {
        super(StringUtils.getFormatString("Repeated handler mapping, please checked: [{}] {}#{} or [{}] {}#{}",
                exists.getRequestType().name(), exists.getClassType().getCanonicalName(), exists.getHandleMethod().getName(),
                target.getRequestType().name(), target.getClassType().getCanonicalName(), target.getHandleMethod().getName()));
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
