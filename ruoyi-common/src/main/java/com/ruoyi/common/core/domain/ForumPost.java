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

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 社区帖子对象 tb_post
 */
@Schema(description = "社区帖子实体")
@TableName(value = "tb_post", autoResultMap = true)
@Data
public class ForumPost extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "帖子ID")
    @TableId(type = IdType.AUTO)
    private Long id;

    @Schema(description = "所属版块ID")
    @TableField("board_id")
    private Long boardId;

    @Schema(description = "发帖人ID")
    @TableField("user_id")
    private Long userId;

    @Schema(description = "帖子标题")
    private String title;

    @Schema(description = "帖子内容")
    private String content;

    @Schema(description = "帖子标签(JSON数组)")
    @TableField(value = "tags", typeHandler = StringListTypeHandler.class)
    private List<String> tags;

    @Schema(description = "封面图(JSON数组)")
    @TableField(value = "cover_image", typeHandler = StringListTypeHandler.class)
    private List<String> coverImage;

    @Schema(description = "浏览数")
    private Long viewCount;

    @Schema(description = "点赞数")
    private Long likeCount;

    @Schema(description = "评论数")
    private Long commentCount;

    @Schema(description = "收藏数")
    private Long favoriteCount;

    @Schema(description = "热度分")
    @TableField("hot_score")
    private BigDecimal hotScore;

    @Schema(description = "状态（0-草稿 1-发布 2-下架）")
    private Integer status;

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

