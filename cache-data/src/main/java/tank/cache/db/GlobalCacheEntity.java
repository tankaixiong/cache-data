package tank.cache.db;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.googlecode.cqengine.ConcurrentIndexedCollection;
import com.googlecode.cqengine.IndexedCollection;
import org.apache.poi.ss.formula.functions.T;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @Author: tank
 * @Email: kaixiong.tan@qq.com
 * @Date: 2017/1/5
 * @Version: 1.0
 * @Description:
 */
@Component
public class GlobalCacheEntity implements ICQCache {
    private Logger LOGGER = org.slf4j.LoggerFactory.getLogger(GlobalCacheEntity.class);


    //两种策略，1，一表一个 IndexedCollection（class==>collection）；2，以roleId关联为一个IndexedCollection(roleId==>class==>collection)
    //private static Map<Class<T>, SQLParser<T>> sqlParserMap = new HashMap<>();

    LoadingCache<Class<T>, IndexedCollection<T>> globalCache = CacheBuilder.newBuilder().build(new CacheLoader<Class<T>, IndexedCollection<T>>() {

        @Override
        public IndexedCollection load(Class<T> key) throws Exception {
            DBCacheManager dbCacheManager=DBCacheManager.getInstance();

            List<T> list = dbCacheManager.getDataProvider().findAll(key);

            //集合
            IndexedCollection<T> cqIndexColl = new ConcurrentIndexedCollection<>();

            //SQLParser<T> sqlParser = CacheUtils.registerAttribute(key);//属性绑定SQLParser
            //dbCacheManager.registerParser(key, sqlParser);

            dbCacheManager.registerParser(key);

            cqIndexColl.addAll(list);

            return cqIndexColl;

        }
    });

//    @Override
//    public <T> SQLParser<T> getSqlParserMap(Class entityType) {
//        SQLParser<T> parser = (SQLParser<T>) sqlParserMap.get(entityType);
//        return parser;
//    }

    public <T> IndexedCollection<T> getCachedByType(Class entityType)  {

        IndexedCollection<T> collection = null;
        try {
            collection = (IndexedCollection<T>) globalCache.get(entityType);
        } catch (ExecutionException e) {
            e.printStackTrace();
            LOGGER.error("{}",e);
        }

        if (collection == null) {
            throw new RuntimeException("没有找到对应的缓存对象集合", null);
        }

        return collection;
    }
}
