package org.beeborframework.web.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * ExceptionHandler
 *
 * @author zxy
 * @version 0.0.1
 * @date 2021/5/14 14:20
 */
public interface ExceptionHandler {

    /**
     * Invoke handler and response
     *
     * @param req  HttpServletRequest
     * @param resp HttpServletResponse
     * @param e    Exception
     */
    void handle(HttpServletRequest req, HttpServletResponse resp, Exception e);

}
