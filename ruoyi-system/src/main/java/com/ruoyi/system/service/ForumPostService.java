package com.ruoyi.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.common.core.domain.ForumPost;
import com.ruoyi.common.core.domain.dto.ForumPostCreateDTO;
import com.ruoyi.common.core.domain.dto.ForumPostQueryDTO;
import com.ruoyi.common.core.domain.dto.ForumPostUpdateDTO;

import java.util.List;

public interface ForumPostService extends IService<ForumPost> {

    List<ForumPost> selectPostListForAdmin(ForumPostQueryDTO queryDTO);

    List<ForumPost> selectPostListForClient(ForumPostQueryDTO queryDTO);

    ForumPost getPostDetailForAdmin(Long id);

    ForumPost getPostDetailForClient(Long id);

    Long createPostByAdmin(ForumPostCreateDTO dto);

    Long createPostByClient(Long userId, ForumPostCreateDTO dto);

    int updatePostByAdmin(Long id, ForumPostUpdateDTO dto);

    int updatePostByClient(Long userId, Long id, ForumPostUpdateDTO dto);

    int deletePostByAdmin(Long id);

    int deletePostByClient(Long userId, Long id);
}

