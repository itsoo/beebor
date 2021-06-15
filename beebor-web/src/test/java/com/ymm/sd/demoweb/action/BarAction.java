package com.ymm.sd.demoweb.action;

import com.ymm.sd.demoweb.api.response.BarResponse;
import org.beeborframework.web.lang.rest.Get;
import org.beeborframework.web.lang.rest.RestAction;

/**
 * BarAction
 *
 * @author zxy
 * @version 0.0.1
 * @date 2021/5/12 17:04
 */
@RestAction("/bar")
public class BarAction {

    @Get("/index")
    public BarResponse index() {
        return BarResponse.builder()
                .id(1L)
                .name("李四GET")
                .department("IT")
                .build();
    }
}
