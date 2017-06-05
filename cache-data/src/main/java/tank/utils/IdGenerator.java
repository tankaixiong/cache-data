/*
 * Copyright 2002-2016 XianYu Game Co. Ltd, The Inuyasha Project
 */

package tank.utils;

import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import static com.google.common.base.Preconditions.checkState;

/**
 * 该类用于生产全局唯一ID
 * 具体算法来自：@link https://github.com/twitter/snowflake
 */
public class IdGenerator {
    private static final Logger LOG = LoggerFactory.getLogger(IdGenerator.class);

    private static long workerId = 1;
    private static long sequence = 0L;
    private static long workerIdBits = 12L;
    private static long maxWorkerId = ~(-1L << workerIdBits);
    private static long sequenceBits = 12L;

    private static long workerIdShift = sequenceBits;
    private static long timestampLeftShift = sequenceBits + workerIdBits;
    private static long sequenceMask = ~(-1L << sequenceBits);

    private static long lastTimestamp = -1L;

    static {
        workerId = getWorkerId();

        LOG.info(String.format("[IdGenerator] 时间戳左移位数:%d,  服务器ID位数：%d, 流水号位数：%d, 服务器ID:%d",
                timestampLeftShift, workerIdBits, sequenceBits, workerId));
    }

    private static int getWorkerId() {
        // 优先读取系统属性中的"idGenerator.workerId"，如果没有则继续查找有没有定义“server.id"
        String workerId = System.getProperty("idGenerator.workerId", System.getProperty("server.id"));

        // 如果系统属性中没有查找到相关设置，则尝试从配置表中读取
        if (workerId == null) {
            // 先检索“idGenerator.properties”
            URL url = IdGenerator.class.getResource("/idGenerator.properties");
            if (url == null) {
                LOG.debug("[IdGenerator] Could NOT find resource [idGenerator.properties], will search [server.properties]");

                // 如果还找不到就查找“server.properties”
                url = IdGenerator.class.getResource("/server.properties");
                if (url == null) {
                    LOG.debug("[IdGenerator] Could NOT find resource：[server.properties]");
                } else {
                    LOG.debug("[IdGenerator] Find resource：[server.properties]");

                    Properties properties = getProperties("/server.properties");
                    workerId = properties.getProperty("server.id");
                }
            } else {
                LOG.debug("[IdGenerator] Find resource：[idGenerator.properties]");

                Properties properties = getProperties("/idGenerator.properties");
                workerId = properties.getProperty("server.id", "1");
            }
        }

        if (Strings.isNullOrEmpty(workerId)) {
            workerId = "1";
        }

        int id = Integer.parseInt(workerId);

        checkState(id <= maxWorkerId, "workerId必须小于或等于" + maxWorkerId + ", 当前值：" + id);

        return id;
    }

    private static Properties getProperties(String file) {
        Properties properties = new Properties();
        InputStream in = null;
        try {
            in = IdGenerator.class.getResourceAsStream(file);
            properties.load(in);
        } catch (Exception e) {
            LOG.error("读取配置文件异常: {}", e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                LOG.error("{}", e);
            }
        }
        return properties;
    }

    public synchronized static long nextId() {
        long timestamp = timeGen();

        if (timestamp < lastTimestamp) {
            LOG.error(String.format("clock is moving backwards.  Rejecting requests until %d.", lastTimestamp));
            throw new RuntimeException(String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
        }

        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & sequenceMask;
            if (sequence == 0) {
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0L;
        }

        lastTimestamp = timestamp;

        long twepoch = 1288834974657L;
        return ((timestamp - twepoch) << timestampLeftShift) | (workerId << workerIdShift) | sequence;
    }

    protected static long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    protected static long timeGen() {
        return System.currentTimeMillis();
    }

    public static void main(String[] args) {
        Set<Long> total = new HashSet<>();
        for (int i = 1; i <= 100; i++) {
            Set<Long> ids = new HashSet<>();
            for (int j = 0; j < 1_000_000; j++) {
                long id = IdGenerator.nextId();
                if (id < 0) {
                    System.err.println("error id:" + id);
                }
                ids.add(id);
            }
            System.out.println("spaceId:" + i + "+_" + ids.size());
            total.addAll(ids);
        }
        System.out.println(total.size());
    }

    private static void batchGenId(final int serverId, final Set<Long> ids) {
        IdGenerator.workerId = serverId;
        for (int i = 0; i < 10000; i++) {
            long id = IdGenerator.nextId();
            System.out.println(id);
            ids.add(id);
        }
    }
}