package org.beeborframework.web.handler;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * AbstractHttpServlet
 *
 * @author zxy
 * @version 0.0.1
 * @date 2021/5/13 20:10
 */
public abstract class AbstractHttpServlet extends HttpServlet implements HttpServletResponsive {

    /**
     * Dispatcher request
     *
     * @param req  HttpServletRequest
     * @param resp HttpServletResponse
     */
    abstract protected void doDispatch(HttpServletRequest req, HttpServletResponse resp);

    /**
     * Do response
     *
     * @param responseBody String
     * @param resp         HttpServletResponse
     */
    abstract protected void doResponse(String responseBody, HttpServletResponse resp);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        doDispatch(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        doDispatch(req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        doDispatch(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        doDispatch(req, resp);
    }
}
