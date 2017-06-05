package tank.cache.provider;

import org.apache.poi.ss.formula.functions.T;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tank.cache.db.EntityUtils;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

/**
 * @Author: tank
 * @Email: kaixiong.tan@qq.com
 * @Date: 2017/1/14
 * @Version: 1.0
 * @Description: hibernate 接口操作数据库数据
 */
@Component
//@Transactional
public class HibernateProvider implements IDataProvider {
    @Resource
    protected SessionFactory sessionFactory;


    protected Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    @Transactional(readOnly = true)
    public <T> List<T> findAll(Class<T> clazz) {
        return getSession().createQuery(" FROM " + clazz.getSimpleName()).list();
    }

    @Override
    @Transactional(readOnly = true)
    public List<T> findByRoleId(Class<T> clazz, Serializable roleId) {
//        String roleFieldName = null;
//        Field fields[] = clazz.getDeclaredFields();
//        for (Field field : fields) {
//            field.setAccessible(true);
//            RoleId roleIdAnno = field.getAnnotation(RoleId.class);
//            if (roleIdAnno != null) {
//                roleFieldName = field.getName();
//                break;
//            }
//        }
//        if (roleFieldName == null) {
//            throw new RuntimeException("没有找到roleId 注解字段");
//        }

        String roleFieldName = EntityUtils.getEntityMeta(clazz).getRoleField();

        return getSession().createQuery(" FROM " + clazz.getSimpleName() + " WHERE " + roleFieldName + "=" + roleId).list();

    }

    @Override
    @Transactional
    public void update(Object entity) {
        getSession().update(entity);
    }

    @Override
    @Transactional
    public void delete(Object entity) {
        getSession().delete(entity);
    }

    @Override
    @Transactional
    public void add(Object entity) {
        getSession().save(entity);
    }
}
