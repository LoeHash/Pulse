package com.ruoyi.common.core.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.handler.StringListTypeHandler;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 用户对象 tb_user
 *
 * @author loe
 * @date 2026-03-21
 */
@Schema(description = "用户实体")
@JsonIgnoreProperties({
        "createBy",
        "updateBy",
        "remark"
})
@TableName(value = "tb_user")
@Data
public class User extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 用户ID */
    @Schema(description="用户ID")
    @Excel(name = "用户id")
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 账号 */
    @Schema(description="账号")
    @Excel(name = "账号")
    private String username;

    @Schema(description="登录密码（加密）")
    @Excel(name = "登录密码")
    private String password;

    /** 昵称 */
    @Excel(name = "昵称")
    @Schema(description="昵称")
    private String nickname;

    /** 性别（0未知 1男 2女） */
    @Excel(name = "性别")
    @Schema(description="性别（0未知 1男 2女）")
    private Integer gender;

    @Schema(description = "年龄")
    @Excel(name = "年龄")
    private Integer age;

    @Schema(description = "身高（cm）")
    @Excel(name = "身高（cm）")
    @TableField("height_cm")
    private Integer heightCm;

    @Schema(description = "体重（kg）")
    @Excel(name = "体重（kg）")
    @TableField("weight_kg")
    private BigDecimal weightKg;

    /** 个性签名 */
    @Excel(name = "个性签名")
    @Schema(description="个性签名")
    private String signature;

    /**
     * 目标信息（JSON数组 / varchar）
     * 表字段：goal_info
     */
    @Excel(name = "目标信息")
    @Schema(description="目标信息（JSON数组）")
    @TableField(value = "goal_info", typeHandler = StringListTypeHandler.class)
    private List<String> goalInfo;

    /** 头像URL */
    @Schema(description="头像URL")
    @Excel(name = "头像URL")
    private String avatar;

    /** 角色ID */
    @Schema(description="角色ID")
    @Excel(name = "角色ID")
    @TableField("role_id")
    private Integer roleId;

    /** 创建时间 */
    @Schema(description="创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "创建时间")
    @TableField("create_time")
    private Date createTime;

    /** 更新时间 */
    @Schema(description="更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "更新时间")
    @TableField("update_time")
    private Date updateTime;

    /** 删除标志（0正常 1删除） */
    @Schema(description="删除标志（0正常 1删除）")
    @Excel(name = "删除标志")
    @TableLogic
    private Integer isDeleted;

    /** 状态（1正常 0禁用） */
    @Schema(description="状态（1正常 0禁用）")
    @Excel(name = "状态")
    private Integer status;
}