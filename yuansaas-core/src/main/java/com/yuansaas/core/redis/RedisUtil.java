package com.yuansaas.core.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Redis 工具类
 *
 * @author HTB 2025/8/12 18:26
 */
@Component
@RequiredArgsConstructor
public class RedisUtil {

    /** Redis 操作模板 */
    private static StringRedisTemplate redisTemplate;
    private static RedissonClient redissonClient;

    /** 统一 Jackson ObjectMapper，复用项目已有配置 */
    private static ObjectMapper objectMapper;

    /** 缓存空值占位符，用于防止缓存穿透 */
    private static final String NULL_PLACEHOLDER = "__NULL__";

    /** 空值缓存过期时间（秒） */
    private static final long NULL_EXPIRE_SECONDS = 60;

    /**
     * 构造函数注入 RedisTemplate 和 ObjectMapper，
     * 并赋值给静态变量，保证静态方法可用
     *
     * @param redisTemplate Redis 操作模板
     * @param objectMapper  Jackson 序列化工具
     */
    @Autowired
    public RedisUtil(StringRedisTemplate redisTemplate, ObjectMapper objectMapper , RedissonClient redissonClient) {
        RedisUtil.redisTemplate = redisTemplate;
        RedisUtil.objectMapper = objectMapper;
        RedisUtil.redissonClient = redissonClient;
    }

    // ===================== 基础操作 =====================

    /**
     * 设置缓存，默认不过期
     *
     * @param key   缓存 Key
     * @param value 缓存值
     * @param <T>   泛型类型
     * @return 是否成功
     */
    public static <T> boolean set(String key, T value) {
        return set(key, value, -1, TimeUnit.SECONDS);
    }

    /**
     * 设置缓存，带过期时间
     *
     * @param key     缓存 Key
     * @param value   缓存值
     * @param timeout 过期时间
     * @param unit    时间单位
     * @param <T>     泛型类型
     * @return 是否成功
     */
    public static <T> boolean set(String key, T value, long timeout, TimeUnit unit) {
        try {
            String json = objectMapper.writeValueAsString(value);
            if (timeout > 0) {
                redisTemplate.opsForValue().set(key, json, timeout, unit);
            } else {
                redisTemplate.opsForValue().set(key, json);
            }
            return true;
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Redis序列化失败", e);
        }
    }

    /**
     * 设置缓存，过期时间用 Duration 表示
     *
     * @param key      缓存 Key
     * @param value    缓存值
     * @param duration 过期时间
     * @param <T>      泛型类型
     * @return 是否成功
     */
    public static <T> boolean set(String key, T value, Duration duration) {
        return set(key, value, duration.toMillis(), TimeUnit.MILLISECONDS);
    }

    /**
     * 获取缓存，反序列化成指定类型
     *
     * @param key   缓存 Key
     * @param clazz 目标类型 Class
     * @param <T>   泛型类型
     * @return 缓存对象，缓存不存在或空值返回 null
     */
    public static <T> T get(String key, Class<T> clazz) {
        String json = redisTemplate.opsForValue().get(key);
        if (json == null) return null;
        if (NULL_PLACEHOLDER.equals(json)) return null; // 处理空值占位符
        try {
            return objectMapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Redis反序列化失败", e);
        }
    }
    /**
     * 获取缓存，反序列化成指定类型
     *
     * @param key   缓存 Key
     * @param clazz 目标类型 Class
     * @param <T>   TypeReference
     * @return 缓存对象，缓存不存在或空值返回 null
     */
    public static <T> T get(String key, TypeReference<T> clazz) {
        String json = redisTemplate.opsForValue().get(key);
        if (json == null) return null;
        if (NULL_PLACEHOLDER.equals(json)) return null; // 处理空值占位符
        try {
            return objectMapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Redis反序列化失败", e);
        }
    }

    /**
     * 删除缓存 Key
     *
     * @param key 缓存 Key
     * @return 是否删除成功
     */
    public static boolean delete(String key) {
        return Boolean.TRUE.equals(redisTemplate.delete(key));
    }

