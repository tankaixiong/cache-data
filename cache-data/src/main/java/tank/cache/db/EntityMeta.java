package tank.cache.db;

import com.esotericsoftware.reflectasm.MethodAccess;

/**
 * @Author: tank
 * @Email: kaixiong.tan@qq.com
 * @Date: 2017/5/22
 * @Version: 1.0
 * @Description:
 */
public class EntityMeta {

    private MethodAccess methodAccess;

    private boolean isCacheRole = false;
    private boolean isCacheGlobal = false;

    private String idField;
    private String roleField;

    public String getIdField() {
        return idField;
    }

    public void setIdField(String idField) {
        this.idField = idField;
    }

    public String getRoleField() {
        return roleField;
    }

    public void setRoleField(String roleField) {
        this.roleField = roleField;
    }

    public boolean isCacheRole() {
        return isCacheRole;
    }

    public void setIsCacheRole(boolean isCacheRole) {
        this.isCacheRole = isCacheRole;
    }

    public boolean isCacheGlobal() {
        return isCacheGlobal;
    }

    public void setIsCacheGlobal(boolean isCacheGlobal) {
        this.isCacheGlobal = isCacheGlobal;
    }

    public MethodAccess getMethodAccess() {
        return methodAccess;
    }

    public void setMethodAccess(MethodAccess methodAccess) {
        this.methodAccess = methodAccess;
    }
}
