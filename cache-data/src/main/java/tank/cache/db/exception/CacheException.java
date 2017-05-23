package tank.cache.db.exception;

/**
 * @Author: tank
 * @Email: kaixiong.tan@qq.com
 * @Date: 2017/5/23
 * @Version: 1.0
 * @Description: 操作数据缓存所对象的异常
 */
public class CacheException extends RuntimeException {
    public CacheException(String message) {
        super(message);
    }
}
