package com.ruoyi.common.core.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 通知接收记录对象 tb_notice_receiver
 *
 * @author loe
 * @date 2026-02-23
 */
@Schema(name = "NoticeReceiver", description = "通知接收记录��体")
@JsonIgnoreProperties({
        "createBy",
        "updateBy",
        "remark",
})
@Data
@TableName("tb_notice_receiver")
public class NoticeReceiver extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 记录ID */
    @Schema(description = "记录ID")
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 通知ID */
    @Schema(description = "通知ID")
    private Long noticeId;

    /** 接收者ID（用户ID） */
    @Schema(description = "接收者ID（用户ID）")
    private Long userId;

    /** 是否已读(0-未读 1-已读) */
    @Schema(description = "是否已读(0-未读 1-已读)")
    private Integer isRead;

    /** 阅读时间 */
    @Schema(description = "阅读时间")
    private LocalDateTime readTime;

    /** 逻辑删除标志(0-未删除 1-已删除) */
    @Schema(description = "逻辑删除标志(0-未删除 1-已删除)")
    @TableLogic
    private Integer isDeleted;
}