package com.yuansaas.core.jpa.querydsl;

import cn.hutool.extra.validation.ValidationUtil;
import com.google.common.base.Objects;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import jodd.util.StringUtil;
import org.springframework.util.ObjectUtils;

import java.util.Optional;
import java.util.function.Function;

/**
 * QueryDslBuilder 构建QueryDsl条件类
 *
 * @author LXZ 2025/10/20 18:04
 */
public class BoolBuilder {

    private BooleanBuilder booleanBuilder;

    public static BoolBuilder getInstance() {
        BoolBuilder boolBuilder = new BoolBuilder();
        boolBuilder.booleanBuilder = new BooleanBuilder();
        return boolBuilder;
    }

    public static BoolBuilder getInstance(Predicate predicate) {
        BoolBuilder boolBuilder = new BoolBuilder();
        boolBuilder.booleanBuilder = new BooleanBuilder(predicate);
        return boolBuilder;
    }
    public BooleanBuilder getWhere() {
        return booleanBuilder;
    }


    /**
     * 使用 and 连接查询条件
     * <p>
     * and (userName, qUser.userName::contains)
     *
     * @param t         查询条件的值
     * @param predicate 查询条件
     * @param <T>       查询条件的类型
     * @return 查询条件构建类
     */
    public <T> BoolBuilder and(T t, Function<T, Predicate> predicate) {
        if (!ObjectUtils.isEmpty(t)) {
            if (t instanceof Optional) {
                Optional<?> optionalValue = (Optional<?>) t;
                optionalValue.ifPresent(v -> booleanBuilder.and(predicate.apply((T) v)));
            }

            booleanBuilder.and(predicate.apply(t));
        }
        return this;
    }

    /**
     * 使用 and 连接查询条件
     * <p>
     * and (qUser.userName.contains(userParam.getUserName()).and(qUser.realName.contains(userParam.getUserName())))
     *
     * @param predicate and 的查询条件
     * @return 查询条件构建类
     */
    public BoolBuilder and(Predicate predicate) {
        if (!ObjectUtils.isEmpty(predicate)) {
            booleanBuilder.and(predicate);
        }
        return this;
    }
    /**
     * 使用 or 连接查询条件
     * <p>
     * or(qUser.userName.contains(userParam.getUserName())
     *
     * @param t         查询条件的值
     * @param predicate 查询条件
     * @param <T>       查询条件的类型
     * @return 查询条件构建类
     */
    public <T> BoolBuilder or(T t, Function<T, Predicate> predicate) {
        if (!ObjectUtils.isEmpty(t)) {
            booleanBuilder.or(predicate.apply(t));
        }
        return this;
    }

    /**
     * and ( p1 or p2)
     *
     * @param predicates 条件
     * @return 构建条件类
     */
    public BoolBuilder andOr(Predicate... predicates) {
        if (!ObjectUtils.isEmpty(predicates)) {
            booleanBuilder.and(booleanBuilder.andAnyOf(predicates));
        }
        return this;
    }
}
