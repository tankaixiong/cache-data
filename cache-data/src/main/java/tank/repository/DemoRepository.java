package tank.repository;

import org.springframework.stereotype.Repository;
import tank.cache.repository.CacheSupportRepository;
import tank.domain.Demo;

/**
 * @Author: tank
 * @Email: kaixiong.tan@qq.com
 * @Date: 2017/1/14
 * @Version: 1.0
 * @Description:
 */
@Repository
public class DemoRepository extends CacheSupportRepository<Demo> implements IDemoRepository {
}
