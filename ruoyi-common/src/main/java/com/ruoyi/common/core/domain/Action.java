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
 * 动作模板对象 tb_action
 *
 * @author loe
 * @date 2026-03-22
 */
@Schema(description = "动作模板实体")
@JsonIgnoreProperties({
        "createBy",
        "updateBy",
        "remark"
})
@TableName(value = "tb_action", autoResultMap = true)
@Data
public class Action extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "动作ID")
    @Excel(name = "动作ID")
    @TableId(type = IdType.AUTO)
    private Long id;

    @Schema(description = "动作名称（如：深蹲、开合跳、休息）")
    @Excel(name = "动作名称")
    private String name;

    @Schema(description = "动作类型（EXERCISE/REST/OTHER）")
    @Excel(name = "动作类型")
    @TableField("action_type")
    private String actionType;

    @Schema(description = "单位类型（REPS/SECONDS）")
    @Excel(name = "单位类型")
    @TableField("unit_type")
    private String unitType;

    @Schema(description = "默认目标值（次数或秒数）")
    @Excel(name = "默认目标值")
    @TableField("default_value")
    private Integer defaultValue;

    @Schema(description = "封面URL")
    @Excel(name = "封面URL")
    @TableField("cover_url")
    private String coverUrl;

    @Schema(description = "教程视频URL（REST可为空）")
    @Excel(name = "教程视频URL")
    @TableField("video_url")
    private String videoUrl;

    @Schema(description = "教程视频时长（秒）")
    @Excel(name = "教程视频时长（秒）")
    @TableField("video_duration_sec")
    private Integer videoDurationSec;

    @Schema(description = "动作提示/要点（富文本）")
    @Excel(name = "动作提示/要点")
    private String tips;

    @Schema(description = "动作简介（列表摘要）")
    @Excel(name = "动作简介")
    private String intro;

    @Schema(description = "创建者用户ID（NULL=系统动作）")
    @Excel(name = "创建者用户ID")
    @TableField("create_user_id")
    private Long createUserId;

    @Schema(description = "动作分类（JSON数组）")
    @Excel(name = "动作分类")
    @TableField(value = "category", typeHandler = StringListTypeHandler.class)
    private List<String> category;

    @Schema(description = "目标标签（JSON数组）")
    @Excel(name = "目标标签")
    @TableField(value = "goal_tags", typeHandler = StringListTypeHandler.class)
    private List<String> goalTags;

    @Schema(description = "通用标签（JSON数组）")
    @Excel(name = "通用标签")
    @TableField(value = "tags", typeHandler = StringListTypeHandler.class)
    private List<String> tags;

    @Schema(description = "状态（1启用 0禁用）")
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
}