package com.ruoyi.common.core.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 训练计划-天-课程安排对象 tb_plan_day_course
 */
@Schema(description = "训练计划-天-课程安排实体")
@TableName("tb_plan_day_course")
@Data
public class PlanDayCourse extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "记录ID")
    @TableId(type = IdType.AUTO)
    private Long id;

    @Schema(description = "计划ID（冗余字段）")
    private Long planId;

    @Schema(description = "计划天ID")
    private Long planDayId;

    @Schema(description = "当天课程序号（从1开始）")
    private Integer sortNo;

    @Schema(description = "课程ID")
    private Long courseId;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "状态（1启用 0禁用）")
    private Integer status;

    @Schema(description = "删除标志（0正常 1删除）")
    @TableLogic
    private Integer isDeleted;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @Schema(description = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}

