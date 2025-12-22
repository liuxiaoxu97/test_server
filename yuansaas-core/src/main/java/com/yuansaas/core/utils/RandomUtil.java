package com.yuansaas.core.utils;

import com.yuansaas.core.utils.id.SnowflakeIdGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 随机数工具类
 *
 * @author HTB 2025/8/26 18:39
 */
@Component
@RequiredArgsConstructor
public class RandomUtil {

    private final SnowflakeIdGenerator snowflakeIdGenerator;
    private final SecureRandom SECURE_RANDOM = new SecureRandom();


    /**
     * 使用雪花算法生成唯一ID
     * @return 雪花算法生成的long类型ID
     */
    public long generateSnowflakeId() {
        return snowflakeIdGenerator.nextId();
    }

    /**
     * 使用雪花算法生成唯一ID
     * @return 雪花算法生成的String类型ID
     */
    public String generateSnowflakeIdString() {
        return String.valueOf(snowflakeIdGenerator.nextId());
    }

    /**
     * 生成高强度的int随机数（避免重复）
     * @return 随机int值（0到2,147,483,647）
     */
    public int generateRandomInt() {
        // 使用毫秒和纳秒时间戳
        long timestamp = System.currentTimeMillis();
        long nanoTime = System.nanoTime();

        // 组合多个随机源
        long combined = (timestamp % 1000000L) * 1000000L +
                (nanoTime % 1000000L) * 1000L +
                ThreadLocalRandom.current().nextInt(1000);

        // 确保在int范围内
        return (int) (Math.abs(combined) % Integer.MAX_VALUE);
    }

    /**
     * 使用加密安全的随机数生成器
     * @return 安全的随机int
     */
    public int generateSecureRandomInt() {
        // 生成4字节随机数
        byte[] bytes = new byte[4];
        SECURE_RANDOM.nextBytes(bytes);

        // 转换为int
        int result = 0;
        for (byte b : bytes) {
            result = (result << 8) | (b & 0xFF);
        }

        // 确保正数
        return result & Integer.MAX_VALUE;
    }

    /**
     * 生成指定范围内的随机int
     * @param min 最小值（包含）
     * @param max 最大值（包含）
     * @return 范围内的随机int
     */
    public int generateRandomIntInRange(int min, int max) {
        if (min >= max) {
            throw new IllegalArgumentException("最大值必须大于最小值");
        }
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    /**
     * 生成当前时间的格式化字符串（YYYYMMDDHHMMSS）
     * @return 当前时间的格式化字符串
     */
    public String generateTimestampString() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
    }

    /**
     * 生成固定长度的随机数字字符串
     * @param length 字符串长度
     * @return 随机数字字符串
     */
    public String generateRandomNumberString(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("长度必须大于0");
        }

        ThreadLocalRandom random = ThreadLocalRandom.current();
        StringBuilder sb = new StringBuilder(length);

        // 首位数避免为0
        sb.append(random.nextInt(1, 10));

        // 生成剩余数字
        for (int i = 1; i < length; i++) {
            sb.append(random.nextInt(0, 10));
        }

        return sb.toString();
    }

    /**
     * 生成时间戳ID（时间戳+随机数）
     * @return 时间戳ID字符串
     */
    public String generateTimestampId() {
        String timestamp = generateTimestampString();
        int random = ThreadLocalRandom.current().nextInt(10000, 100000);
        return timestamp + random;
    }

    /**
     * 生成带前缀的雪花ID
     * @param prefix 前缀字符串
     * @return 带前缀的雪花ID
     */
    public String generatePrefixedSnowflakeId(String prefix) {
        return prefix + snowflakeIdGenerator.nextId();
    }


    /**
     * 生成简化的UUID（不带连字符）
     * @return 32字符的UUID
     */
    public String generateSimpleUuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
