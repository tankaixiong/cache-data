package tank.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author tank
 * @version :0.1
 * @email kaixiong.tan@qq.com
 * @date:2015年8月11日 下午2:38:38
 * @description:线程池管理
 */

public class ThreadPoolManager {
    private static Logger LOG = LoggerFactory.getLogger(ThreadPoolManager.class);
    private static ExecutorService executor;

    static {
        executor = Executors.newCachedThreadPool();
        // 注册关闭钩子==>手动(以保证最后退出)
//        Runtime.getRuntime().addShutdownHook(new Thread() {
//            @Override
//            public void run() {
//                if (executor != null) {
//                    executor.shutdown();
//                    LOG.info("关闭线程池");
//                }
//            }
//        });

    }

    public static ExecutorService getExecutorService() {
        return executor;
    }

    public static void shutdown() {
        if (executor != null) {
            executor.shutdown();
            LOG.info("关闭线程池");
        }
    }
}
