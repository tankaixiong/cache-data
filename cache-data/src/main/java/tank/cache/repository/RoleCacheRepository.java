package tank.cache.repository;

import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.query.parser.sql.SQLParser;
import com.googlecode.cqengine.resultset.ResultSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tank.cache.db.*;

import javax.annotation.Resource;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: tank
 * @Email: kaixiong.tan@qq.com
 * @Date: 2017/1/14
 * @Version: 1.0
 * @Description: 操作roleId缓存==>CacheSupportRepository 替代
 */
@Resource
@Deprecated
public abstract class RoleCacheRepository<T> implements IRoleCacheRepository<T> {
    //@Resource
    private DBCacheManager cache = DBCacheManager.getInstance();
    private PersistentChecker checker = PersistentChecker.getInstant();

    private Logger LOGGER = LoggerFactory.getLogger(RoleCacheRepository.class);
    protected Class<T> entityClass;

    public Class<T> getEntityClass() {

        if (entityClass == null) {
            Type type = getClass().getGenericSuperclass();
            if (type instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) type;
                entityClass = (Class<T>) parameterizedType.getActualTypeArguments()[0];
            }
        }

        return entityClass;
    }

    private IndexedCollection<T> getIndexCollection(long roleId) {
        RoleCacheEntity roleCacheEntity = cache.getRoleCacheEntity(roleId);
        return roleCacheEntity.getCachedByType(getEntityClass());
    }

    private IndexedCollection<T> getIndexCollection(T entity) {
        Serializable roleId = EntityUtils.getRoleId(entity);

        if (roleId != null) {
            RoleCacheEntity roleCacheEntity = cache.getRoleCacheEntity(roleId);
            return roleCacheEntity.getCachedByType(entity.getClass());
        } else {
            LOGGER.error("实体类{}没有设置roleId 注释", entity);
            throw new RuntimeException(entity.getClass() + "没有设置roleId 注释");
        }

    }

    //添加
    public void add(T entity) {

        IndexedCollection<T> indexedCollection = getIndexCollection(entity);//cache.getCachedByType(getEntityClass());

        indexedCollection.add(entity);

        checker.change(entity, PersistentStatus.ADD);

    }

    //删除
    public void delete(T entity) {

        IndexedCollection<T> indexedCollection = getIndexCollection(entity);//cache.getCachedByType(getEntityClass());

        if (indexedCollection.contains(entity)) {
            indexedCollection.remove(entity);

            checker.change(entity, PersistentStatus.DEL);
        } else {
            LOGGER.warn("对象不存在");
        }

    }


    //查询
    public List<T> find(long roleId, String query) {
        List<T> list = new ArrayList<T>();

        Class<T> clazz = getEntityClass();

        IndexedCollection<T> indexedCollection = getIndexCollection(roleId);//cache.getCachedByType(clazz);

        SQLParser<T> parser = cache.getSqlParserMap(clazz);

        ResultSet<T> resultSet = parser.retrieve(indexedCollection, query);
        for (T t : resultSet) {
            list.add(t);
        }


        return list;
    }

    //修改
    public void update(T entity) {

        IndexedCollection<T> indexedCollection = getIndexCollection(entity);//cache.getCachedByType(getEntityClass());

        if (indexedCollection.contains(entity)) {

            checker.change(entity, PersistentStatus.UPDATE);
        } else {
            LOGGER.warn("对象不存在");
        }

    }
}
