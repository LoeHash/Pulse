package com.ruoyi.web.controller.base.client;

import com.ruoyi.common.annotation.RequireRole;
import com.ruoyi.common.constant.RequireRoleConstant;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.ForumPost;
import com.ruoyi.common.core.domain.User;
import com.ruoyi.common.core.domain.dto.ForumPostCreateDTO;
import com.ruoyi.common.core.domain.dto.ForumPostQueryDTO;
import com.ruoyi.common.core.domain.dto.ForumPostUpdateDTO;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.UserHolder;
import com.ruoyi.system.service.ForumPostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "客户端-社区帖子接口", description = "帖子浏览与发帖")
@RestController
@RequestMapping("/client/forum/post-v2")
public class ClientForumPostController extends BaseController {

    private final ForumPostService forumPostService;

    public ClientForumPostController(ForumPostService forumPostService) {
        this.forumPostService = forumPostService;
    }

    @Operation(summary = "分页查询帖子（支持时间/热度排序）")
    @GetMapping("/page")
    public TableDataInfo page(ForumPostQueryDTO queryDTO) {
        startPage();
        List<ForumPost> list = forumPostService.selectPostListForClient(queryDTO);
        return getDataTable(list);
    }

    @Operation(summary = "帖子详情")
    @GetMapping("/{id}")
    public AjaxResult detail(@PathVariable Long id) {
        ForumPost post = forumPostService.getPostDetailForClient(id);
        if (post == null) {
            return error("帖子不存在或不可见");
        }
        return success(post);
    }

    @RequireRole(RequireRoleConstant.ROLE_NORMAL_CODE)
    @Operation(summary = "发布帖子")
    @PostMapping
    public AjaxResult add(@Valid @RequestBody ForumPostCreateDTO dto) {
        User currentUser = UserHolder.getUser();
        try {
            return success(forumPostService.createPostByClient(currentUser.getId(), dto));
        } catch (IllegalArgumentException e) {
            return error(e.getMessage());
        }
    }

    @RequireRole(RequireRoleConstant.ROLE_NORMAL_CODE)
    @Operation(summary = "修改我的帖子")
    @PutMapping("/{id}")
    public AjaxResult edit(@PathVariable Long id, @RequestBody ForumPostUpdateDTO dto) {
        User currentUser = UserHolder.getUser();
        try {
            return toAjax(forumPostService.updatePostByClient(currentUser.getId(), id, dto));
        } catch (IllegalArgumentException e) {
            return error(e.getMessage());
        }
    }

    @RequireRole(RequireRoleConstant.ROLE_NORMAL_CODE)
    @Operation(summary = "删除我的帖子")
    @DeleteMapping("/{id}")
    public AjaxResult remove(@PathVariable Long id) {
        User currentUser = UserHolder.getUser();
        return toAjax(forumPostService.deletePostByClient(currentUser.getId(), id));
    }

    @RequireRole(RequireRoleConstant.ROLE_NORMAL_CODE)
    @Operation(summary = "分页查询我的帖子")
    @GetMapping("/my/page")
    public TableDataInfo myPage(ForumPostQueryDTO queryDTO) {
        User currentUser = UserHolder.getUser();
        queryDTO.setUserId(currentUser.getId());
        startPage();
        List<ForumPost> list = forumPostService.selectPostListForAdmin(queryDTO);
        return getDataTable(list);
    }
}

