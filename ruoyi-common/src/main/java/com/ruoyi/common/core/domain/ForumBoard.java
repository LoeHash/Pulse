package com.ruoyi.common.core.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 社区版块对象 tb_board
 */
@Schema(description = "社区版块实体")
@TableName("tb_board")
@Data
public class ForumBoard extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "版块ID")
    @TableId(type = IdType.AUTO)
    private Long id;

    @Schema(description = "版块名称")
    private String name;

    @Schema(description = "版块描述")
    private String description;

    @Schema(description = "版块图标")
    private String icon;

    @Schema(description = "排序值")
    private Integer sort;

    @Schema(description = "状态（0-禁用 1-启用）")
    private Integer status;

    @Schema(description = "帖子数")
    private Long postCount;

    @Schema(description = "关注数")
    private Long followerCount;

    @Schema(description = "删除标志（0-未删除 1-已删除）")
    @TableLogic
    private Integer isDeleted;

    @Schema(description = "创建者")
    @TableField("create_by")
    private String createBy;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("create_time")
    private Date createTime;

    @Schema(description = "更新者")
    @TableField("update_by")
    private String updateBy;

    @Schema(description = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("update_time")
    private Date updateTime;

    @Schema(description = "备注")
    private String remark;
}

