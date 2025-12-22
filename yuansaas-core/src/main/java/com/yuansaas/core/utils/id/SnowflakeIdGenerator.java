package com.yuansaas.core.utils.id;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.time.Instant;
import java.util.Enumeration;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 全局唯一ID生成器（雪花算法实现）
 * ID结构（64位）：
 * | 1位符号位 | 41位时间戳 | 5位数据中心ID | 5位机器ID | 12位序列号 |
 * 特性：
 * 1. 毫秒级生成最多4096个ID
 * 2. 自动根据IP生成机器ID（确保分布式唯一）
 * 3. 解决系统时钟回拨问题
 * 4. 支持分布式部署
 *
 * @author HTB 2025/7/31 15:31
 */
@Component
public class SnowflakeIdGenerator implements InitializingBean {

    // 配置默认值（可通过Spring配置覆盖）
    // 数据中心ID
    @Value("${snowflake.datacenter-id:-1}")
    private long dataCenterId;

    // 机器ID
    @Value("${snowflake.machine-id:-1}")
    private long machineId;

    // 时间起始标记点（2024-01-01 00:00:00）
    private static final long TWEPOCH = 1704038400000L;

    // 机器ID位数
    private static final long MACHINE_ID_BITS = 5L;

    // 数据中心ID位数
    private static final long DATACENTER_ID_BITS = 5L;

    // 最大机器ID
    private static final long MAX_MACHINE_ID = ~(-1L << MACHINE_ID_BITS);

    // 最大数据中心ID
    private static final long MAX_DATACENTER_ID = ~(-1L << DATACENTER_ID_BITS);

    // 序列号位数
    private static final long SEQUENCE_BITS = 12L;

    // 机器ID左移位数
    private static final long MACHINE_ID_SHIFT = SEQUENCE_BITS;

    // 数据中心ID左移位数
    private static final long DATACENTER_ID_SHIFT = SEQUENCE_BITS + MACHINE_ID_BITS;

    // 时间戳左移位数
    private static final long TIMESTAMP_SHIFT = SEQUENCE_BITS + MACHINE_ID_BITS + DATACENTER_ID_BITS;

    // 序列号掩码（4095）
    private static final long SEQUENCE_MASK = ~(-1L << SEQUENCE_BITS);

    // 上一次时间戳
    private volatile long lastTimestamp = -1L;

    // 序列号
    private final AtomicLong sequence = new AtomicLong(0);

    // 最大时钟回拨容忍（10毫秒）
    private static final long MAX_CLOCK_BACKWARD_MS = 10;

    @Override
    public void afterPropertiesSet() throws Exception {
        validateAndAssignIds();
        logConfiguration();
    }

    /**
     * 验证并分配ID
     */
    private void validateAndAssignIds() throws Exception {
        // 如果未配置数据中心ID，自动生成
        if (dataCenterId == -1) {
            dataCenterId = generateAutoDatacenterId();
        } else if (dataCenterId > MAX_DATACENTER_ID) {
            throw new IllegalArgumentException(
                    String.format("数据中心ID不能超过最大值 %d", MAX_DATACENTER_ID));
        }

        // 如果未配置机器ID，自动生成
        if (machineId == -1) {
            machineId = generateAutoMachineId();
        } else if (machineId > MAX_MACHINE_ID) {
            throw new IllegalArgumentException(
                    String.format("机器ID不能超过最大值 %d", MAX_MACHINE_ID));
        }
    }

    /**
     * 生成自动数据中心ID
     */
    private long generateAutoDatacenterId() {
        return Math.abs(getHostName().hashCode()) % (MAX_DATACENTER_ID + 1);
    }

    /**
     * 生成自动机器ID
     */
    private long generateAutoMachineId() throws Exception {
        byte[] mac = getNetworkInterface().getHardwareAddress();
        return ((0x000000FF & (long) mac[mac.length - 1]) |
                (0x0000FF00 & (((long) mac[mac.length - 2]) << 8))) >> 4;
    }

