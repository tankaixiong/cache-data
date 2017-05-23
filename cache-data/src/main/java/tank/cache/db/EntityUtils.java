package tank.cache.db;

import com.esotericsoftware.reflectasm.MethodAccess;
import org.apache.commons.lang.StringUtils;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tank.cache.db.anno.CacheGlobal;
import tank.cache.db.anno.CacheRole;
import tank.domain.Demo;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @Author: tank
 * @Email: kaixiong.tan@qq.com
 * @Date: 2017/1/18
 * @Version: 1.0
 * @Description:
 */
public class EntityUtils {

    private static Logger LOGGER = LoggerFactory.getLogger(EntityUtils.class);

    private static Map<Class<?>, EntityMeta> entityMetaMap = new HashMap<>();//缓存实体类MethodAccess等反射相关信息


    static {
        Reflections reflections = new Reflections("tank.domain");//domain对应的包名
        Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(Entity.class);
        for (Class<?> clazz : annotated) {

            EntityMeta entityMeta = new EntityMeta();
            MethodAccess methodAccess = MethodAccess.get(clazz);
            entityMeta.setMethodAccess(methodAccess);


            CacheRole cacheRole = clazz.getAnnotation(CacheRole.class);
            if (cacheRole != null) {
                entityMeta.setIsCacheRole(true);
                entityMeta.setRoleField(cacheRole.roleIdField());
            } else {
                CacheGlobal cacheGlobal = clazz.getAnnotation(CacheGlobal.class);
                if (cacheGlobal != null) {
                    entityMeta.setIsCacheGlobal(true);
                }
            }

            Field fields[] = clazz.getDeclaredFields();
            for (Field f : fields) {
                f.setAccessible(true);
                Id id = f.getAnnotation(Id.class);
                if (id != null) {
                    entityMeta.setIdField(f.getName());
                }
            }

            entityMetaMap.put(clazz, entityMeta);
        }

    }

    public static EntityMeta getEntityMeta(Class clazz) {
        if (!entityMetaMap.containsKey(clazz)) {
            throw new RuntimeException("没有找到注解信息:" + clazz);
        }
        return entityMetaMap.get(clazz);
    }

    public static Serializable getId(Object obj) {

        EntityMeta meta = entityMetaMap.get(obj.getClass());
        if (meta != null) {
            int methodIndex = meta.getMethodAccess().getIndex("get" + StringUtils.capitalize(meta.getIdField()));
            return (Serializable) meta.getMethodAccess().invoke(obj, methodIndex);
        }
        throw new RuntimeException("没有找到Id注解信息:" + obj.getClass());
    }

    public static Serializable getRoleId(Object obj) {
        EntityMeta meta = entityMetaMap.get(obj.getClass());
        if (meta != null) {
            return (Serializable) meta.getMethodAccess().invoke(obj, "get" + StringUtils.capitalize(meta.getRoleField()));
        }
        throw new RuntimeException("没有找到roleId信息:" + obj.getClass());
    }

//    //缓存class 中注解为ID 的字段
//    private static Map<Class<?>, Field> idFieldMap = new HashMap<>();
//    //获取唯一ID的值
//    public static Serializable getId(Object obj) {
//
//        try {
//            Field field = idFieldMap.get(obj.getClass());
//            if (field == null) {
//                Field fields[] = obj.getClass().getDeclaredFields();
//                for (Field f : fields) {
//                    f.setAccessible(true);
//                    Id id = f.getAnnotation(Id.class);
//                    if (id != null) {
//                        idFieldMap.put(obj.getClass(), f);
//                        return (Serializable) f.get(obj);
//                    }
//                }
//            } else {
//                return (Serializable) field.get(obj);
//            }
//
//
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }
//        LOGGER.error("没有找到类型:{}的Id字段", obj.getClass());
//        return null;
//    }
//    //缓存class 中注解为roleId的字段
//    private static Map<Class<?>, Field> roleFieldMap = new HashMap<>();
//    public static Serializable getRoleId(Object obj) {
//        try {
//            Field field = roleFieldMap.get(obj.getClass());
//            if (field == null) {
//                Field fields[] = obj.getClass().getDeclaredFields();
//                for (Field f : fields) {
//                    f.setAccessible(true);
//                    RoleId roleId = f.getAnnotation(RoleId.class);
//                    if (roleId != null) {
//                        roleFieldMap.put(obj.getClass(), f);
//                        return (Serializable) f.get(obj);
//                    }
//                }
//            } else {
//                return (Serializable) field.get(obj);
//            }
//
//
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }
//        LOGGER.error("没有找到类型:{}的Id字段", obj.getClass());
//        return null;
//    }


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
