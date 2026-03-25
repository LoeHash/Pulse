package com.ruoyi.common.core.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 运动种类对象 tb_sport
 */
@Schema(description = "运动种类实体")
@TableName("tb_sport")
@Data
public class Sport extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "运动ID")
    @TableId(type = IdType.AUTO)
    private Long id;

    @Schema(description = "运动名称")
    private String name;

    @Schema(description = "运动类型（RUN/DANCE/BALL/HIIT/OTHER）")
    private String sportType;

    @Schema(description = "消耗系数")
    private BigDecimal calorieFactor;

    @Schema(description = "封面URL")
    private String coverUrl;

    @Schema(description = "简介")
    private String intro;

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

