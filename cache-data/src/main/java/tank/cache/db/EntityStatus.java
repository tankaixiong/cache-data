package tank.cache.db;

/**
 * @Author: tank
 * @Email: kaixiong.tan@qq.com
 * @Date: 2017/1/17
 * @Version: 1.0
 * @Description:
 */
public class EntityStatus {

    public PersistentStatus status;

    public Object obj;

    public EntityStatus() {
    }

    public EntityStatus(Object obj, PersistentStatus status) {
        this.status = status;
        this.obj = obj;
    }

    public PersistentStatus getStatus() {
        return status;
    }

    public void setStatus(PersistentStatus status) {
        this.status = status;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }
}
