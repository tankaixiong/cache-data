package tank.cache.repository;

import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.query.parser.sql.SQLParser;
import com.googlecode.cqengine.resultset.ResultSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tank.cache.db.*;
import tank.cache.db.exception.CacheException;

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
 * @Description: 操作缓存
 */
@Resource
public abstract class CacheSupportRepository<T> implements ICacheRepository<T> {
    //@Resource
    private DBCacheManager cache = DBCacheManager.getInstance();
    private PersistentChecker checker = PersistentChecker.getInstant();

    private Logger LOGGER = LoggerFactory.getLogger(CacheSupportRepository.class);
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

    /**
     * 获取对应roleId查询的IndexCollection
     *
     * @param roleId
     * @return
     */
    private IndexedCollection<T> getIndexCollection(long roleId) {
        RoleCacheEntity roleCacheEntity = cache.getRoleCacheEntity(roleId);
        return roleCacheEntity.getCachedByType(getEntityClass());
    }

    /**
     * 获取对应global查询的IndexCollection
     *
     * @return
     */
    private IndexedCollection<T> getIndexedCollection() {
        return cache.getGlobalCacheEntity().getCachedByType(getEntityClass());
    }

    /**
     * 获取操作update del 的IndexCollection
     *
     * @param entity
     * @return
     */
    private IndexedCollection<T> getIndexedCollection(T entity) {

        EntityMeta meta = EntityUtils.getEntityMeta(getEntityClass());
        if (meta.isCacheGlobal()) {
            return cache.getGlobalCacheEntity().getCachedByType(getEntityClass());
        } else if (meta.isCacheRole()) {
            Serializable roleId = EntityUtils.getRoleId(entity);
            RoleCacheEntity roleCacheEntity = cache.getRoleCacheEntity(roleId);
            return roleCacheEntity.getCachedByType(entity.getClass());
        } else {
            throw new CacheException("没有对应cache 类型的处理" + entity.getClass());
        }
    }

    //查询
    public List<T> find(long roleId, String query) {
        List<T> list = new ArrayList<T>();

        Class<T> clazz = getEntityClass();

        EntityMeta meta = EntityUtils.getEntityMeta(clazz);
        if (!meta.isCacheRole()) {
            throw new CacheException("错误的缓存类型的方法find(long roleId, String query) 调用" + clazz);
        }

        IndexedCollection<T> indexedCollection = getIndexCollection(roleId);//cache.getCachedByType(clazz);

        SQLParser<T> parser = cache.getSqlParserMap(clazz);

        ResultSet<T> resultSet = parser.retrieve(indexedCollection, query);
        for (T t : resultSet) {
            list.add(t);
        }


        return list;
    }

    //查询
    public List<T> find(String query) {
        Class<T> clazz = getEntityClass();

        EntityMeta meta = EntityUtils.getEntityMeta(clazz);
        if (!meta.isCacheGlobal()) {
            throw new CacheException("错误的缓存类型的方法find(String query)调用" + clazz);
        }

        List<T> list = new ArrayList<T>();

        IndexedCollection<T> indexedCollection = getIndexedCollection();//cache.getCachedByType(clazz);

        SQLParser<T> parser = cache.getSqlParserMap(clazz);

        ResultSet<T> resultSet = parser.retrieve(indexedCollection, query);
        for (T t : resultSet) {
            list.add(t);
        }
        return list;
    }

    //添加
    public void add(T entity) {

        IndexedCollection<T> indexedCollection = getIndexedCollection(entity);//cache.getCachedByType(getEntityClass());

        indexedCollection.add(entity);

        checker.change(entity, PersistentStatus.ADD);

    }

    //删除
    public void delete(T entity) {

        IndexedCollection<T> indexedCollection = getIndexedCollection(entity);//cache.getCachedByType(getEntityClass());

        if (indexedCollection.contains(entity)) {
            indexedCollection.remove(entity);

            checker.change(entity, PersistentStatus.DEL);
        } else {
            LOGGER.warn("对象不存在");
        }

    }


    //修改
    public void update(T entity) {
        IndexedCollection<T> indexedCollection = getIndexedCollection();//cache.getCachedByType(getEntityClass());

        if (indexedCollection.contains(entity)) {

            checker.change(entity, PersistentStatus.UPDATE);
        } else {
            LOGGER.warn("对象不存在");
        }

    }
}
