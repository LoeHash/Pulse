package com.ruoyi.system.recommand.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 推荐曝光记录表 tb_recommend_exposure
 */
@Data
@TableName("tb_recommend_exposure")
public class TbRecommendExposure implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("user_id")
    private Long userId;

    @TableField("post_id")
    private Long postId;

    @TableField("biz_date")
    private Date bizDate;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;
}