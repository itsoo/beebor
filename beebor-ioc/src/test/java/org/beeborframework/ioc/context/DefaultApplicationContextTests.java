package org.beeborframework.ioc.context;

import com.ymm.sd.demoioc.Application;
import com.ymm.sd.demoioc.domain.entity.Entity1;
import com.ymm.sd.demoioc.domain.entity.Entity2;
import org.junit.Assert;
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

        Entity1 entity1 = (Entity1) ctx.getObject("entity1");
        Entity2 entity2 = (Entity2) ctx.getObject("entity2");

        Assert.assertEquals(entity1, entity2.getEntity1());
        Assert.assertEquals(entity2, entity1.getEntity2());
    }
}
