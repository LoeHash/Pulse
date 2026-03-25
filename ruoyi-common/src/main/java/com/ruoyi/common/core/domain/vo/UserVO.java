package com.ruoyi.common.core.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 用户展示VO（字段与 User 实体保持一致；敏感字段不输出）
 *
 * @author loe
 * @date 2026-03-21
 */
@Schema(name = "UserVO", description = "用户展示视图对象（字段与User实体一致；敏感字段不输出）")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class UserVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 用户ID */
    @Schema(description="用户ID")
    private Long id;

    /** 账号 */
    @Schema(description="账号")
    private String username;

    /** 登录密码（加密）- 安全：不返回给前端 */
    @JsonIgnore
    @Schema(description="登录密码（加密），安全字段，不返回")
    private String password;

    /** 昵称 */
    @Schema(description="昵称")
    private String nickname;

    /** 性别（0未知 1男 2女） */
    @Schema(description="性别（0未知 1男 2女）")
    private Integer gender;

    /** 年龄 */
    @Schema(description="年龄")
    private Integer age;

    /** 身高（cm） */
    @Schema(description="身高（cm）")
    private Integer heightCm;

    /** 体重（kg） */
    @Schema(description="体重（kg）")
    private BigDecimal weightKg;

    /** 个性签名 */
    @Schema(description="个性签名")
    private String signature;

    /** 目标信息（JSON数组） */
    @Schema(description="目标信息（JSON数组）")
    private List<String> goalInfo;

    /** 头像URL */
    @Schema(description="头像URL")
    private String avatar;

    /** 角色ID */
    @Schema(description="角色ID")
    private Integer roleId;

    /** 创建时间 */
    @Schema(description="创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 更新时间 */
    @Schema(description="更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /** 删除标志（0正常 1删除） */
    @Schema(description="删除标志（0正常 1删除）")
    private Integer isDeleted;

    /** 状态（1正常 0禁用） */
    @Schema(description="状态（1正常 0禁用）")
    private Integer status;
}