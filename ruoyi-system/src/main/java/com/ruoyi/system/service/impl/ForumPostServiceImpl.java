package com.ruoyi.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.core.domain.ForumBoard;
import com.ruoyi.common.core.domain.ForumPost;
import com.ruoyi.common.core.domain.dto.ForumPostCreateDTO;
import com.ruoyi.common.core.domain.dto.ForumPostQueryDTO;
import com.ruoyi.common.core.domain.dto.ForumPostUpdateDTO;
import com.ruoyi.common.utils.TagConditionUtils;
import com.ruoyi.system.mapper.ForumBoardMapper;
import com.ruoyi.system.mapper.ForumPostMapper;
import com.ruoyi.system.service.ForumPostService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class ForumPostServiceImpl extends ServiceImpl<ForumPostMapper, ForumPost> implements ForumPostService {

    private final ForumBoardMapper forumBoardMapper;

    public ForumPostServiceImpl(ForumBoardMapper forumBoardMapper) {
        this.forumBoardMapper = forumBoardMapper;
    }

    @Override
    public List<ForumPost> selectPostListForAdmin(ForumPostQueryDTO queryDTO) {
        LambdaQueryWrapper<ForumPost> wrapper = buildPostQuery(queryDTO, false);
        return this.list(wrapper);
    }

    @Override
    public List<ForumPost> selectPostListForClient(ForumPostQueryDTO queryDTO) {
        LambdaQueryWrapper<ForumPost> wrapper = buildPostQuery(queryDTO, true);
        return this.list(wrapper);
    }

    @Override
    public ForumPost getPostDetailForAdmin(Long id) {
        return this.getById(id);
    }

    @Override
    public ForumPost getPostDetailForClient(Long id) {
        return this.getOne(new LambdaQueryWrapper<ForumPost>()
                .eq(ForumPost::getId, id)
                .eq(ForumPost::getStatus, 1));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createPostByAdmin(ForumPostCreateDTO dto) {
        ensureBoardExists(dto.getBoardId());

        ForumPost entity = new ForumPost();
        BeanUtils.copyProperties(dto, entity);
        if (entity.getStatus() == null) {
            entity.setStatus(1);
        }
        initCounterFields(entity);
        entity.setCreateTime(new Date());
        entity.setUpdateTime(new Date());
        this.save(entity);
        return entity.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createPostByClient(Long userId, ForumPostCreateDTO dto) {
        ensureBoardExists(dto.getBoardId());

        ForumPost entity = new ForumPost();
        BeanUtils.copyProperties(dto, entity);
        entity.setUserId(userId);
        if (entity.getStatus() == null) {
            entity.setStatus(1);
        }
        initCounterFields(entity);
        entity.setCreateTime(new Date());
        entity.setUpdateTime(new Date());
        this.save(entity);
        return entity.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updatePostByAdmin(Long id, ForumPostUpdateDTO dto) {
        ForumPost db = this.getById(id);
        if (db == null) {
            throw new IllegalArgumentException("帖子不存在");
        }
        if (dto.getBoardId() != null) {
            ensureBoardExists(dto.getBoardId());
        }
        ForumPost entity = new ForumPost();
        BeanUtils.copyProperties(dto, entity);
        entity.setId(id);
        entity.setUpdateTime(new Date());
        return this.baseMapper.updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updatePostByClient(Long userId, Long id, ForumPostUpdateDTO dto) {
        ForumPost db = this.getById(id);
        if (db == null) {
            throw new IllegalArgumentException("帖子不存在");
        }
        if (!userId.equals(db.getUserId())) {
            throw new IllegalArgumentException("无权修改他人帖子");
        }
        if (dto.getBoardId() != null) {
            ensureBoardExists(dto.getBoardId());
        }
        ForumPost entity = new ForumPost();
        BeanUtils.copyProperties(dto, entity);
        entity.setId(id);
        entity.setUserId(userId);
        entity.setUpdateTime(new Date());
        return this.baseMapper.updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deletePostByAdmin(Long id) {
        return this.baseMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deletePostByClient(Long userId, Long id) {
        return this.baseMapper.delete(new LambdaQueryWrapper<ForumPost>()
                .eq(ForumPost::getId, id)
                .eq(ForumPost::getUserId, userId));
    }

    private LambdaQueryWrapper<ForumPost> buildPostQuery(ForumPostQueryDTO queryDTO, boolean clientOnlyPublished) {
        LambdaQueryWrapper<ForumPost> wrapper = new LambdaQueryWrapper<>();
        if (queryDTO != null) {
            if (queryDTO.getBoardId() != null) {
                wrapper.eq(ForumPost::getBoardId, queryDTO.getBoardId());
            }
            if (queryDTO.getUserId() != null) {
                wrapper.eq(ForumPost::getUserId, queryDTO.getUserId());
            }
            if (StringUtils.hasText(queryDTO.getTitle())) {
                wrapper.like(ForumPost::getTitle, queryDTO.getTitle());
            }
            if (queryDTO.getStatus() != null) {
                wrapper.eq(ForumPost::getStatus, queryDTO.getStatus());
            }
            if (queryDTO.getTags() != null && !queryDTO.getTags().isEmpty()) {
                TagConditionUtils.applyJsonLikeTags(wrapper, ForumPost::getTags, queryDTO.getTags());
            }
        }

        if (clientOnlyPublished) {
            wrapper.eq(ForumPost::getStatus, 1);
        }

        String sortBy = queryDTO == null ? null : queryDTO.getSortBy();
        if ("hot".equalsIgnoreCase(sortBy)) {
            wrapper.orderByDesc(ForumPost::getHotScore).orderByDesc(ForumPost::getCreateTime);
        } else {
            wrapper.orderByDesc(ForumPost::getCreateTime);
        }
        return wrapper;
    }

    private void initCounterFields(ForumPost entity) {
        if (entity.getViewCount() == null) {
            entity.setViewCount(0L);
        }
        if (entity.getLikeCount() == null) {
            entity.setLikeCount(0L);
        }
        if (entity.getCommentCount() == null) {
            entity.setCommentCount(0L);
        }
        if (entity.getFavoriteCount() == null) {
            entity.setFavoriteCount(0L);
        }
        if (entity.getHotScore() == null) {
            entity.setHotScore(BigDecimal.ZERO);
        }
    }

    private void ensureBoardExists(Long boardId) {
        ForumBoard board = forumBoardMapper.selectById(boardId);
        if (board == null) {
            throw new IllegalArgumentException("所属版块不存在");
        }
    }
}

