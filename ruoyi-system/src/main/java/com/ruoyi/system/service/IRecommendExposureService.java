package com.ruoyi.system.recommand.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.system.recommand.domain.TbRecommendExposure;

import java.util.List;
import java.util.Set;

public interface IRecommendExposureService extends IService<TbRecommendExposure> {
    Set<Long> getExposedPostIds(Long userId, int days);
    void saveExposure(Long userId, List<Long> postIds);
}