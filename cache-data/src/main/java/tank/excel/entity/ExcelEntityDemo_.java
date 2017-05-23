
package tank.excel.entity;


import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.attribute.SimpleNullableAttribute;
import com.googlecode.cqengine.query.option.QueryOptions;
/**
 * Created by auto  
 */
public class ExcelEntityDemo_ extends ExcelEntityDemo {

    

    /**
     * CQEngine attribute for accessing field {@code ExcelEntityDemo.roleID}.
     */
    // Note: For best performance:
    // - if this field cannot be null, replace this SimpleNullableAttribute with a SimpleAttribute
    public static final Attribute<ExcelEntityDemo, Integer> ROLE_ID = new SimpleNullableAttribute<ExcelEntityDemo, Integer>("ROLE_ID") {
        public Integer getValue(ExcelEntityDemo excelentitydemo, QueryOptions queryOptions) { return excelentitydemo.roleID; }
    };
}