    /**
     * 获取首选网络接口
     */
    private NetworkInterface getNetworkInterface() throws Exception {
        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
        NetworkInterface selectedInterface = null;
        while (interfaces.hasMoreElements()) {
            NetworkInterface ni = interfaces.nextElement();
            if (!ni.isLoopback() && ni.isUp() && ni.getHardwareAddress() != null) {
                selectedInterface = ni;
                if (!ni.getName().contains("docker")) break;
            }
        }
        if (selectedInterface == null) {
            throw new RuntimeException("未找到合适的网络接口");
        }
        return selectedInterface;
    }

    /**
     * 获取主机名
     */
    private String getHostName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (Exception e) {
            return "UNKNOWN_HOST";
        }
    }

    /**
     * 打印配置信息
     */
    private void logConfiguration() {
        System.out.println("[Snowflake] 配置信息 => " +
                "数据中心ID: " + dataCenterId +
                ", 机器ID: " + machineId +
                ", 序列掩码: " + SEQUENCE_MASK);
    }

    /**
     * 生成下一个ID
     */
    public synchronized long nextId() {
        long currentTimestamp = System.currentTimeMillis();

        // 处理时钟回拨
        if (currentTimestamp < lastTimestamp) {
            handleClockBackwards(currentTimestamp);
        }

        // 同一毫秒内生成序列号
        if (lastTimestamp == currentTimestamp) {
            long seq = sequence.incrementAndGet() & SEQUENCE_MASK;
            // 序列号用尽时等待下一毫秒
            if (seq == 0) {
                currentTimestamp = waitNextMillis();
            } else {
                return combineParts(currentTimestamp, seq);
            }
        }
        // 新毫秒重置序列号
        else {
            sequence.set(0);
        }

        lastTimestamp = currentTimestamp;
        return combineParts(currentTimestamp, 0);
    }

    /**
     * 组合各部分生成ID
     */
    private long combineParts(long timestamp, long sequence) {
        return ((timestamp - TWEPOCH) << TIMESTAMP_SHIFT) |
                (dataCenterId << DATACENTER_ID_SHIFT) |
                (machineId << MACHINE_ID_SHIFT) |
                sequence;
    }

    /**
     * 处理时钟回拨
     */
    private void handleClockBackwards(long currentTimestamp) {
        long timeDiff = lastTimestamp - currentTimestamp;
        if (timeDiff > MAX_CLOCK_BACKWARD_MS) {
            throw new IllegalStateException(
                    "检测到时钟回拨！拒绝生成ID。回拨时间: " + timeDiff + "ms");
        }

        try {
            Thread.sleep(timeDiff);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * 等待下一毫秒
     */
    private long waitNextMillis() {
        long now = System.currentTimeMillis();
        while (now <= lastTimestamp) {
            now = System.currentTimeMillis();
        }
        return now;
    }

    /**
     * 反解析ID
     */
    public IdParts parseId(long id) {
        long timestamp = (id >> TIMESTAMP_SHIFT) + TWEPOCH;
        long datacenterId = (id >> DATACENTER_ID_SHIFT) & MAX_DATACENTER_ID;
        long machineId = (id >> MACHINE_ID_SHIFT) & MAX_MACHINE_ID;
        long sequence = id & SEQUENCE_MASK;
        return new IdParts(timestamp, datacenterId, machineId, sequence);
    }

    /**
     * ID结构分析结果
     */
    public static class IdParts {
        private final long timestamp;
        private final long datacenterId;
        private final long machineId;
        private final long sequence;

        public IdParts(long timestamp, long datacenterId, long machineId, long sequence) {
            this.timestamp = timestamp;
            this.datacenterId = datacenterId;
            this.machineId = machineId;
            this.sequence = sequence;
        }

        @Override
        public String toString() {
            return String.format(
                    "ID结构{timestamp=%d [%s], datacenterId=%d, machineId=%d, sequence=%d}",
                    timestamp, Instant.ofEpochMilli(timestamp).toString(), datacenterId, machineId, sequence
            );
        }
    }

}
