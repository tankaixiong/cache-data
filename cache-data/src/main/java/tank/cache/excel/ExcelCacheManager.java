package tank.cache.excel;

import com.googlecode.cqengine.ConcurrentIndexedCollection;
import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.attribute.ReflectiveAttribute;
import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.parser.sql.SQLParser;
import com.googlecode.cqengine.resultset.ResultSet;
import tank.excel.entity.Test;
import tank.excel.entity.TestTemplate;
import tank.excel.parse.ExcelParser;

import java.beans.Transient;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import static com.googlecode.cqengine.query.QueryFactory.equal;

/**
 * @Author: tank
 * @Email: kaixiong.tan@qq.com
 * @Date: 2016/12/30
 * @Version: 1.0
 * @Description: 缓存EXCEL数据
 */
public class ExcelCacheManager {

    public static void main(String[] args) {

        ExcelParser.getInstance().scanPackage(new String[]{"tank.excel.entity"}, "/excel");

        Map<Class, List> listMap=ExcelParser.getExcelMap();
        System.out.println(listMap);

        //集合
        IndexedCollection<Test> testColl=new ConcurrentIndexedCollection<>();

        SQLParser<Test> parser=SQLParser.forPojo(Test.class);

        Field[] fields=Test.class.getDeclaredFields();
        for (Field f : fields) {
            if(f.getAnnotation(Transient.class)==null){

                Attribute attr=new ReflectiveAttribute<>(Test.class,f.getType(),f.getName());//注册字段为必须，且类型为引用类型

                //Class fieldWrapClazz=Primitives.wrap(f.getType());
                //Attribute attr=new ReflectiveAttribute<>(Test.class,fieldWrapClazz,f.getName());

                parser.registerAttribute(attr);
            }
        }

        List<Test> testList=listMap.get(Test.class);
        testColl.addAll(testList);


        //String hql="select * from Test where roleID=101";
        String hql="select * from Test where test3=true";

        ResultSet<Test> result=  parser.retrieve(testColl, hql);
        for (Test test : result) {
            System.out.println(test.getRoleID()+" "+test.getTest3());
        }

        Query<Test> query=equal(TestTemplate.roleIdAttr, 101);
        ResultSet<Test> testResultSet= testColl.retrieve(query);
        for (Test test : testResultSet) {
            System.out.println(test.getRoleID()+" "+test.getTest3());
        }

    }
}
