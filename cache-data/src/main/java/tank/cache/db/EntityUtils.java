package tank.cache.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tank.domain.Demo;

import javax.persistence.Id;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: tank
 * @Email: kaixiong.tan@qq.com
 * @Date: 2017/1/18
 * @Version: 1.0
 * @Description:
 */
public class EntityUtils {

    private static Logger LOGGER = LoggerFactory.getLogger(EntityUtils.class);
    //缓存class 中注解为ID 的字段
    private static Map<Class<?>, Field> idFieldMap = new HashMap<>();
    //缓存class 中注解为roleId的字段
    private static Map<Class<?>, Field> roleFieldMap = new HashMap<>();

    //获取唯一ID的值
    public static Serializable getId(Object obj) {

        try {
            Field field = idFieldMap.get(obj.getClass());
            if (field == null) {
                Field fields[] = obj.getClass().getDeclaredFields();
                for (Field f : fields) {
                    f.setAccessible(true);
                    Id id = f.getAnnotation(Id.class);
                    if (id != null) {
                        idFieldMap.put(obj.getClass(), f);
                        return (Serializable) f.get(obj);
                    }
                }
            } else {
                return (Serializable) field.get(obj);
            }


        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        LOGGER.error("没有找到类型:{}的Id字段", obj.getClass());
        return null;
    }

    public static Serializable getRoleId(Object obj) {
        try {
            Field field = roleFieldMap.get(obj.getClass());
            if (field == null) {
                Field fields[] = obj.getClass().getDeclaredFields();
                for (Field f : fields) {
                    f.setAccessible(true);
                    RoleId roleId = f.getAnnotation(RoleId.class);
                    if (roleId != null) {
                        roleFieldMap.put(obj.getClass(), f);
                        return (Serializable) f.get(obj);
                    }
                }
            } else {
                return (Serializable) field.get(obj);
            }


        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        LOGGER.error("没有找到类型:{}的Id字段", obj.getClass());
        return null;
    }


    public static Serializable getUniqueId(Object obj) {
        return obj.getClass().getSimpleName() + getId(obj);
    }

    public static void main(String[] args) {
        Demo demo = new Demo();
        demo.setName("aaa");
        demo.setRoleId(987L);
        demo.setAge(4523);


        Demo demo1 = new Demo();
        demo1.setName("aaa");
        demo1.setRoleId(999L);
        demo1.setAge(4523);

        System.out.println(getId(demo));
        System.out.println(getId(demo1));
        System.out.println(getUniqueId(demo1));

    }
}
