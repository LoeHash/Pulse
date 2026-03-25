package com.ruoyi.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.core.domain.Sport;
import com.ruoyi.system.mapper.SportMapper;
import com.ruoyi.system.service.SportService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class SportServiceImpl extends ServiceImpl<SportMapper, Sport> implements SportService {

    @Override
    public List<Sport> selectSportList(Sport query) {
        LambdaQueryWrapper<Sport> wrapper = new LambdaQueryWrapper<>();
        if (query != null) {
            if (StringUtils.hasText(query.getName())) {
                wrapper.like(Sport::getName, query.getName());
            }
            if (StringUtils.hasText(query.getSportType())) {
                wrapper.eq(Sport::getSportType, query.getSportType());
            }
            if (query.getStatus() != null) {
                wrapper.eq(Sport::getStatus, query.getStatus());
            }
        }
        wrapper.orderByDesc(Sport::getCreateTime);
        return this.list(wrapper);
    }

    @Override
    public List<Sport> selectSportListForClient(Sport query) {
        LambdaQueryWrapper<Sport> wrapper = new LambdaQueryWrapper<>();
        if (query != null) {
            if (StringUtils.hasText(query.getName())) {
                wrapper.like(Sport::getName, query.getName());
            }
            if (StringUtils.hasText(query.getSportType())) {
                wrapper.eq(Sport::getSportType, query.getSportType());
            }
        }
        wrapper.eq(Sport::getStatus, 1);
        wrapper.orderByDesc(Sport::getCreateTime);
        return this.list(wrapper);
    }
}

