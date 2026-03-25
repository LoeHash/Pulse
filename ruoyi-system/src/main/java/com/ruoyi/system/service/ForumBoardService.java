package com.ruoyi.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.common.core.domain.ForumBoard;
import com.ruoyi.common.core.domain.dto.ForumBoardCreateDTO;
import com.ruoyi.common.core.domain.dto.ForumBoardUpdateDTO;

import java.util.List;

public interface ForumBoardService extends IService<ForumBoard> {

    List<ForumBoard> selectBoardList(ForumBoard query, boolean clientOnlyEnabled);

    Long createBoard(ForumBoardCreateDTO dto);

    int updateBoard(Long id, ForumBoardUpdateDTO dto);

    int deleteBoard(Long id);
}

