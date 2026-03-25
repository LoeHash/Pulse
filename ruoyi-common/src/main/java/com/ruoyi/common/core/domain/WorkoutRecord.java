package com.ruoyi.common.core.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ruoyi.common.annotation.Excel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 训练记录对象 tb_workout_record
 *
 * @author loe
 * @date 2026-03-21
 */
@Schema(description = "训练记录实体")
@JsonIgnoreProperties({
        "createBy",
        "updateBy",
        "remark"
})
@TableName(value = "tb_workout_record", autoResultMap = true)
@Data
public class WorkoutRecord extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "记录ID")
    @Excel(name = "记录ID")
    @TableId(type = IdType.AUTO)
    private Long id;

    @Schema(description = "用户ID")
    @Excel(name = "用户ID")
    @TableField("user_id")
    private Long userId;

    @Schema(description = "课程ID")
    @Excel(name = "课程ID")
    @TableField("course_id")
    private Long courseId;

    @Schema(description = "开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "开始时间")
    @TableField("start_time")
    private Date startTime;

    @Schema(description = "结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "结束时间")
    @TableField("end_time")
    private Date endTime;

    @Schema(description = "本次训练时长（秒）")
    @Excel(name = "训练时长（秒）")
    @TableField("duration_sec")
    private Integer durationSec;

    @Schema(description = "本次消耗热量（kcal，可选）")
    @Excel(name = "消耗热量（kcal）")
    private Integer calories;

    @Schema(description = "是否完成（1完成 0中断）")
    @Excel(name = "是否完成")
    private Integer completed;

    @Schema(description = "备注（训练笔记，可选）")
    @Excel(name = "备注")
    private String note;

    @Schema(description = "训练指标扩展（JSON字符串，可选）")
    private String metrics;

    @Schema(description = "是否自动结束（1是 0否）")
    @Excel(name = "是否自动结束")
    @TableField("auto_closed")
    private Integer autoClosed;

    @Schema(description = "结束原因（user/manual/system_timeout）")
    @Excel(name = "结束原因")
    @TableField("close_reason")
    private String closeReason;

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