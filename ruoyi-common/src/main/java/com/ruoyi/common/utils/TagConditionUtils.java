package com.ruoyi.common.utils;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 标签条件工具类
 * 用于处理数据库中存储为JSON数组字符串的标签字段查询
 */
public class TagConditionUtils {

    /**
     * 应用JSON类似标签查询条件
     * 针对数据库中存储为 ["tag1", "tag2"] 格式的JSON数组字符串字段
     * 生成 (column LIKE '%"tag1"%' OR column LIKE '%"tag2"%') 的条件
     *
     * @param wrapper LambdaQueryWrapper
     * @param column  列函数
     * @param tags    标签列表
     * @param <T>     实体类型
     */
    public static <T> void applyJsonLikeTags(LambdaQueryWrapper<T> wrapper, SFunction<T, ?> column, List<String> tags) {
        if (CollectionUtils.isEmpty(tags)) {
            return;
        }

        wrapper.and(w -> {
            boolean first = true;
            for (String tag : tags) {
                if (StringUtils.isBlank(tag)) {
                    continue;
                }
                // 使用 LIKE '%"tag"%' 来匹配 JSON 数组中的元素
                // 加上双引号是为了避免匹配到其他包含该字符串但不是独立元素的标签
                String likeValue = "\"" + tag + "\"";
                
                if (first) {
                    w.like(column, likeValue);
                    first = false;
                } else {
                    w.or().like(column, likeValue);
                }
            }
        });
    }
}

