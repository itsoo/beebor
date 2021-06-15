package org.beeborframework.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * InterceptorRegister
 *
 * @author zxy
 * @version 0.0.1
 * @date 2021/6/14 20:31
 */
public final class InterceptorRegister {

    private final List<Interceptor> interceptorChain;


    public InterceptorRegister() {
        this.interceptorChain = new LinkedList<>();
    }

    public static InterceptorRegister newInstance() {
        return new InterceptorRegister();
    }

    public List<Interceptor> getInterceptors() {
        return Collections.unmodifiableList(interceptorChain);
    }

    public void registerInterceptor(Interceptor interceptor) {
        interceptorChain.add(interceptor);
    }

    public void registerInterceptor(Collection<Interceptor> interceptors) {
        interceptorChain.addAll(interceptors);
    }

    public boolean doPreHandle(HttpServletRequest req, HttpServletResponse resp) {
        for (Interceptor interceptor : interceptorChain) {
            if (!interceptor.preHandle(req, resp)) {
                return false;
            }
        }

        return true;
    }

    public void doPostHandle(HttpServletRequest req, HttpServletResponse resp) {
        for (Interceptor interceptor : interceptorChain) {
            interceptor.postHandle(req, resp);
        }
    }
}
