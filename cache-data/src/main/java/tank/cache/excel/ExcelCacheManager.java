package tank.cache.excel;

import com.googlecode.cqengine.ConcurrentIndexedCollection;
import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.attribute.ReflectiveAttribute;
import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.parser.sql.SQLParser;
import com.googlecode.cqengine.resultset.ResultSet;
import tank.cache.db.exception.CacheException;
import tank.excel.entity.ExcelEntityDemo;
import tank.excel.parse.ExcelParser;

import java.beans.Transient;
import java.lang.reflect.Field;
import java.util.*;

/**
 * @Author: tank
 * @Email: kaixiong.tan@qq.com
 * @Date: 2016/12/30
 * @Version: 1.0
 * @Description: 缓存EXCEL数据
 */
public class ExcelCacheManager {

    private static Map<Class, IndexedCollection> excelCollMap = new HashMap<>();

    static {
        ExcelParser.getInstance().scanPackage(new String[]{"tank.excel.entity"}, "/excel");

        Map<Class, List> listMap = ExcelParser.getExcelMap();

        for (Map.Entry<Class, List> entry : listMap.entrySet()) {

            IndexedCollection clazzColl = new ConcurrentIndexedCollection<>();
            clazzColl.addAll(entry.getValue());

            excelCollMap.put(entry.getKey(), clazzColl);
        }

    }

    public static <T> IndexedCollection<T> getIndexedCollection(Class<T> tClass) {
        return excelCollMap.get(tClass);
    }

    public static <T> ResultSet<T> find(Class<T> tClass, Query<T> query) {
        if (!excelCollMap.containsKey(tClass)) {
            throw new CacheException("没有找到相应类型的缓存数据" + tClass);
        }
        return excelCollMap.get(tClass).retrieve(query);
    }

    public static <T> IndexedCollection<T> findAll(Class<T> tClass) {
        if (!excelCollMap.containsKey(tClass)) {
            throw new CacheException("没有找到相应类型的缓存数据" + tClass);
        }
        return excelCollMap.get(tClass);
    }

    public static <T> T findOne(Class<T> tClass, Query<T> query) {
        ResultSet<T> resultSet = find(tClass, query);
        if (resultSet.size() == 1) {
            return resultSet.uniqueResult();
        } else {
            throw new CacheException("找到非唯一记录:" + resultSet.size());
        }

    }


    public static void main(String[] args) {

        ExcelParser.getInstance().scanPackage(new String[]{"tank.excel.entity"}, "/excel");

        Map<Class, List> listMap = ExcelParser.getExcelMap();
        System.out.println(listMap);

        //集合
        IndexedCollection<ExcelEntityDemo> testColl = new ConcurrentIndexedCollection<>();

        SQLParser<ExcelEntityDemo> parser = SQLParser.forPojo(ExcelEntityDemo.class);

        Field[] fields = ExcelEntityDemo.class.getDeclaredFields();
        for (Field f : fields) {
            if (f.getAnnotation(Transient.class) == null) {

                Attribute attr = new ReflectiveAttribute<>(ExcelEntityDemo.class, f.getType(), f.getName());//注册字段为必须，且类型为引用类型

                //Class fieldWrapClazz=Primitives.wrap(f.getType());
                //Attribute attr=new ReflectiveAttribute<>(Test.class,fieldWrapClazz,f.getName());

                parser.registerAttribute(attr);
            }
        }

        List<ExcelEntityDemo> testList = listMap.get(ExcelEntityDemo.class);
        testColl.addAll(testList);


        //String hql="select * from Test where roleID=101";
        String hql = "select * from Test where test3=true";

        ResultSet<ExcelEntityDemo> result = parser.retrieve(testColl, hql);
        for (ExcelEntityDemo test : result) {
            System.out.println(test.getRoleID() + " " + test.getTest3());
        }


    }
}
