package tank.cache.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tank.cache.provider.IDataProvider;
import tank.utils.JsonUtil;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.*;

/**
 * @Author: tank
 * @Email: kaixiong.tan@qq.com
 * @Date: 2017/1/17
 * @Version: 1.0
 * @Description:
 */
//@Component
public class PersistentChecker {
    //@Resource
    //private IDataProvider dataProvider;

    private static final Logger LOGGER = LoggerFactory.getLogger(PersistentChecker.class);
    // offer 添加一个元素并返回true 如果队列已满，则返回false
    // poll 移除并返问队列头部的元素 如果队列为空，则返回null
    //protected Queue<EntityStatus> queue = new ConcurrentLinkedQueue<EntityStatus>();//

    public Map<Serializable, EntityStatus> entityStatusMap = new ConcurrentHashMap<>();
    public List<EntityStatus> changeList = new ArrayList<>();//保存所有的变更

    public static final long PERIOD = 10000;//入库间隔时间MS(最少大于1分钟)
    public static final long CHECK_STATUS = 2000;//当没有状态更新的情况下休眠（时间MS）后继续检查状态
    private volatile boolean isExit = false;

    ExecutorService executorService = Executors.newSingleThreadExecutor();
    Future future = null;
    //当前为单例
    private static PersistentChecker context = new PersistentChecker();

    private PersistentChecker() {
    }

    public static PersistentChecker getInstant() {
        return context;
    }

    //添加对象状态变更
    public void change(Object entity, PersistentStatus status) {
        //queue.offer(new EntityStatus(entity, status));
        changeList.add(new EntityStatus(entity, status));
    }

    public void setIsExit(boolean isExit) {
        this.isExit = isExit;
    }

    public boolean isExit() {
        return isExit;
    }

    public void exit(boolean isExit) {
        setIsExit(isExit);

        try {
            LOGGER.info("等待数据入库...");
            future.get();//等待执行完成

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } finally {
            executorService.shutdown();
        }
    }

    public void start() {


        future = executorService.submit(new Thread("my-cache-persistent") {
            @Override
            public void run() {

                long lastTime = System.currentTimeMillis();

                while (!isExit) {
                    try {
                        int size = changeList.size();

                        LOGGER.info("数据变更总数:{}", size);
                        if (size > 0) {
                            List<EntityStatus> entityStatusList = changeList.subList(0, size);

                            for (EntityStatus entity : entityStatusList) {

                                Serializable entityId = EntityUtils.getUniqueId(entity.getObj());
                                EntityStatus cacheObj = entityStatusMap.get(entityId);
                                if (cacheObj != null) {
                                    PersistentStatus curStatus = getStatus(cacheObj.getStatus(), entity.getStatus());//更新状态
                                    cacheObj.setStatus(curStatus);
                                } else {
                                    entityStatusMap.put(entityId, entity);
                                }

                            }

                            entityStatusList.clear();//这里清除

                        } else {
                            Thread.sleep(CHECK_STATUS);
                        }

//                        //读取变更队列===》累积缓冲变更==》定时入库
//                        EntityStatus queueObj = queue.poll();
//                        if (queueObj != null) {
//                            Serializable entityId = EntityUtils.getUniqueId(queueObj.getObj());
//                            EntityStatus cacheObj = entityStatusMap.get(entityId);
//                            if (cacheObj != null) {
//                                PersistentStatus curStatus = getStatus(cacheObj.getStatus(), queueObj.getStatus());//更新状态
//                                cacheObj.setStatus(curStatus);
//                            } else {
//                                entityStatusMap.put(entityId, queueObj);
//                            }
//
//                        } else {
//                            Thread.sleep(CHECK_STATUS);
//                        }

                        if ((System.currentTimeMillis() - lastTime) >= PERIOD) {

                            persistent();//实现真正入库

                            lastTime = System.currentTimeMillis();
                        }
                    } catch (Exception e) {
                        LOGGER.error("{}", e);
                    }
                }
                //退出后，入库
                persistent();
            }
        });


    }

    public void persistent() {
        IDataProvider dataProvider = DBCacheManager.getInstance().getDataProvider();//

        Iterator<EntityStatus> iterator = entityStatusMap.values().iterator();
        while (iterator.hasNext()) {

            EntityStatus status = iterator.next();
            try {
                if (status.getStatus() == PersistentStatus.ADD) {
                    dataProvider.add(status.getObj());
                } else if (status.getStatus() == PersistentStatus.UPDATE) {
                    dataProvider.update(status.getObj());
                } else if (status.getStatus() == PersistentStatus.DEL) {
                    dataProvider.delete(status.getObj());
                } else if (status.getStatus() == PersistentStatus.NULL) {
                    LOGGER.info("忽略");
                }

                iterator.remove();//移除

            } catch (Exception e) {
                LOGGER.error("入库实体:{}", JsonUtil.toJson(status.getObj()));
                LOGGER.error("入库异常:{}", e);
            }
        }
    }

    public PersistentStatus getStatus(PersistentStatus old, PersistentStatus current) {
        PersistentStatus status = old;

        if (old == PersistentStatus.ADD) {
            if (current == PersistentStatus.DEL) {
                status = PersistentStatus.NULL;
            }
        } else if (old == PersistentStatus.UPDATE) {
            if (current == PersistentStatus.DEL) {
                status = PersistentStatus.DEL;
            }
        }

        return status;
    }


}
