package com.yuansaas.core.page;

import ch.qos.logback.core.CoreConstants;
import cn.hutool.core.bean.BeanUtil;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 *
 * 分页数据
 *
 * @author LXZ 2025/10/16 17:25
 */
@Data
@NoArgsConstructor
public class RPage<T> {

    /**
     * 页码
     */
    private int number;

    /**
     * 列表集合
     */
    private List<T> content;

    /**
     * 每页数量
     */
    private int size;

    /**
     * 总页数
     */
    private int totalPages;

    /**
     * 总行数
     */
    private long totalElements;

    /**
     * 是否为第一页
     */
    private boolean isFirst;

    /**
     * 是否为最后一页
     */
    private boolean isLast;

    /**
     * 是否有下一页
     */
    private boolean hasNext;

    /**
     * 是否有上一页
     */
    private boolean hasPrevious;


    public boolean getFirst() {
        return number <= 1;
    }

    public RPage<T> content(List<T> content) {
        this.setContent(content);
        return this;
    }

    public RPage<T> totalElements(long totalElements) {
        this.setTotalElements(totalElements);
        return this;
    }


    public boolean getLast() {
        return getTotalPages() <= 1 || getTotalPages() == number;
    }

    public boolean getHasNext() {
        return getTotalPages() > 1 && getTotalPages() > number;
    }

    public boolean setHasPrevious() {
        return number > 1;
    }

    public int getTotalPages() {
        if (this.totalPages != 0) {
            return this.totalPages;
        }
        this.totalPages = size > 0 ? (int) Math.ceil(((double) totalElements) / size) : 0;
        return totalPages;
    }

    /**
     * 初始化Page
     *
     * @param number        页号
     * @param size          页长
     * @param content       数据列表
     * @param totalElements 数据总条数
     */
    public RPage(int number, int size, List<T> content, long totalElements) {
        this(number, size);
        this.content = content;
        this.totalElements = totalElements;
        this.totalPages = size > 0 ? (int) Math.ceil(((double) totalElements) / size) : 0;
    }

    /**
     * 初始化Page
     *
     * @param number 页号
     * @param size   页长
     */
    public RPage(int number, int size) {
        this.number = number;
        this.size = size;
        enforceRenewalPageSize(this);
    }

    /**
     * 强制更新PageSize
     *
     * @param page
     */
    private static void enforceRenewalPageSize(RPage<?> page) {
        page.setSize(Math.min(CoreConstants.TABLE_ROW_LIMIT, page.getSize()));
    }


    public static <R> RPage<R> convert(Page<R> page) {
        RPage<R> rPage = new RPage<>();
        rPage.number = page.getNumber() + 1;
        rPage.size = page.getSize();
        rPage.totalElements = page.getTotalElements();
        rPage.totalPages = page.getTotalPages();
        rPage.setContent(page.getContent());
        return rPage;
    }

    public static <R, T> RPage<T> convert(Page<R> page, Class<T> target) {
        RPage<T> rPage = new RPage<>();
        rPage.number = page.getNumber() + 1;
        rPage.size = page.getSize();
        rPage.totalElements = page.getTotalElements();
        rPage.totalPages = page.getTotalPages();
        rPage.content = BeanUtil.copyToList(page.getContent(), target);
        return rPage;
    }

    public <R> RPage<R> map(Function<T, R> mapper) {
        RPage<R> rPage = renew();
        rPage.content = this.content.stream().map(mapper).collect(Collectors.toList());
        return rPage;
    }

    public <R> RPage<R> convertElements(Class<R> clazz) {
        RPage<R> renew = renew();
        renew.content = BeanUtil.copyToList(this.getContent(), clazz);
        return renew;
    }

    public <R> RPage<R> renew() {
        RPage<R> rPage = new RPage<>();
        rPage.number = this.getNumber();
        rPage.size = this.getSize();
        rPage.totalElements = this.getTotalElements();
        rPage.totalPages = this.getTotalPages();
        return rPage;
    }
}
