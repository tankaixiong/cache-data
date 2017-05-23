package tank.cache.db.anno;

import java.lang.annotation.*;

/**
 * @Author: tank
 * @Email: kaixiong.tan@qq.com
 * @Date: 2017/5/22
 * @Version: 1.0
 * @Description: 注释应用在domain上，用于加载domain对象到缓存中。与CacheRole 区别在于 CacheGlobal 可进行全局查询finaAll与全局统计相关查询。一般作用于role 表。
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
public @interface CacheGlobal {
}
