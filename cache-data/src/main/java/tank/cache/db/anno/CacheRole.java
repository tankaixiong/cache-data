package tank.cache.db.anno;

import java.lang.annotation.*;

/**
 * @Author: tank
 * @Email: kaixiong.tan@qq.com
 * @Date: 2017/5/22
 * @Version: 1.0
 * @Description: 注释应用在domain上，用于加载domain对象到缓存中。与CacheGlobal区别在于CacheRole依赖roleId查询，无全局查询finaAll与全局统计相关查询。
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
public @interface CacheRole {
    /**
     * roleId 关联的字段名
     *
     * @return
     */
    String roleIdField() default "roleId";
}
