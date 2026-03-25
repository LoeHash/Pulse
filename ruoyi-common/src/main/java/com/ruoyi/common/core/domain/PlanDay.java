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
 * 训练计划-天对象 tb_plan_day
 */
@Schema(description = "训练计划-天实体")
@TableName("tb_plan_day")
@Data
public class PlanDay extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "计划天ID")
    @TableId(type = IdType.AUTO)
    private Long id;

    @Schema(description = "计划ID")
    private Long planId;

    @Schema(description = "第几天（从1开始）")
    private Integer dayNo;

    @Schema(description = "当天标题")
    private String title;

    @Schema(description = "当天提示")
    private String tips;

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

