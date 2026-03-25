package com.ruoyi.common.core.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ruoyi.common.annotation.Excel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 通知对象 tb_notice
 *
 * @author loe
 * @date 2026-02-23
 */
@Schema(name = "Notice", description = "通知实体")
@JsonIgnoreProperties({
        "createBy",
        "updateBy",
        "remark",
})
@Data
@TableName(value = "tb_notice", autoResultMap = true)
public class Notice extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 通知ID */
    @Schema(description = "通知ID")
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 发送者ID */
    @Schema(description = "发送者ID")
    @Excel(name = "发送者ID")
    private Long senderId;

    /** 通知标题 */
    @Schema(description = "通知标题")
    @Excel(name = "通知标题")
    private String title;

    /** 通知内容（富文本） */
    @Schema(description = "通知内容（富文本）")
    @Excel(name = "通知内容")
    private String content;

    /** 通知类型 */
    @Schema(description = "通知类型")
    @Excel(name = "通知类型")
    private String type;

    /** 扩展数据（JSON格式） */
    @Schema(description = "扩展数据（JSON格式）")
    @Excel(name = "扩展数据")
    private String extra;

    /** 状态：NORMAL-正常 CANCELED-已撤销 DELETED-已删除 */
    @Schema(description = "状态：NORMAL-正常 CANCELED-已撤销 DELETED-已删除")
    @Excel(name = "状态")
    private String status;

    /** 是否已读（仅用于展示，不入库） */
    @Schema(description = "是否已读（仅用于展示，不入库）")
    @Excel(name = "是否已读")
    @TableField(exist = false)
    private Integer isRead;

    /** 逻辑删除标志(0-未删除 1-已删除) */
    @Schema(description = "逻辑删除标志(0-未删除 1-已删除)")
    @TableLogic
    private Integer isDeleted;
}