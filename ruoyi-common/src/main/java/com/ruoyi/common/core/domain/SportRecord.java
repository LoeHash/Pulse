package com.ruoyi.common.core.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 运动记录对象 tb_sport_record
 */
@Schema(description = "运动记录实体")
@TableName("tb_sport_record")
@Data
public class SportRecord extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "运动记录ID")
    @TableId(type = IdType.AUTO)
    private Long id;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "运动ID")
    private Long sportId;

    @Schema(description = "开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @Schema(description = "结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    @Schema(description = "运动时长（秒）")
    private Integer durationSec;

    @Schema(description = "平均心率")
    private Integer avgHr;

    @Schema(description = "基础消耗（kcal）")
    private Integer caloriesBase;

    @Schema(description = "消耗系数快照")
    private BigDecimal calorieFactorSnapshot;

    @Schema(description = "最终消耗（kcal）")
    private Integer calories;

    @Schema(description = "是否完成（1完成 0中断）")
    private Integer completed;

    @Schema(description = "是否自动结束（1是 0否）")
    private Integer autoClosed;

    @Schema(description = "结束原因")
    private String closeReason;

    @Schema(description = "备注")
    private String note;

    @Schema(description = "扩展指标（JSON）")
    private String metrics;

    @Schema(description = "删除标志（0正常 1删除）")
    @TableLogic
    private Integer isDeleted;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @Schema(description = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @Schema(description = "运动名称")
    @TableField(exist = false)
    private String sportName;
}

