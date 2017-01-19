package tank.service;

import org.springframework.stereotype.Service;
import tank.domain.Demo;
import tank.repository.IDemoRepository;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: tank
 * @Email: kaixiong.tan@qq.com
 * @Date: 2017/1/16
 * @Version: 1.0
 * @Description:
 */
@Service
public class DemoService {

    @Resource
    private IDemoRepository demoRepository;

    public List<Demo> find() {
        //return demoRepository.find("select * from Demo where roleId>5000 ");
        return demoRepository.find("select * from Demo   ");
    }
    public void add(Demo demo){
        demoRepository.add(demo);
    }
}
