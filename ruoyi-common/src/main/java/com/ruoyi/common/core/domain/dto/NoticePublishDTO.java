package com.ruoyi.common.core.domain.dto;

import com.ruoyi.common.core.domain.Notice;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 管理员发布通知 DTO
 */
@Data
@Schema(name = "通知发布DTO")
public class NoticePublishDTO {

    @Schema(description = "通知信息")
    private Notice notice;

    @Schema(description = "接收用户ID列表")
    private List<Long> userIds;
}