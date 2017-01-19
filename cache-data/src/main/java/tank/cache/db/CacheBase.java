package tank.cache.db;

import tank.cache.provider.IDataProvider;

/**
 * @Author: tank
 * @Email: kaixiong.tan@qq.com
 * @Date: 2017/1/5
 * @Version: 1.0
 * @Description:
 */
@Deprecated
public class CacheBase {

    private IDataProvider dataProvider;

    public void setDataProvider(IDataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }

    public IDataProvider getDataProvider() {
        return dataProvider;
    }


}
