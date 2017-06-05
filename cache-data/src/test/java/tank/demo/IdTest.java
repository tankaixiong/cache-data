package tank.demo;

import org.junit.Test;
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
                    System.out.println(IdGenerator.nextId());
                }
            }.start();
        }

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }
}
