package com.ruoyi.common.core.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ruoyi.common.annotation.Excel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 课程动作编排对象 tb_course_action
 * 说明：休息也作为动作（引用 tb_action.action_type=REST 的动作模板）
 *
 * @author loe
 * @date 2026-03-22
 */
@Schema(description = "课程动作编排实体（线性步骤，纯动作化）")
@JsonIgnoreProperties({
        "createBy",
        "updateBy",
        "remark"
})
@TableName(value = "tb_course_action", autoResultMap = true)
@Data
public class CourseAction extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "步骤ID")
    @Excel(name = "步骤ID")
    private Long id;

    @Schema(description = "课程ID")
    @Excel(name = "课程ID")
    @TableField("course_id")
    private Long courseId;

    @Schema(description = "步骤序号（从1开始）")
    @Excel(name = "步骤序号")
    @TableField("sort_no")
    private Integer sortNo;

    @Schema(description = "动作模板ID")
    @Excel(name = "动作模板ID")
    @TableField("action_id")
    private Long actionId;

    @Schema(description = "目标值（次数或秒数；单位由动作模板unit_type决定）")
    @Excel(name = "目标值")
    @TableField("target_value")
    private Integer targetValue;

    @Schema(description = "步骤提示（富文本，可选，覆盖动作模板tips）")
    @Excel(name = "步骤提示")
    @TableField("tips_override")
    private String tipsOverride;

    @Schema(description = "步骤指标扩展（JSON字符串，可选）")
    @Excel(name = "步骤���标扩展")
    @TableField("metrics_override")
    private String metricsOverride;

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

    @Schema(description = "动作详情")
    @TableField(exist = false)
    private Action action;
}