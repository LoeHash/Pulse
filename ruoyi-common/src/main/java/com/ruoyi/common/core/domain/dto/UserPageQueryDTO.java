package com.ruoyi.common.core.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 管理端用户分页查询参数
 */
@Schema(description = "管理端用户分页查询参数")
@Data
public class UserPageQueryDTO {

    @Schema(description = "用户ID", example = "1")
    private Long id;

    @Schema(description = "账号（模糊）", example = "adm")
    private String username;

    @Schema(description = "昵称（模糊）", example = "管理员")
    private String nickname;

    @Schema(description = "性别（0未知 1男 2女）", example = "1")
    private Integer gender;

    @Schema(description = "角色ID", example = "4")
    private Integer roleId;

    @Schema(description = "状态（1正常 0禁用）", example = "1")
    private Integer status;

    @Schema(description = "删除标志（0正常 1删除）", example = "0")
    private Integer isDeleted;
}

