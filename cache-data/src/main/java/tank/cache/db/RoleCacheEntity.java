package tank.cache.db;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.googlecode.cqengine.ConcurrentIndexedCollection;
import com.googlecode.cqengine.IndexedCollection;
import org.apache.poi.ss.formula.functions.T;
import org.slf4j.Logger;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @Author: tank
 * @Email: kaixiong.tan@qq.com
 * @Date: 2017/1/5
 * @Version: 1.0
 * @Description:
 */
public class RoleCacheEntity implements ICQCache {
    private Logger LOGGER = org.slf4j.LoggerFactory.getLogger(RoleCacheEntity.class);

    private Serializable roleId;

    private LoadingCache<Class<T>, IndexedCollection<T>> roleCache = CacheBuilder.newBuilder().build(new CacheLoader<Class<T>, IndexedCollection<T>>() {
        @Override
        public IndexedCollection load(Class<T> key) throws Exception {

            DBCacheManager dbCacheManager = DBCacheManager.getInstance();

            List<T> list = dbCacheManager.getDataProvider().findByRoleId(key, roleId);

            //集合
            IndexedCollection<T> cqIndexColl = new ConcurrentIndexedCollection<>();

            //SQLParser<T> sqlParser = CacheUtils.registerAttribute(key);//属性绑定SQLParser
            //sqlParserMap.put(key, sqlParser);

            dbCacheManager.registerParser(key);

            cqIndexColl.addAll(list);

            return cqIndexColl;
        }
    });


    @Override
    public <T> IndexedCollection<T> getCachedByType(Class entityType) {

        IndexedCollection<T> collection = null;
        try {
            collection = (IndexedCollection<T>) roleCache.get(entityType);

        } catch (ExecutionException e) {
            e.printStackTrace();
            LOGGER.error("{}");
        }

        if (collection == null) {
            throw new RuntimeException("没有找到对应的缓存对象集合", null);
        }

        return collection;
    }

    public RoleCacheEntity(Serializable roleId) {
        this.roleId = roleId;
    }
}
