package tank.excel.entity;

import com.googlecode.cqengine.attribute.SimpleAttribute;
import com.googlecode.cqengine.query.option.QueryOptions;

/**
 * @Author: tank
 * @Email: kaixiong.tan@qq.com
 * @Date: 2017/1/3
 * @Version: 1.0
 * @Description:
 */
public class TestTemplate extends Test{

    public static final  SimpleAttribute roleIdAttr=new SimpleAttribute<Test,Integer>() {

        @Override
        public Integer getValue(Test object, QueryOptions queryOptions) {
            return object.roleID;
        }
    };

}
