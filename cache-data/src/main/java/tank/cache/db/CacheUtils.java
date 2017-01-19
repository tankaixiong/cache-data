package tank.cache.db;

import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.attribute.ReflectiveAttribute;
import com.googlecode.cqengine.query.parser.sql.SQLParser;
import org.apache.poi.ss.formula.functions.T;

import java.beans.Transient;
import java.lang.reflect.Field;

/**
 * @Author: tank
 * @Email: kaixiong.tan@qq.com
 * @Date: 2017/1/5
 * @Version: 1.0
 * @Description:
 */
public class CacheUtils {
    public static SQLParser<T> registerAttribute(Class<T>key){


        SQLParser<T> parser = SQLParser.forPojo(key);

        Field[] fields = key.getDeclaredFields();
        for (Field f : fields) {
            if (f.getAnnotation(Transient.class) == null) {

                Attribute attr = new ReflectiveAttribute<>(key, f.getType(), f.getName());//注册字段为必须，且类型为引用类型

                //Class fieldWrapClazz=Primitives.wrap(f.getType());
                //Attribute attr=new ReflectiveAttribute<>(Test.class,fieldWrapClazz,f.getName());

                parser.registerAttribute(attr);
            }
        }


        return parser;
    }
}
