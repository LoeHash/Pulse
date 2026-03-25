package com.ruoyi.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.core.domain.ForumBoard;
import com.ruoyi.common.core.domain.dto.ForumBoardCreateDTO;
import com.ruoyi.common.core.domain.dto.ForumBoardUpdateDTO;
import com.ruoyi.system.mapper.ForumBoardMapper;
import com.ruoyi.system.service.ForumBoardService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

@Service
public class ForumBoardServiceImpl extends ServiceImpl<ForumBoardMapper, ForumBoard> implements ForumBoardService {

    @Override
    public List<ForumBoard> selectBoardList(ForumBoard query, boolean clientOnlyEnabled) {
        LambdaQueryWrapper<ForumBoard> wrapper = new LambdaQueryWrapper<>();
        if (query != null) {
            if (StringUtils.hasText(query.getName())) {
                wrapper.like(ForumBoard::getName, query.getName());
            }
            if (query.getStatus() != null) {
                wrapper.eq(ForumBoard::getStatus, query.getStatus());
            }
        }
        if (clientOnlyEnabled) {
            wrapper.eq(ForumBoard::getStatus, 1);
        }
        wrapper.orderByAsc(ForumBoard::getSort).orderByDesc(ForumBoard::getCreateTime);
        return this.list(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createBoard(ForumBoardCreateDTO dto) {
        ForumBoard entity = new ForumBoard();
        BeanUtils.copyProperties(dto, entity);
        entity.setPostCount(entity.getPostCount() == null ? 0L : entity.getPostCount());
        entity.setFollowerCount(entity.getFollowerCount() == null ? 0L : entity.getFollowerCount());
        entity.setCreateTime(new Date());
        entity.setUpdateTime(new Date());
        this.save(entity);
        return entity.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateBoard(Long id, ForumBoardUpdateDTO dto) {
        ForumBoard db = this.getById(id);
        if (db == null) {
            throw new IllegalArgumentException("版块不存在");
        }
        ForumBoard entity = new ForumBoard();
        BeanUtils.copyProperties(dto, entity);
        entity.setId(id);
        entity.setUpdateTime(new Date());
        return this.baseMapper.updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteBoard(Long id) {
        return this.baseMapper.deleteById(id);
    }
}

