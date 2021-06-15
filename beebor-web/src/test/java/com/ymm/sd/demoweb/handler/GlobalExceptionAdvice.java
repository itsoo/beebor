package com.ymm.sd.demoweb.handler;

import com.ymm.sd.demoweb.api.dto.BaseDTO;
import com.ymm.sd.demoweb.exception.BusinessException;
import com.ymm.sd.demoweb.exception.UniqueCheckedException;
import org.beeborframework.web.lang.advice.HandleAdvice;
import org.beeborframework.web.lang.advice.RestActionAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * BusinessExceptionAdvice
 *
 * @author zxy
 * @version 0.0.1
 * @date 2021/5/14 17:52
 */
@RestActionAdvice
public class GlobalExceptionAdvice {

    @HandleAdvice(BusinessException.class)
    public BaseDTO handleBusinessException(HttpServletRequest req, HttpServletResponse resp, BusinessException e) {
        return BaseDTO.builder().code(500).msg(e.getMessage()).build();
    }

    @HandleAdvice(UniqueCheckedException.class)
    public BaseDTO handleUniqueCheckedException(HttpServletRequest req, HttpServletResponse resp, UniqueCheckedException e) {
        return BaseDTO.builder().code(500).msg("Unique checked exception: " + e.getMessage()).build();
    }

    @HandleAdvice
    public BaseDTO handleException(HttpServletRequest req, HttpServletResponse resp, Exception e) {
        return BaseDTO.builder().code(500).msg("Exception: " + e.getMessage()).build();
    }
}
