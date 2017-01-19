package tank.demo;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import tank.cache.db.DBCacheManager;
import tank.common.BaseJunit;
import tank.domain.Item;
import tank.service.ItemService;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: tank
 * @Email: kaixiong.tan@qq.com
 * @Date: 2017/1/14
 * @Version: 1.0
 * @Description:
 */
public class ItemTest extends BaseJunit {


    @Resource
    private ItemService itemService;

    @Before
    public void before() {
    }

    @After
    public void after() {
        DBCacheManager.getInstance().stop();
    }

    @Test
    public void testService() {
        List<Item> list = itemService.find(101L);
        for (Item item : list) {
            System.out.println(item.getRoleId() + " " + item.getItemId() + " " + item.getNum());
        }
    }


    @Test
    public void testAddEntity() {
        for (int i = 1; i < 10; i++) {
            Item item = new Item();
            item.setId(i*1000L);
            item.setRoleId(101L);
            item.setName("item" + i);
            item.setItemId(i * 100L);
            item.setNum(i);

            itemService.add(item);
        }

    }


}
