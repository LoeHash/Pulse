package com.ruoyi.common.core.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ruoyi.common.annotation.Excel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 用户行为事件对象 tb_user_event
 *
 * @author loe
 * @date 2026-03-24
 */
@Schema(description = "用户行为事件实体")
@JsonIgnoreProperties({
        "createBy",
        "updateBy",
        "remark"
})
@Data
@TableName("tb_user_event")
public class UserEvent extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 事件ID */
    @Schema(description = "事件ID", example = "1")
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 用户ID */
    @Schema(description = "用户ID", example = "1001")
    @Excel(name = "用户ID")
    @TableField("user_id")
    private Long userId;

    /** 帖子ID */
    @Schema(description = "帖子ID", example = "2001")
    @Excel(name = "帖子ID")
    @TableField("post_id")
    private Long postId;

    /** 事件类型 */
    @Schema(description = "事件类型", example = "impression_duration")
    @Excel(name = "事件类型")
    @TableField("event_type")
    private String eventType;

    /** 场景：feed_list/post_detail */
    @Schema(description = "场景", example = "feed_list")
    @Excel(name = "场景")
    private String scene;

    /** 页码 */
    @Schema(description = "页码", example = "1")
    @Excel(name = "页码")
    @TableField("page_no")
    private Integer pageNo;

    /** 开始时间戳(ms) */
    @Schema(description = "开始时间戳(ms)", example = "1764000000000")
    @Excel(name = "开始时间戳")
    @TableField("start_ts")
    private Long startTs;

    /** 结束时间戳(ms) */
    @Schema(description = "结束时间戳(ms)", example = "1764000003500")
    @Excel(name = "结束时间戳")
    @TableField("end_ts")
    private Long endTs;

    /** 停留时长(ms) */
    @Schema(description = "停留时长(ms)", example = "3500")
    @Excel(name = "停留时长(ms)")
    @TableField("duration_ms")
    private Long durationMs;

    /** 事件时间 */
    @Schema(description = "事件时间", example = "2026-03-24 20:30:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "事件时间")
    @TableField("event_time")
    private Date eventTime;

    /** 删除标志（0-未删除 1-已删除） */
    @Schema(description = "删除标志（0-未删除 1-已删除）", allowableValues = {"0", "1"}, example = "0")
    @TableLogic
    @TableField("is_deleted")
    private Integer isDeleted;

    /** 创建时间 */
    @Schema(description = "创建时间", example = "2026-03-24 10:30:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "创建时间")
    @TableField("create_time")
    private Date createTime;

    /** 更新时间 */
    @Schema(description = "更新时间", example = "2026-03-24 15:45:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "更新时间")
    @TableField("update_time")
    private Date updateTime;
}