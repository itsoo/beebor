package com.ymm.sd.demoweb.service;

import com.ymm.sd.demoweb.domain.dao.FooDao;
import org.beeborframework.ioc.lang.Inject;
import org.beeborframework.web.lang.component.Service;

/**
 * FooService
 *
 * @author zxy
 * @version 0.0.1
 * @date 2021/6/14 23:45
 */
@Service
public class FooService {

    @Inject
    private FooDao fooDao;


    public String doTest() {
        return "FooService#doTest";
    }
}