    /**
     * 判断缓存 Key 是否存在
     *
     * @param key 缓存 Key
     * @return 是否存在
     */
    public static boolean exists(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    /**
     * 设置缓存过期时间
     *
     * @param key     缓存 Key
     * @param timeout 过期时间
     * @param unit    时间单位
     * @return 是否设置成功
     */
    public static boolean expire(String key, long timeout, TimeUnit unit) {
        return Boolean.TRUE.equals(redisTemplate.expire(key, timeout, unit));
    }

    /**
     * 获取缓存剩余过期时间（单位秒）
     *
     * @param key 缓存 Key
     * @return 剩余时间（秒），-2 表示 Key 不存在
     */
    public static long getExpire(String key) {
        return Optional.of(redisTemplate.getExpire(key, TimeUnit.SECONDS)).orElse(-2L);
    }

    /**
     * 计数器自增
     *
     * @param key   缓存 Key
     * @param delta 增加值（必须大于0）
     * @return 新值
     */
    public static Long increment(String key, long delta) {
        if (delta <= 0) throw new IllegalArgumentException("delta 必须大于0");
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 计数器自减
     *
     * @param key   缓存 Key
     * @param delta 减少值（必须大于0）
     * @return 新值
     */
    public static Long decrement(String key, long delta) {
        if (delta <= 0) throw new IllegalArgumentException("delta 必须大于0");
        return redisTemplate.opsForValue().increment(key, -delta);
    }

    /**
     * 判断缓存中是否存在指定的key
     *
     * @param key 缓存的键
     * @return 如果存在返回true，否则返回false
     */
    public static boolean hasKey(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            // 可以加日志记录异常，避免因Redis异常导致业务中断
            return false;
        }
    }

    // ===================== 高阶缓存操作 =====================

    /**
     * 先从缓存获取，缓存无命中时调用数据库查询方法，查询结果写入缓存并返回
     * 支持空值缓存，防止缓存穿透
     *
     * @param key     缓存 Key
     * @param clazz   目标类型 Class
     * @param dbQuery 数据库查询 Supplier
     * @param timeout 过期时间
     * @param unit    时间单位
     * @param <T>     泛型类型
     * @return 查询结果，可能为 null
     */
    public static <T> T getOrLoad(String key, TypeReference<T> clazz, Supplier<T> dbQuery, long timeout, TimeUnit unit) {
        T value = get(key, clazz);
        if (value != null) return value;

        // 缓存未命中，调用数据库查询
        T dbValue = dbQuery.get();
        if (dbValue != null) {
            set(key, dbValue, timeout, unit);
        } else {
            // 缓存空值，避免缓存穿透
            redisTemplate.opsForValue().set(key, NULL_PLACEHOLDER, NULL_EXPIRE_SECONDS, TimeUnit.SECONDS);
        }
        return dbValue;
    }

    /**
     * getOrLoad 变体，不指定过期时间（默认无过期）
     */
    public static <T> T getOrLoad(String key, TypeReference<T> clazz, Supplier<T> dbQuery) {
        return getOrLoad(key, clazz, dbQuery, -1, TimeUnit.SECONDS);
    }

    /**
     * 批量获取缓存，缺失数据调用批量数据库查询方法补充并缓存
     * 支持空值缓存，防止缓存穿透
     *
     * @param keys     业务对象 key 集合
     * @param dbQuery  批量查询数据库方法，返回 Map<业务key, 值>
     * @param keyMapper 业务 key 转 Redis key 的映射函数
     * @param clazz    目标类型 Class
     * @param timeout  过期时间
     * @param unit     时间单位
     * @param <K>      业务 key 类型
     * @param <V>      业务对象类型
     * @return 缓存 + 数据库查询的合并结果
     */
    public static <K, V> Map<K, V> batchGetOrLoad(Collection<K> keys,
                                                  Function<Collection<K>, Map<K, V>> dbQuery,
                                                  Function<K, String> keyMapper,
                                                  Class<V> clazz,
                                                  long timeout, TimeUnit unit) {
        List<String> redisKeys = keys.stream().map(keyMapper).collect(Collectors.toList());

        // 批量查询 Redis
        List<String> jsonList = redisTemplate.opsForValue().multiGet(redisKeys);

        Map<K, V> resultMap = new HashMap<>();
        List<K> missingKeys = new ArrayList<>();

        int i = 0;
        assert jsonList != null;
        for (String json : jsonList) {
            K businessKey = (K) keys.toArray()[i++];
            if (json == null) {
                missingKeys.add(businessKey);
            } else if (!NULL_PLACEHOLDER.equals(json)) {
                try {
                    resultMap.put(businessKey, objectMapper.readValue(json, clazz));
                } catch (JsonProcessingException e) {
                    throw new RuntimeException("Redis反序列化失败", e);
                }
            }
        }

        // 查询数据库补全缺失数据
        if (!missingKeys.isEmpty()) {
            Map<K, V> dbData = dbQuery.apply(missingKeys);
            for (K key : missingKeys) {
                String redisKey = keyMapper.apply(key);
                V value = dbData.get(key);
                if (value != null) {
                    set(redisKey, value, timeout, unit);
                    resultMap.put(key, value);
                } else {
                    // 空值缓存
                    redisTemplate.opsForValue().set(redisKey, NULL_PLACEHOLDER, NULL_EXPIRE_SECONDS, TimeUnit.SECONDS);
                }
            }
        }
        return resultMap;
    }

    // ===================== List 操作 =====================

    /**
     * 左侧入 List
     *
     * @param key   缓存 Key
     * @param value 值
     * @param <T>   泛型类型
     */
    public static <T> void lPush(String key, T value) {
        try {
            redisTemplate.opsForList().leftPush(key, objectMapper.writeValueAsString(value));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("List写入失败", e);
        }
    }

    /**
     * 获取 List 范围内的所有元素并反序列化
     *
     * @param key   缓存 Key
     * @param start 起始下标
     * @param end   结束下标
     * @param clazz 目标类型
     * @param <T>   泛型类型
     * @return List 对象集合
     */
    public static <T> List<T> lRange(String key, long start, long end, Class<T> clazz) {
        List<String> list = redisTemplate.opsForList().range(key, start, end);
        if (list == null) return Collections.emptyList();
        return list.stream().map(json -> {
            try {
                return objectMapper.readValue(json, clazz);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("List读取失败", e);
            }
        }).collect(Collectors.toList());
    }

    // ===================== Set 操作 =====================

    /**
     * 添加元素到 Set
     *
     * @param key   缓存 Key
     * @param value 元素值
     * @param <T>   泛型类型
     */
    public static <T> void sAdd(String key, T value) {
        try {
            redisTemplate.opsForSet().add(key, objectMapper.writeValueAsString(value));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Set写入失败", e);
        }
    }

    /**
     * 获取 Set 所有成员
     *
     * @param key   缓存 Key
     * @param clazz 目标类型
     * @param <T>   泛型类型
     * @return Set 集合
     */
    public static <T> Set<T> sMembers(String key, Class<T> clazz) {
        Set<String> members = redisTemplate.opsForSet().members(key);
        if (members == null) return Collections.emptySet();
        return members.stream().map(json -> {
            try {
                return objectMapper.readValue(json, clazz);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Set读取失败", e);
            }
        }).collect(Collectors.toSet());
    }

    // ===================== Hash 操作 =====================

    /**
     * Hash 写入字段
     *
     * @param key   缓存 Key
     * @param field Hash 字段名
     * @param value 值
     * @param <T>   泛型类型
     */
    public static <T> void hSet(String key, String field, T value) {
        try {
            redisTemplate.opsForHash().put(key, field, objectMapper.writeValueAsString(value));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Hash写入失败", e);
        }
    }

    /**
     * Hash 获取字段值
     *
     * @param key   缓存 Key
     * @param field Hash 字段名
     * @param clazz 目标类型
     * @param <T>   泛型类型
     * @return 字段值，可能为 null
     */
    public static <T> T hGet(String key, String field, Class<T> clazz) {
        Object value = redisTemplate.opsForHash().get(key, field);
        if (value == null) return null;
        try {
            return objectMapper.readValue(value.toString(), clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Hash读取失败", e);
        }
    }

    /**
     * Hash 删除字段
     *
     * @param key    缓存 Key
     * @param fields 字段名列表
     */
    public static void hDelete(String key, String... fields) {
        redisTemplate.opsForHash().delete(key, (Object[]) fields);
    }

    // ===================== 统一缓存Key生成 =====================

    /**
     * 快速生成缓存 Key，使用冒号拼接
     * 例如：
     *   genKey("user", userId, "profile") -> "user:123:profile"
     *
     * @param parts 组成 Key 的各部分
     * @return 拼接好的缓存 Key
     */
    public static String genKey(Object... parts) {
        if (parts == null || parts.length == 0) {
            throw new IllegalArgumentException("genKey 需要至少一个参数");
        }
        return Arrays.stream(parts)
                .map(String::valueOf)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.joining(":"));
    }

    // ==================== Redisson分布式锁相关 ====================

    /**
     * 尝试获取分布式锁，支持等待时间和锁持有时间（超时自动释放）
     *
     * @param lockKey   锁名
     * @param waitTime  等待获取锁时间（秒）
     * @param leaseTime 锁持有时间，超过自动释放（秒）
     * @return 获得的锁对象，null 表示获取失败
     */
    public static RLock tryLock(String lockKey, long waitTime, long leaseTime) {
        RLock lock = redissonClient.getLock(lockKey);
        try {
            boolean acquired = lock.tryLock(waitTime, leaseTime, TimeUnit.SECONDS);
            return acquired ? lock : null;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        }
    }

    /**
     * 释放分布式锁，只有当前线程持有锁才释放
     *
     * @param lock 锁对象
     */
    public static void unlock(RLock lock) {
        if (lock != null && lock.isHeldByCurrentThread()) {
            lock.unlock();
        }
    }

    /**
     * 执行带分布式锁保护的任务，自动加锁解锁
     *
     * @param lockKey   锁名
     * @param waitTime  等待获取锁时间（秒）
     * @param leaseTime 锁持有时间（秒）
     * @param task      执行任务
     * @param <T>       返回结果类型
     * @return 任务执行结果
     * @throws RuntimeException 如果未获得锁抛异常
     */
    public static <T> T executeWithLock(String lockKey, long waitTime, long leaseTime, LockTask<T> task) {
        RLock lock = tryLock(lockKey, waitTime, leaseTime);
        if (lock == null) {
            throw new RuntimeException("获取锁失败：" + lockKey);
        }
        try {
            return task.execute();
        } finally {
            unlock(lock);
        }
    }

    /**
     * 分布式锁业务接口
     *
     * @param <T> 返回结果类型
     */
    @FunctionalInterface
    public interface LockTask<T> {
        T execute();
    }
}
