package com.ruoyi.common.core.domain;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 批量用户事件请求对象
 */
@Data
@Schema(description = "批量用户事件请求")
public class UserEventBatchReq {

    /** 事件列表 */
    @Schema(description = "事件列表")
    private List<UserEventReq> events;
}