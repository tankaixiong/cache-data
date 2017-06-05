package tank.domain;

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import tank.cache.db.anno.CacheRole;
import tank.common.HibernateFieldType;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author: tank
 * @Email: kaixiong.tan@qq.com
 * @Date: 2017/1/14
 * @Version: 1.0
 * @Description:
 */
@Entity
@Table(name = "t_item")
@CacheRole(roleIdField = "roleId")
public class Item {
    @Id
    private Long id;
    //@RoleId
    private Long roleId;
    private Long itemId;
    private String name;
    private Integer num;

    //@Transient
    @Type(type = "tank.common.HibernateJsonType", parameters = {
            @Parameter(name = HibernateFieldType.TYPE, value = HibernateFieldType.LIST),
            @Parameter(name = HibernateFieldType.TYPE_VALUE, value = "java.lang.Integer")
    })
    private List<Integer> subItems;

    @Type(type = "tank.common.HibernateJsonType", parameters = {
            @Parameter(name = HibernateFieldType.TYPE, value = HibernateFieldType.MAP),
            @Parameter(name = HibernateFieldType.TYPE_KEY, value = "java.lang.Integer"),
            @Parameter(name = HibernateFieldType.TYPE_VALUE, value = "java.lang.Integer")
    })
    private Map<Integer, Integer> typeNum;

    @Type(type = "tank.common.HibernateJsonType", parameters = {
            @Parameter(name = HibernateFieldType.TYPE, value = HibernateFieldType.SET),
            @Parameter(name = HibernateFieldType.TYPE_VALUE, value = "java.lang.String")
    })
    private Set<String> alias;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public List<Integer> getSubItems() {
        return subItems;
    }

    public void setSubItems(List<Integer> subItems) {
        this.subItems = subItems;
    }

    public Map<Integer, Integer> getTypeNum() {
        return typeNum;
    }

    public void setTypeNum(Map<Integer, Integer> typeNum) {
        this.typeNum = typeNum;
    }

    public Set<String> getAlias() {
        return alias;
    }

    public void setAlias(Set<String> alias) {
        this.alias = alias;
    }
}
