package org.beeborframework.web.handler;

import lombok.SneakyThrows;
import org.beeborframework.core.converter.BasicConvertChain;
import org.beeborframework.core.converter.ObjectTypeConverter;
import org.beeborframework.core.util.AnnotationUtils;
import org.beeborframework.core.util.IoUtils;
import org.beeborframework.core.util.JsonUtils;
import org.beeborframework.web.lang.rest.Body;
import org.beeborframework.web.lang.rest.Param;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Parameter;
import java.util.Objects;

/**
 * HttpServletResponsive
 *
 * @author zxy
 * @version 0.0.1
 * @date 2021/5/14 17:20
 */
public interface HttpServletResponsive {

    /**
     * Set application/json headers
     *
     * @param resp HttpServletResponse
     */
    default void setApplicationJsonResponse(HttpServletResponse resp) {
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json;charset=UTF-8");
        resp.setHeader("Content-Disposition", "inline");
        resp.setHeader("Pragma", "No-cache");
        resp.setHeader("Cache-Control", "no-cache");
        resp.setDateHeader("Expires", 0);
    }

    /**
     * Process param and body value
     *
     * @param req    HttpServletRequest
     * @param resp   HttpServletResponse
     * @param params Array of Class
     * @return Array of Object
     */
    @SneakyThrows(IOException.class)
    default Object[] getRequestParams(HttpServletRequest req, HttpServletResponse resp, Parameter[] params) {
        Object[] result = new Object[params.length];
        for (int i = 0; i < params.length; i++) {
            if (AnnotationUtils.hasAnnotation(params[i], Body.class)) {
                String json = IoUtils.readToUnicode(req.getInputStream());
                result[i] = JsonUtils.parseObject(json, params[i].getType());
                continue;
            }

            Param ann = AnnotationUtils.findAnnotation(params[i], Param.class);
            if (Objects.nonNull(ann)) {
                result[i] = (!JsonUtils.isConvertibleClass(params[i].getType())
                        ? BasicConvertChain.convert(params[i].getType(), req.getParameter(ann.value()))
                        : null);
            } else {
                // put HttpServletRequest
                if (HttpServletRequest.class.isAssignableFrom(params[i].getType())) {
                    result[i] = req;
                }
                // put HttpServletResponse
                else if (HttpServletResponse.class.isAssignableFrom(params[i].getType())) {
                    result[i] = resp;
                }
                // put other without-annotation dto
                else {
                    result[i] = ObjectTypeConverter.INSTANCE.convert(params[i].getType(), req.getParameterMap());
                }
            }
        }

        return result;
    }

    /**
     * Process object to json
     *
     * @param obj Object
     * @return String
     */
    default String getResponseBody(Object obj) {
        if (Objects.isNull(obj)) {
            return String.valueOf((Object) null);
        }

        if (JsonUtils.isConvertibleClass(obj.getClass())) {
            return JsonUtils.toJsonString(obj);
        }

        return String.valueOf(obj);
    }
}
