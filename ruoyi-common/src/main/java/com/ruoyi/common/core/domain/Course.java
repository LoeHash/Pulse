package com.ruoyi.common.core.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.handler.StringListTypeHandler;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 课程对象 tb_course
 *
 * @author loe
 * @date 2026-03-21
 */
@Schema(description = "课程实体")
@JsonIgnoreProperties({
        "createBy",
        "updateBy",
        "remark"
})
@TableName(value = "tb_course", autoResultMap = true)
@Data
public class Course extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "课程ID")
    @Excel(name = "课程ID")
    @TableId(type = IdType.AUTO)
    private Long id;

    @Schema(description = "课程标题")
    @Excel(name = "课程标题")
    private String title;

    @Schema(description = "封面URL")
    @Excel(name = "封面URL")
    @TableField("cover_url")
    private String coverUrl;


    /**
     * 分类（JSON数组 / varchar）
     * 表字段：category
     */
    @Schema(description = "分类（JSON数组）")
    @Excel(name = "分类")
    @TableField(value = "category", typeHandler = StringListTypeHandler.class)
    private List<String> category;

    /**
     * 目标标签（JSON数组 / varchar）
     * 表字段：goal_tags
     * 与用户 goalInfo 对齐，用于推荐匹配
     */
    @Schema(description = "目标标签（JSON数组，用于推荐匹配）")
    @Excel(name = "目标标签")
    @TableField(value = "goal_tags", typeHandler = StringListTypeHandler.class)
    private List<String> goalTags;

    @Schema(description = "难度（1入门 2初级 3中级 4高级）")
    @Excel(name = "难度")
    private Integer difficulty;

    @Schema(description = "课程时长（秒）")
    @Excel(name = "课程时长（秒）")
    @TableField("duration_sec")
    private Integer durationSec;

    @Schema(description = "消耗热量估算（kcal，可选）")
    @Excel(name = "消耗热量估算（kcal）")
    private Integer calories;

    @Schema(description = "创建者id, Null 为系统课程, 所有人都可以查看")
    @Excel(name = "创建者ID")
    private Long createUserId;

    @Schema(description = "课程简介")
    @Excel(name = "课程简介")
    private String intro;

    @Schema(description = "状态（1上架 0下架）")
    @Excel(name = "状态")
    private Integer status;

    @Schema(description = "删除标志（0正常 1删除）")
    @Excel(name = "删除标志")
    @TableLogic
    private Integer isDeleted;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "创建时间")
    @TableField("create_time")
    private Date createTime;

    @Schema(description = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "更新时间")
    @TableField("update_time")
    private Date updateTime;

    @Schema(description = "课程动作列表")
    @TableField(exist = false)
    private List<CourseAction> courseActions;
}