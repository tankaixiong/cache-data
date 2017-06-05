package tank.common;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.usertype.ParameterizedType;
import org.hibernate.usertype.UserType;

import java.io.IOException;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.*;

/**
 * @Author: tank
 * @Email: kaixiong.tan@qq.com
 * @Date: 2017/6/2
 * @Version: 1.0
 * @Description: 自定义hibernate 映射与数据库字段转换关系(支持list,set,map 转josn 存储)
 */
public class HibernateJsonType implements UserType, ParameterizedType {

    private static int[] SQL_TYPE = {Types.LONGVARCHAR};
    private static ObjectMapper objectMapper = new ObjectMapper();
    private Class<?> objClazz;
    private JavaType javaType;

    @Override
    public int[] sqlTypes() {
        return SQL_TYPE;
    }

    @Override
    public Class returnedClass() {
        return objClazz;
    }

    @Override
    public boolean equals(Object x, Object y) throws HibernateException {
        return Objects.equals(x, y);
    }

    @Override
    public int hashCode(Object x) throws HibernateException {
        return Objects.hashCode(x);
    }

    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner) throws HibernateException, SQLException {
        if (rs.wasNull()) {
            return null;
        } else {
            String json = rs.getString(names[0]);
            try {
                if (StringUtils.isNotEmpty(json)) {
                    return objectMapper.readValue(json, javaType);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return null;
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session) throws HibernateException, SQLException {
        if (value == null) {
            st.setNull(index, Types.VARCHAR);
        } else {
            String jsonVal = null;
            try {
                jsonVal = objectMapper.writeValueAsString(value);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            st.setString(index, jsonVal);
        }
    }

    @Override
    public Object deepCopy(Object value) throws HibernateException {

        if (value == null) {
            return null;
        }
        try {
            if (javaType.isCollectionLikeType()) {

                Object copyObj = value.getClass().newInstance();

                Collection collection = (Collection) copyObj;
                collection.addAll((Collection) value);

                return copyObj;

            } else if (javaType.isMapLikeType()) {
                Object copyObj = value.getClass().newInstance();

                Map collection = (Map) copyObj;
                collection.putAll((Map) value);

                return copyObj;
            } else {
                throw new UnsupportedOperationException("不支持COPY类型：" + javaType);
            }

        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public boolean isMutable() {
        return true;
    }

    @Override
    public Serializable disassemble(Object value) throws HibernateException {
        return (Serializable) deepCopy(value);
    }

    @Override
    public Object assemble(Serializable cached, Object owner) throws HibernateException {
        return deepCopy(cached);
    }

    //For mutable objects, it is safe to return a copy of the first parameter.
    @Override
    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return deepCopy(original);
    }


    @Override
    public void setParameterValues(Properties parameters) {

        String type = parameters.getProperty(HibernateFieldType.TYPE);

        if (HibernateFieldType.LIST.equalsIgnoreCase(type)) {
            String valueType = parameters.getProperty(HibernateFieldType.TYPE_VALUE);
            if (valueType != null) {
                try {
                    javaType = objectMapper.getTypeFactory().constructCollectionType(List.class, Class.forName(valueType));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
            objClazz = List.class;

        } else if (HibernateFieldType.MAP.equalsIgnoreCase(type)) {
            String keyType = parameters.getProperty(HibernateFieldType.TYPE_KEY);
            String valueType = parameters.getProperty(HibernateFieldType.TYPE_VALUE);
            if (valueType != null && keyType != null) {
                try {
                    javaType = objectMapper.getTypeFactory().constructMapType(Map.class, Class.forName(keyType), Class.forName(valueType));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                throw new UnsupportedOperationException("参数末设值：key=" + keyType + "value" + valueType);
            }

            objClazz = Map.class;
        } else if (HibernateFieldType.SET.equalsIgnoreCase(type)) {

            String valueType = parameters.getProperty(HibernateFieldType.TYPE_VALUE);
            if (valueType != null) {
                try {
                    javaType = objectMapper.getTypeFactory().constructCollectionType(HashSet.class, Class.forName(valueType));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }


            objClazz = Set.class;
        } else {
            throw new UnsupportedOperationException("不支持转换类型：" + type);
        }


    }
}
