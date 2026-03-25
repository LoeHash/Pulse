package com.ruoyi.system.recommand.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.system.recommand.domain.TbRecommendExposure;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RecommendExposureMapper extends BaseMapper<TbRecommendExposure> {

    /**
     * MySQL: 批量插入，遇到唯一键冲突忽略
     */
    int insertIgnoreBatch(@Param("list") List<TbRecommendExposure> list);
}