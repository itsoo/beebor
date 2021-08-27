package org.beeborframework.web.interceptor;

import org.beeborframework.web.handler.HttpServletResponsive;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Interceptor
 *
 * @author zxy
 * @version 0.0.1
 * @date 2021/6/14 22:09
 */
public interface Interceptor extends HttpServletResponsive {

    /**
     * Pre-handle
     *
     * @param req  HttpServletRequest
     * @param resp HttpServletResponse
     * @return boolean
     */
    default boolean preHandle(HttpServletRequest req, HttpServletResponse resp) {
        return true;
    }

    /**
     * Post-handle
     *
     * @param req  HttpServletRequest
     * @param resp HttpServletResponse
     */
    void postHandle(HttpServletRequest req, HttpServletResponse resp);

}
