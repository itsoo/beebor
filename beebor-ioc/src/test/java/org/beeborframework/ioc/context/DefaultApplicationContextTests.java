package org.beeborframework.ioc.context;

import com.ymm.sd.demoioc.Application;
import com.ymm.sd.demoioc.domain.DemoEntity1;
import com.ymm.sd.demoioc.domain.DemoEntity2;
import com.ymm.sd.demoioc.domain.DemoEntity3;
import com.ymm.sd.demoioc.domain.DemoEntity4;
import org.junit.Test;

/**
 * ApplicationContextTests
 *
 * @author zxy
 * @version 0.0.1
 * @date 2021/6/13 12:48
 */
public class DefaultApplicationContextTests {

    @Test
    public void test() {
        DefaultApplicationContext ctx = new DefaultApplicationContext(Application.class);
        ctx.refresh();

        DemoEntity1 entity1 = (DemoEntity1) ctx.getObject("demoEntity1");
        DemoEntity2 entity2 = (DemoEntity2) ctx.getObject("demoEntity2");
        DemoEntity3 entity3 = (DemoEntity3) ctx.getObject("demoEntity3");
        DemoEntity4 entity4 = (DemoEntity4) ctx.getObject("demoEntity4");

        System.out.println("entity1 == entity4.demoEntity1 ==> " + entity1.equals(entity4.getDemoEntity1()));
        System.out.println("entity2 == entity1.demoEntity2 ==> " + entity2.equals(entity1.getDemoEntity2()));
        System.out.println("entity3 == entity2.demoEntity3 ==> " + entity3.equals(entity2.getDemoEntity3()));
        System.out.println("entity4 == entity3.demoEntity4 ==> " + entity4.equals(entity3.getDemoEntity4()));
    }
}
