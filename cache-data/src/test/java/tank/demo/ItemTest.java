package tank.demo;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import tank.cache.db.DBCacheManager;
import tank.common.BaseJunit;
import tank.domain.Item;
import tank.service.ItemService;

import javax.annotation.Resource;
import java.util.*;

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
            System.out.println(item.getRoleId() + " " + item.getItemId() + " " + item.getNum() + item.getSubItems());
        }
    }


    @Test
    public void testAddEntity() {
        for (int i = 1; i < 10; i++) {
            Item item = new Item();
            item.setId(i * 1000L);
            item.setRoleId(101L);
            item.setName("item" + i);
            item.setItemId(i * 100L);
            item.setNum(i);

            if (i % 2 == 0) {
                List<Integer> subItem = new ArrayList<>();
                subItem.add(i * 1024);
                subItem.add(i * 123);
                item.setSubItems(subItem);


                Map<Integer, Integer> typeNum = new HashMap<>();
                typeNum.put(i, i * 12);
                typeNum.put(i * 2, i * 13);
                item.setTypeNum(typeNum);

                Set<String> alias = new HashSet<>();
                alias.add("tom");
                alias.add("joe");
                item.setAlias(alias);
            }


            itemService.add(item);
        }


    }

    @Test
    public void testUpdateService() {
        List<Item> list = itemService.find(101L);
        for (Item item : list) {
            if (item.getSubItems() != null) {
                item.getSubItems().add(111);
            }
            if (item.getAlias() != null) {
                item.getAlias().add("demo");
            }
            if (item.getTypeNum() != null) {
                item.getTypeNum().put(123, 321);
            }

            itemService.update(item);
        }
    }


}
