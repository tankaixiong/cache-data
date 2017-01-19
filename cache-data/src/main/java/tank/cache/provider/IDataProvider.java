package tank.cache.provider;

import org.apache.poi.ss.formula.functions.T;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: tank
 * @Email: kaixiong.tan@qq.com
 * @Date: 2017/1/5
 * @Version: 1.0
 * @Description: 数据接口
 */
public interface IDataProvider {

    <T> List<T> findAll(Class<T> clazz);

    List<T> findByRoleId(Class<T> clazz, Serializable roleId);

    void update(Object entity);

    void delete(Object entity);

    void add(Object entity);


}
