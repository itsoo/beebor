package com.ymm.sd.demoweb.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * BaseDTO
 *
 * @author zxy
 * @version 0.0.1
 * @date 2021/5/14 17:56
 */
@Data
@Builder
@AllArgsConstructor
public class BaseDTO {

    private int code;

    private String msg;

    private Object data;

}
