package tank.cache.db;

import com.googlecode.cqengine.resultset.ResultSet;
import org.apache.poi.ss.formula.functions.T;

/**
 * @Author: tank
 * @Email: kaixiong.tan@qq.com
 * @Date: 2017/1/5
 * @Version: 1.0
 * @Description:
 */
public class RoleCacheManager {

    public ResultSet<T> find(Class<T> clazz, String query) {
//        CacheBase cacheBase = null;
//        Map<Class<T>, SQLParser<T>> map = cacheBase.getSqlParserMap();
//        SQLParser sqlParser = map.get(clazz);
//
//        try {
//            IndexedCollection indexedCollection = cacheBase.getCollectionLoadingCache().get(clazz);
//
//            return sqlParser.retrieve(indexedCollection, query);
//
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }
        return null;
    }
}
