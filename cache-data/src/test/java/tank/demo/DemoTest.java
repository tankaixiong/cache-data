package tank.demo;

import org.junit.Before;
import org.junit.Test;
import tank.cache.provider.IDataProvider;
import tank.common.BaseJunit;
import tank.domain.Demo;
import tank.service.DemoService;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: tank
 * @Email: kaixiong.tan@qq.com
 * @Date: 2017/1/14
 * @Version: 1.0
 * @Description:
 */
public class DemoTest extends BaseJunit {

    @Resource
    private IDataProvider dataProvider;

    @Resource
    private DemoService demoService;

    @Before
    public void before(){
        //DBCacheManager.getInstance().setDataProvider(dataProvider);
    }
    
    @Test
    public void testService() {
        List<Demo> list=demoService.find();
        for (Demo demo : list) {
            System.out.println(demo.getRoleId()+"  "+demo.getName());
        }
    }


    @Test
    public void testAddEntity() {
        for (int i = 0; i < 1000; i++) {

            if(i%8==0){
                try {
                    Thread.sleep(1800);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Demo demo = new Demo();
            demo.setAge(i);
            demo.setRoleId(1000L + i);
            demo.setName("hihi" + i);

            demoService.add(demo);
        }

        List<Demo> list=demoService.find();
        for (Demo demo : list) {
            demo.setName(demo.getName()+"update");
            demoService.update(demo);
        }
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }



}
