package tank;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * @Author: tank
 * @Email: kaixiong.tan@qq.com
 * @Date: 2017/3/9
 * @Version: 1.0
 * @Description:
 */
public class TestList {
    public static void main(String[] args) {

        final CyclicBarrier barrier = new CyclicBarrier(20);

        final List<Integer> list = new ArrayList<>();

        for (int i = 0; i < 10; i++) {

            new Thread() {
                @Override
                public void run() {

                    try {
                        barrier.await();
                        System.out.println("--add--");
                        list.add(123);


                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (BrokenBarrierException e) {
                        e.printStackTrace();
                    }
                }
            }.start();

            new Thread() {
                @Override
                public void run() {

                    try {
                        barrier.await();
                        System.out.println("--clear--");
                        list.subList(0,list.size()).clear();


                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (BrokenBarrierException e) {
                        e.printStackTrace();
                    }
                }
            }.start();

        }
    }
}
