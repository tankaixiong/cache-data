package tank.cache.db;

import com.googlecode.cqengine.query.parser.sql.SQLParser;
import org.apache.poi.ss.formula.functions.T;
import tank.cache.provider.IDataProvider;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: tank
 * @Email: kaixiong.tan@qq.com
 * @Date: 2017/1/5
 * @Version: 1.0
 * @Description: 数据库对像缓存
 */
//@Component
public class DBCacheManager  {

    //@Resource
    private IDataProvider dataProvider;


    //两种策略，1，一表一个 IndexedCollection（class==>collection）；2，以roleId关联为一个IndexedCollection(roleId==>class==>collection)
    private static Map<Class<T>, SQLParser<T>> sqlParserMap = new HashMap<>();


    private static DBCacheManager dbCacheManager = new DBCacheManager();


    private GlobalCacheEntity globalCacheEntity=new GlobalCacheEntity();//全局缓存集合
    private Map<Serializable,RoleCacheEntity> roleCacheEntityMap=new HashMap<>();//ROLE 为中心的缓存集合

    private DBCacheManager() {

    }

    public static DBCacheManager getInstance() {
        return dbCacheManager;
    }

    public void init() {
        PersistentChecker.getInstant().start();
    }

    public void stop() {
        PersistentChecker.getInstant().exit(true);
    }

    public void registerParser(Class<T> clazz){
        if(!sqlParserMap.containsKey(clazz)){
            SQLParser<T> sqlParser = CacheUtils.registerAttribute(clazz);//属性绑定SQLParser
            sqlParserMap.put(clazz, sqlParser);
        }
        //sqlParserMap.put(clazz,parser);
    }

    public <T> SQLParser<T> getSqlParserMap(Class entityType) {
        SQLParser<T> parser = (SQLParser<T>) sqlParserMap.get(entityType);
        return parser;
    }

    public void setDataProvider(IDataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }

    public IDataProvider getDataProvider() {
        return dataProvider;
    }

    public GlobalCacheEntity getGlobalCacheEntity() {
        return globalCacheEntity;
    }

//    public Map<Serializable, RoleCacheEntity> getRoleCacheEntityMap() {
//        return roleCacheEntityMap;
//    }
    public  RoleCacheEntity getRoleCacheEntity(Serializable roleId) {
        RoleCacheEntity roleCacheEntity = roleCacheEntityMap.get(roleId);
        if(roleCacheEntity==null){
            roleCacheEntity=new RoleCacheEntity(roleId);
            roleCacheEntityMap.put(roleId,roleCacheEntity);
        }

        return roleCacheEntity;
    }
}
