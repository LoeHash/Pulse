package com.ruoyi.common.core.domain.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "评论参数")
@Data
public class CommentDTO {

    @Schema(description="帖子ID", example = "100")
    private Long postId;

    @Schema(description="评论用户ID", example = "200")
    private Long userId;

    @Schema(description="评论内容（支持富文本）")
    private String content;

    @Schema(description="回复的评论ID", example = "300")
    private Long replyToCommentId;
}
