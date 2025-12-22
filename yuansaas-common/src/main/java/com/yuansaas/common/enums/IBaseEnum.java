package com.yuansaas.common.enums;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

/**
 * 基础枚举接口
 *
 * @param <E> 枚举类型
 */
public interface IBaseEnum<E extends Enum<E>> {

    /**
     * 获取枚举名称
     */
    String name();

    /**
     * 获取枚举描述信息
     */
    default String getDescription() {
        return "";
    }

    String getName();

    /**
     * 检查是否匹配指定枚举名称（不区分大小写）
     *
     * @param name 要匹配的名称
     * @return 是否匹配
     */
    default boolean matches(String name) {
        if (name == null || name.isEmpty()) {
            return false;
        }
        return name().equalsIgnoreCase(name);
    }

    /**
     * 检查是否匹配指定枚举实例
     *
     * @param other 要匹配的枚举实例
     * @return 是否匹配
     */
    default boolean matches(E other) {
        return this == other;
    }

    /**
     * 检查是否不匹配指定枚举名称
     *
     * @param name 要检查的名称
     * @return 是否不匹配
     */
    default boolean mismatches(String name) {
        return !matches(name);
    }

    /**
     * 检查是否不匹配指定枚举实例
     *
     * @param other 要检查的枚举实例
     * @return 是否不匹配
     */
    default boolean mismatches(E other) {
        return !matches(other);
    }

    /**
     * 检查是否匹配任意一个枚举实例
     *
     * @param others 要匹配的枚举实例集合
     * @return 是否匹配任意一个枚举实例
     */
    default boolean matchesAny(Collection<E> others) {
        if (others == null || others.isEmpty()) {
            return false;
        }
        return others.stream().anyMatch(this::matches);
    }

    /**
     * 根据名称查找枚举实例
     *
     * @param name 枚举名称
     * @param enumClass 枚举类
     * @return 找到的枚举实例（如果存在）
     */
    static <E extends Enum<E> & IBaseEnum<E>> Optional<E> fromName(String name, Class<E> enumClass) {
        if (name == null || enumClass == null) {
            return Optional.empty();
        }

        try {
            return Optional.of(Enum.valueOf(enumClass, name));
        } catch (IllegalArgumentException e) {
            // 尝试不区分大小写查找
            return Arrays.stream(enumClass.getEnumConstants())
                    .filter(e1 -> e1.matches(name))
                    .findFirst();
        }
    }

    /**
     * 根据名称查找枚举实例（忽略大小写）
     *
     * @param name 枚举名称
     * @param enumClass 枚举类
     * @return 找到的枚举实例（如果存在）
     */
    static <E extends Enum<E> & IBaseEnum<E>> Optional<E> fromNameIgnoreCase(String name, Class<E> enumClass) {
        if (name == null || enumClass == null) {
            return Optional.empty();
        }

        return Arrays.stream(enumClass.getEnumConstants())
                .filter(e -> e.matches(name))
                .findFirst();
    }

    /**
     * 根据描述查找枚举实例
     *
     * @param description 描述信息
     * @param enumClass 枚举类
     * @return 找到的枚举实例（如果存在）
     */
    static <E extends Enum<E> & IBaseEnum<E>> Optional<E> fromDescription(String description, Class<E> enumClass) {
        if (description == null || enumClass == null) {
            return Optional.empty();
        }

        return Arrays.stream(enumClass.getEnumConstants())
                .filter(e -> description.equals(e.getDescription()))
                .findFirst();
    }
}
