package tank.repository;

import org.springframework.stereotype.Repository;
import tank.cache.repository.CacheSupportRepository;
import tank.domain.Item;

/**
 * @Author: tank
 * @Email: kaixiong.tan@qq.com
 * @Date: 2017/1/19
 * @Version: 1.0
 * @Description:
 */
@Repository
public class ItemRepository extends CacheSupportRepository<Item> implements IItemRepository {
}
