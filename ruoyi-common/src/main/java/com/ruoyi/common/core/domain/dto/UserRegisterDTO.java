package com.ruoyi.common.core.domain.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

/**
 * 用户注册DTO
 */
@Schema(description = "用户注册请求参数")
@Data
public class UserRegisterDTO {

    @Schema(description="用户名（4-20位，字母数字下划线）", example = "loe_user")
    @NotBlank(message = "用户名不能为空")
    @Size(min = 4, max = 20, message = "用户名长度必须在4-20个字符之间")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "用户名只能包含字母、数字和下划线")
    private String username;

    @Schema(description="密码（6-20位）", example = "123456")
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度必须在6-20个字符之间")
    private String password;

    @Schema(description="确认密码", example = "123456")
    @NotBlank(message = "确认密码不能为空")
    private String confirmPassword;

    @Schema(description="昵称", example = "小明")
    private String nickname;

    @Schema(description="性别：0-未知 1-男 2-女", example = "1")
    private Integer gender;

    @Schema(description="个性签名", example = "代码改变世界")
    private String signature;

    @Schema(description="个人简介", example = "一名热爱技术的学生")
    private String profile;

    @Schema(description="能力标签（逗号分隔）", example = "Java,Spring,MySQL")
    private List<String> abilityTags;
}
