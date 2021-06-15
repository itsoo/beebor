package org.beeborframework.web.handler;

import lombok.Getter;
import lombok.SneakyThrows;
import org.beeborframework.web.core.HandlerMapping;
import org.beeborframework.web.exception.UnsupportedDispatcherException;
import org.beeborframework.web.interceptor.InterceptorRegister;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Objects;

import static javax.servlet.http.HttpServletResponse.SC_METHOD_NOT_ALLOWED;
import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;

/**
 * DispatcherHandler
 *
 * @author zxy
 * @version 0.0.1
 * @date 2021/5/11 13:08
 */
public class DispatcherHandler extends AbstractHttpServlet {

    public final String defaultName = "dispatcherHandler";

    private final HandlerMapping handlerMapping;

    private final DefaultExceptionHandler exceptionHandler;

    @Getter
    private final InterceptorRegister interceptorRegister;


    public DispatcherHandler(HandlerMapping handlerMapping) {
        this.handlerMapping = handlerMapping;
        this.exceptionHandler = new DefaultExceptionHandler();
        this.interceptorRegister = new InterceptorRegister();
    }

    @Override
    public void init() {
        // todo 发送容器第一次接收请求事件
        // todo 记录日志，上下文挂载
    }

    @Override
    @SneakyThrows(IOException.class)
    protected void doDispatch(HttpServletRequest req, HttpServletResponse resp) {
        HandlerInvoker hi = handlerMapping.getHandlerInvoker(req.getMethod(), req.getRequestURI());
        try {
            assertServletRequestHandler(req, hi);
        } catch (UnsupportedDispatcherException e) {
            resp.sendError(e.getSc(), e.getMessage());
            return;
        }
        // Set response headers
        setApplicationJsonResponse(resp);
        try {
            // Do prepare intercept-handle
            if (!interceptorRegister.doPreHandle(req, resp)) {
                return;
            }
            // Invoke action mapping method
            String responseBody = getResponseBody(hi.invoke(getRequestParams(req, resp, hi.getParamTypes())));
            // Do post intercept-handle
            interceptorRegister.doPostHandle(req, resp);
            // Do response
            doResponse(responseBody, resp);
        } catch (Exception e) {
            // Handle global-exception
            exceptionHandler.handle(req, resp, e);
        }
    }

    @Override
    @SneakyThrows(IOException.class)
    protected void doResponse(String responseBody, HttpServletResponse resp) {
        try (BufferedWriter bw = new BufferedWriter(resp.getWriter())) {
            bw.write(responseBody);
            bw.flush();
        }
    }

    private void assertServletRequestHandler(HttpServletRequest req, HandlerInvoker hi) {
        // 404
        if (Objects.isNull(hi)) {
            throw new UnsupportedDispatcherException(SC_NOT_FOUND, "Not found");
        }
        // 405
        if (!hi.getRequestType().name().equals(req.getMethod())) {
            throw new UnsupportedDispatcherException(SC_METHOD_NOT_ALLOWED, "Method not allowed");
        }
    }
}
