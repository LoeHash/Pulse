package com.ruoyi.common.core.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ruoyi.common.annotation.Excel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;
import java.util.Map;

/**
 * 用户画像对象 tb_user_profile
 *
 * @author loe
 * @date 2026-03-24
 */
@Schema(description = "用户画像实体")
@JsonIgnoreProperties({
        "createBy",
        "updateBy",
        "remark"
})
@Data
@TableName(value = "tb_user_profile", autoResultMap = true)
public class UserProfile extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @Schema(description = "主键ID", example = "1")
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 用户ID */
    @Schema(description = "用户ID", example = "1001")
    @Excel(name = "用户ID")
    @TableField("user_id")
    private Long userId;

    /** 标签偏好分数(JSON) */
    @Schema(description = "标签偏好分数(JSON)", example = "{\"Java\":12.5,\"Spring\":8.2}")
    @Excel(name = "标签偏好")
    @TableField(value = "tag_pref", typeHandler = JacksonTypeHandler.class)
    private Map<String, Double> tagPref;

    /** 版块偏好分数(JSON) */
    @Schema(description = "版块偏好分数(JSON)", example = "{\"1\":10.0,\"3\":4.6}")
    @Excel(name = "版块偏好")
    @TableField(value = "board_pref", typeHandler = JacksonTypeHandler.class)
    private Map<String, Double> boardPref;

    /** 最近活跃时间 */
    @Schema(description = "最近活跃时间", example = "2026-03-24 20:30:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "最近活跃时间")
    @TableField("last_active_time")
    private Date lastActiveTime;

    /** 画像版本号 */
    @Schema(description = "画像版本号", example = "1")
    @Excel(name = "画像版本号")
    @TableField("profile_version")
    private Integer profileVersion;

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