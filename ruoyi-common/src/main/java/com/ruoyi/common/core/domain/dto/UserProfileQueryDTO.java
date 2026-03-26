package com.ruoyi.common.core.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 用户画像查询参数
 */
@Schema(description = "用户画像查询参数")
@Data
public class UserProfileQueryDTO {

    @Schema(description = "画像ID")
    private Long id;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "画像版本号")
    private Integer profileVersion;

    @Schema(description = "最近活跃时间-起")
    private Date lastActiveTimeFrom;

    @Schema(description = "最近活跃时间-止")
    private Date lastActiveTimeTo;
}

