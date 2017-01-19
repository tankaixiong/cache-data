package tank.cache.repository;

/**
 * @Author: tank
 * @Email: kaixiong.tan@qq.com
 * @Date: 2017/1/19
 * @Version: 1.0
 * @Description:
 */
public interface IBaseCacheRepository<T> {


    public void add(T entity);

    public void delete(T entity);

    public void update(T entity);
}
