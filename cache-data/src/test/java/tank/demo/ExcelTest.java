package tank.demo;

import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.attribute.ReflectiveAttribute;
import com.googlecode.cqengine.query.parser.sql.SQLParser;
import com.googlecode.cqengine.resultset.ResultSet;
import org.junit.Test;
import org.springframework.beans.BeanUtils;
import tank.cache.excel.ExcelCacheManager;
import tank.excel.entity.ExcelEntityDemo;
import tank.utils.JsonUtil;

import java.beans.Transient;
import java.lang.reflect.Field;
import java.util.HashSet;

/**
 * @Author: tank
 * @Email: kaixiong.tan@qq.com
 * @Date: 2017/5/23
 * @Version: 1.0
 * @Description:
 */
public class ExcelTest {


    @Test
    public void queryTest() {
        IndexedCollection<ExcelEntityDemo> demoColl = ExcelCacheManager.findAll(ExcelEntityDemo.class);
        HashSet<ExcelEntityDemo> set = new HashSet<>(demoColl);
        for (ExcelEntityDemo excelEntityDemo : set) {
            ExcelEntityDemo temp = new ExcelEntityDemo();
            BeanUtils.copyProperties(excelEntityDemo, temp);
            System.out.println(JsonUtil.toJson(temp));
        }
    }

    @Test
    public void sqlQueryTest() {
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

        IndexedCollection<ExcelEntityDemo> testColl = ExcelCacheManager.getIndexedCollection(ExcelEntityDemo.class);

        //String hql="select * from Test where roleID=101";
        String hql = "select * from Test where test3=true";

        ResultSet<ExcelEntityDemo> result = parser.retrieve(testColl, hql);
        for (ExcelEntityDemo test : result) {
            System.out.println(test.getRoleID() + " " + test.getTest3());
        }

    }
}
