package tank.demo;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import org.junit.After;
import org.junit.Test;
import org.springframework.stereotype.Component;
import tank.common.BaseJunit;
import tank.event.common.EventPublisher;
import tank.event.entity.DemoEvent;
import tank.event.entity.LoginEvent;

import javax.annotation.Resource;

/**
 * @Author: tank
 * @Email: kaixiong.tan@qq.com
 * @Date: 2017/6/5
 * @Version: 1.0
 * @Description: 测试事件机制
 */
@Component
public class EventTest extends BaseJunit {

    @Resource
    private EventPublisher publisher;

    @Test
    public void testEvent() {

        publisher.fireEvent(new LoginEvent(212L, "test"));
        publisher.fireEvent(new DemoEvent());
    }

    @AllowConcurrentEvents
    @Subscribe
    public void lister(LoginEvent event) {

        System.out.println(event);
    }

    @After
    public void after() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
