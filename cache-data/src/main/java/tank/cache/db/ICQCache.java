package tank.cache.db;

import com.googlecode.cqengine.IndexedCollection;

import java.util.concurrent.ExecutionException;

/**
 * @Author: tank
 * @Email: kaixiong.tan@qq.com
 * @Date: 2017/1/18
 * @Version: 1.0
 * @Description:
 */
public interface ICQCache {

    //<T> SQLParser<T> getSqlParserMap(Class entityType);

    //public abstract LoadingCache<Class<T>, IndexedCollection<T>> getCollectionLoadingCache();

    <T> IndexedCollection<T> getCachedByType(Class entityType) throws ExecutionException;

}
