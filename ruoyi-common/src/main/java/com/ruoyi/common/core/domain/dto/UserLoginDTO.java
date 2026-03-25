package com.ruoyi.common.core.domain.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 登录传递对象
 */
@Schema(description = "用户登录请求参数")
@Data
public class UserLoginDTO {

    @Schema(description="用户名", example = "loe")
    private String username;

    @Schema(description="密码", example = "123456")
    private String password;

    @Schema(description="是否记住我", example = "false")
    private Boolean rememberMe;
}
