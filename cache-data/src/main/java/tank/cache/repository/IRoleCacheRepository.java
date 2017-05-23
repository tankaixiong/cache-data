package tank.cache.repository;

import java.util.List;

/**
 * @Author: tank
 * @Email: kaixiong.tan@qq.com
 * @Date: 2017/1/19
 * @Version: 1.0
 * @Description:
 */
@Deprecated
public interface IRoleCacheRepository<T> extends IBaseCacheRepository<T> {

    List<T> find(long roleId, String query);
}
