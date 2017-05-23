package tank.service;

import org.springframework.stereotype.Service;
import tank.domain.Item;
import tank.repository.IItemRepository;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: tank
 * @Email: kaixiong.tan@qq.com
 * @Date: 2017/1/19
 * @Version: 1.0
 * @Description:
 */
@Service
public class ItemService {

    @Resource
    private IItemRepository itemRepository;

    public List<Item> find(long roleId) {
        //return itemRepository.find("select * from Item where num>3");
        return itemRepository.find(roleId, "select * from Item where num>3");
    }

    public void add(Item item) {

        itemRepository.add(item);
    }
}
