package com.ruoyi.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.system.recommand.domain.TbRecommendExposure;
import com.ruoyi.system.recommand.mapper.RecommendExposureMapper;
import com.ruoyi.system.recommand.service.IRecommendExposureService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecommendExposureServiceImpl
        extends ServiceImpl<RecommendExposureMapper, TbRecommendExposure>
        implements IRecommendExposureService {

    private final RecommendExposureMapper exposureMapper;

    @Override
    public Set<Long> getExposedPostIds(Long userId, int days) {
        if (userId == null || days <= 0) return Collections.emptySet();

        Date start = Date.from(
                LocalDate.now().minusDays(days).atStartOfDay(ZoneId.systemDefault()).toInstant()
        );

        return this.list(new LambdaQueryWrapper<TbRecommendExposure>()
                        .select(TbRecommendExposure::getPostId)
                        .eq(TbRecommendExposure::getUserId, userId)
                        .ge(TbRecommendExposure::getCreateTime, start))
                .stream()
                .map(TbRecommendExposure::getPostId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveExposure(Long userId, List<Long> postIds) {
        if (userId == null || postIds == null || postIds.isEmpty()) return;

        Date now = new Date();
        Date bizDate = now;

        List<TbRecommendExposure> rows = postIds.stream()
                .filter(Objects::nonNull)
                .distinct()
                .map(pid -> {
                    TbRecommendExposure e = new TbRecommendExposure();
                    e.setUserId(userId);
                    e.setPostId(pid);
                    e.setBizDate(bizDate);
                    e.setCreateTime(now);
                    return e;
                })
                .collect(Collectors.toList());

        if (!rows.isEmpty()) {
            exposureMapper.insertIgnoreBatch(rows);
        }
    }
}