package com.ruoyi.common.core.domain.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Schema(description = "用户信息更新参数")
@Data
public class UserUpdateDTO {

    @Schema(description = "昵称", example = "新昵称")
    @Size(max = 50, message = "昵称长度不能超过50个字符")
    private String nickname;

    @Schema(description="性别：0-未知 1-男 2-女", example = "1")
    private Integer gender;

    @Schema(description="个性签名", example = "保持热爱")
    @Size(max = 255, message = "个性签名长度不能超过255个字符")
    private String signature;

    @Schema(description="个人简介", example = "专注后端开发")
    @Size(max = 500, message = "个人简介长度不能超过500个字符")
    private String profile;

    @Schema(description="能力标签", example = "SpringBoot,Redis")
    @Size(max = 255, message = "能力标签长度不能超过255个字符")
    private String abilityTags;

    @Schema(description="头像URL", example = "https://xxx/avatar.png")
    @Size(max = 255, message = "头像URL长度不能超过255个字符")
    private String avatar;


    @Schema(description="角色ID", example = "1（普通）2（更高）")
    @DecimalMax(value = "3", message = "参数错误！")
    private Integer roleId;

}

