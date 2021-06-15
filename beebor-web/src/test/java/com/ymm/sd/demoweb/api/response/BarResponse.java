package com.ymm.sd.demoweb.api.response;

import lombok.Builder;
import lombok.Data;

/**
 * BarResponse
 *
 * @author zxy
 * @version 0.0.1
 * @date 2021/5/14 9:17
 */
@Data
@Builder
public class BarResponse {

    private Long id;

    private String name;

    private String department;

}
