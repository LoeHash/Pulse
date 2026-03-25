package com.ruoyi.common.core.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 管理端课程参与明细VO
 */
@Schema(name = "CourseParticipationAdminVO", description = "管理端课程参与明细")
@Data
public class CourseParticipationAdminVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "参与记录ID")
    private Long id;

    @Schema(description = "课程ID")
    private Long courseId;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "账号")
    private String username;

    @Schema(description = "昵称")
    private String nickname;

    @Schema(description = "状态（1有效 0取消）")
    private Integer status;

    @Schema(description = "参与时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}

