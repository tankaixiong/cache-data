package tank.cache.repository;

import java.util.List;

/**
 * @Author: tank
 * @Email: kaixiong.tan@qq.com
 * @Date: 2017/1/16
 * @Version: 1.0
 * @Description:
 */
public interface ICacheRepository<T> extends IBaseCacheRepository<T> {

    public List<T> find(String query);

    List<T> find(long roleId, String query);
}
