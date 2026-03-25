package com.ruoyi.common.core.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 管理端修改用户参数
 */
@Schema(description = "管理端修改用户参数")
@Data
public class UserAdminUpdateDTO {

    @Schema(description = "账号", example = "alice")
    @Size(max = 50, message = "账号长度不能超过50个字符")
    private String username;

    @Schema(description = "明文密码（服务端会做SHA-256）", example = "123456")
    @Size(max = 100, message = "密码长度不能超过100个字符")
    private String password;

    @Schema(description = "昵称", example = "爱丽丝")
    @Size(max = 50, message = "昵称长度不能超过50个字符")
    private String nickname;

    @Schema(description = "性别（0未知 1男 2女）", example = "2")
    private Integer gender;

    @Schema(description = "年龄", example = "25")
    private Integer age;

    @Schema(description = "身高(cm)", example = "168")
    private Integer heightCm;

    @Schema(description = "体重(kg)", example = "52.5")
    private BigDecimal weightKg;

    @Schema(description = "个性签名", example = "保持热爱")
    @Size(max = 255, message = "个性签名长度不能超过255个字符")
    private String signature;

    @Schema(description = "目标信息", example = "[\"减脂\",\"跑步\"]")
    private List<String> goalInfo;

    @Schema(description = "头像URL", example = "https://example.com/avatar.png")
    @Size(max = 255, message = "头像URL长度不能超过255个字符")
    private String avatar;

    @Schema(description = "角色ID", example = "2")
    private Integer roleId;

    @Schema(description = "状态（1正常 0禁用）", example = "1")
    private Integer status;

    @Schema(description = "删除标志（0正常 1删除）", example = "0")
    private Integer isDeleted;
}

