package org.beeborframework.web.handler;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.SneakyThrows;
import org.beeborframework.core.resource.ResourceScanner;
import org.beeborframework.core.util.AnnotationUtils;
import org.beeborframework.core.util.ExceptionUtils;
import org.beeborframework.ioc.context.ContextHolder;
import org.beeborframework.web.StartLauncher;
import org.beeborframework.web.lang.advice.HandleAdvice;
import org.beeborframework.web.lang.advice.RestActionAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * DefaultExceptionHandler
 *
 * @author zxy
 * @version 0.0.1
 * @date 2021/5/13 21:03
 */
public class DefaultExceptionHandler implements ExceptionHandler, HttpServletResponsive {

    private final ResourceScanner scanner;

    private final List<HandlerEntry> handlerEntries;


    public DefaultExceptionHandler() {
        this.scanner = new ResourceScanner(StartLauncher.getEnv().getAppClass());
        this.handlerEntries = new LinkedList<>();
        this.init();
    }

    @Override
    @SneakyThrows(IOException.class)
    public void handle(HttpServletRequest req, HttpServletResponse resp, Exception e) {
        setApplicationJsonResponse(resp);
        for (HandlerEntry handlerEntry : handlerEntries) {
            if (handlerEntry.getClassType().isAssignableFrom(e.getClass())) {
                String responseBody = getResponseBody(handlerEntry.invoke(req, resp, e));
                try (BufferedWriter bw = new BufferedWriter(resp.getWriter())) {
                    bw.write(responseBody);
                    bw.flush();
                }
                return;
            }
        }
    }

    /**
     * Initial exception-handler
     */
    private void init() {
        for (Class<?> clazz : scanner.getClasses(DefaultExceptionHandler::isAdviceHandlerComponent)) {
            for (Method method : clazz.getDeclaredMethods()) {
                HandleAdvice ann = AnnotationUtils.findAnnotation(method, HandleAdvice.class);
                if (Objects.nonNull(ann)) {
                    handlerEntries.add(HandlerEntry.build(ann.value(), clazz, method));
                }
            }
        }

        handlerEntries.sort(HandlerEntries::compareDesc);
    }

    /**
     * Is rest-action-advice component
     *
     * @param clazz Class
     * @return boolean
     */
    private static boolean isAdviceHandlerComponent(Class<?> clazz) {
        return (!clazz.isAnnotation() && !clazz.isAnonymousClass())
                && AnnotationUtils.hasAnnotation(clazz, RestActionAdvice.class);
    }


    /**
     * HandlerEntry
     */
    @Data
    @AllArgsConstructor
    private static class HandlerEntry {

        private Class<? extends Exception> classType;

        private Object instance;

        private Method handleMethod;


        /**
         * Build handler-entry
         *
         * @param classType    Class
         * @param targetClass  Class
         * @param handleMethod Method
         * @return HandlerEntry
         */
        static HandlerEntry build(Class<? extends Exception> classType, Class<?> targetClass, Method handleMethod) {
            Object instance = ContextHolder.computeIvkInstance(targetClass, HandlerEntry::newInstance);
            return new HandlerEntry(classType, instance, handleMethod);
        }

        /**
         * Invoke exception-handler method
         *
         * @param req  HttpServletRequest
         * @param resp HttpServletResponse
         * @param e    Exception
         * @return Object
         */
        @SneakyThrows(ReflectiveOperationException.class)
        Object invoke(HttpServletRequest req, HttpServletResponse resp, Exception e) {
            Class<?>[] pClasses = handleMethod.getParameterTypes();
            Object[] tarObjects = new Object[pClasses.length];
            for (int i = 0; i < pClasses.length; i++) {
                if (HttpServletRequest.class.isAssignableFrom(pClasses[i])) {
                    tarObjects[i] = req;
                } else if (HttpServletResponse.class.isAssignableFrom(pClasses[i])) {
                    tarObjects[i] = resp;
                } else if (Exception.class.isAssignableFrom(pClasses[i])) {
                    tarObjects[i] = getTargetException(e);
                } else {
                    tarObjects[i] = null;
                }
            }

            return handleMethod.invoke(instance, tarObjects);
        }

        /**
         * Static new instance
         *
         * @param targetClass Class
         * @return Object
         */
        private static Object newInstance(Class<?> targetClass) {
            try {
                return targetClass.newInstance();
            } catch (ReflectiveOperationException e) {
                return null;
            }
        }

        /**
         * Get real-source exception
         *
         * @param e Exception
         * @return Throwable
         */
        private Throwable getTargetException(Exception e) {
            return (e instanceof InvocationTargetException) ? ((InvocationTargetException) e).getTargetException() : e;
        }
    }


    /**
     * HandlerEntries
     */
    private static class HandlerEntries {

        /**
         * The farther the inheritance hierarchy is from the exception, the higher the priority
         *
         * @param a HandlerEntry
         * @param b HandlerEntry
         * @return int
         */
        static int compareAsc(HandlerEntry a, HandlerEntry b) {
            int aLevel = ExceptionUtils.computeSubclassLevel(a.getClassType());
            int bLevel = ExceptionUtils.computeSubclassLevel(b.getClassType());
            return aLevel - bLevel;
        }

        /**
         * Sorted desc
         *
         * @param a HandlerEntry
         * @param b HandlerEntry
         * @return int
         */
        static int compareDesc(HandlerEntry a, HandlerEntry b) {
            return compareAsc(b, a);
        }
    }
}
