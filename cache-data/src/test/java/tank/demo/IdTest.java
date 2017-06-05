package tank.demo;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tank.utils.IdGenerator;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * @Author: tank
 * @Email: kaixiong.tan@qq.com
 * @Date: 2017/6/5
 * @Version: 1.0
 * @Description:
 */
public class IdTest {

    private Logger LOG = LoggerFactory.getLogger(IdTest.class);

    @Test
    public void testId() {

        final CyclicBarrier cyclicBarrier = new CyclicBarrier(10);

        for (int i = 0; i < 10; i++) {
            new Thread() {
                @Override
                public void run() {
                    try {
                        cyclicBarrier.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (BrokenBarrierException e) {
                        e.printStackTrace();
                    }
                    LOG.info("{}", IdGenerator.nextId());
                }
            }.start();
        }

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testLog() {

        for (int i = 0; i < 10000; i++) {

            LOG.info("{}", "测试日志");
            LOG.error("{}", "测试日志");
        }
    }
}
