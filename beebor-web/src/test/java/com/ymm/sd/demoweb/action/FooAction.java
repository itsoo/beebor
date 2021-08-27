package com.ymm.sd.demoweb.action;

import com.ymm.sd.demoweb.api.request.FooRequest;
import com.ymm.sd.demoweb.api.response.FooResponse;
import com.ymm.sd.demoweb.service.FooService;
import org.beeborframework.ioc.lang.Inject;
import org.beeborframework.web.lang.rest.*;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.ThreadLocalRandom;

/**
 * FooAction
 *
 * @author zxy
 * @version 0.0.1
 * @date 2021/5/12 17:04
 */
@RestAction(path = "/foo")
public class FooAction {

    @Inject
    private FooService fooService;


    @Get
    public FooResponse get(@Param("name") String name, @Param("age") Integer age, @Param("phone") String phone,
                           FooRequest fooReq) {
        if (ThreadLocalRandom.current().nextBoolean()) {
            throw new NullPointerException("Tested random NullPointerException");
        }

        System.out.println("=========== " + fooService.doTest());

        return FooResponse.builder().name(name).age(age).phone(phone).build();
    }

    @Post
    public FooResponse post(@Body FooRequest body, HttpServletRequest req) {
        return FooResponse.builder().name(body.getName()).age(body.getAge()).phone(body.getPhone()).build();
    }
}
