package com.ruoyi.common.core.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.handler.StringListTypeHandler;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 训练计划对象 tb_plan
 */
@Schema(description = "训练计划实体")
@TableName(value = "tb_plan", autoResultMap = true)
@Data
public class Plan extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "计划ID")
    @TableId(type = IdType.AUTO)
    private Long id;

    @Schema(description = "计划标题")
    private String title;

    @Schema(description = "封面URL")
    private String coverUrl;

    @Schema(description = "计划简介")
    private String intro;

    @Schema(description = "计划天数")
    private Integer days;

    @Schema(description = "适应人群标签", example = "[\"宝妈\",\"大体重\"]")
    @TableField(typeHandler = StringListTypeHandler.class)
    private List<String> fitPeopleTags;

    @Schema(description = "目标标签", example = "[\"增肌\",\"舒缓放松\"]")
    @TableField(typeHandler = StringListTypeHandler.class)
    private List<String> goalTags;

    @Schema(description = "部位标签", example = "[\"胸\",\"肩\",\"腿\"]")
    @TableField(typeHandler = StringListTypeHandler.class)
    private List<String> bodyPartTags;

    @Schema(description = "难度（1零基础 2初级 3进阶 4强化）")
    private Integer difficulty;

    @Schema(description = "状态（1上架 0下架）")
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

