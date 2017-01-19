package tank.demo;

import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.query.parser.sql.SQLParser;
import com.googlecode.cqengine.resultset.ResultSet;
import org.junit.Test;
import tank.cache.db.DBCacheManager;
import tank.cache.provider.IDataProvider;
import tank.common.BaseJunit;
import tank.domain.Demo;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: tank
 * @Email: kaixiong.tan@qq.com
 * @Date: 2017/1/14
 * @Version: 1.0
 * @Description:
 */
public class DataProviderTest extends BaseJunit {

    @Resource
    private IDataProvider dataProvider;

    @Test
    public void testLoadAll() {
        List<Demo> list = dataProvider.findAll(Demo.class);
        for (Demo demo : list) {
            System.out.println(demo.getName());
        }
    }

    @Test
    public void testAdd() {
        for (int i = 0; i < 10; i++) {
            Demo demo = new Demo();
            demo.setAge(i);
            demo.setRoleId(1000L + i);
            demo.setName("name" + i);

            dataProvider.add(demo);
        }

    }

    @Test
    public void testCacheLoad() {

        DBCacheManager.getInstance().setDataProvider(dataProvider);

        IndexedCollection<Demo> cache = DBCacheManager.getInstance().getGlobalCacheEntity().getCachedByType(Demo.class);

        //System.out.println(cache);


        SQLParser<Demo> demoSQLParser = DBCacheManager.getInstance().getSqlParserMap(Demo.class);

        ResultSet<Demo> demoResultSet = demoSQLParser.retrieve(cache, "select * from Demo ");
        for (Demo demo : demoResultSet) {
            System.out.println(demo.getRoleId());
        }


    }

}
