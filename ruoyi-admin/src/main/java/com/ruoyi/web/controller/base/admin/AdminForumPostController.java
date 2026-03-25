package com.ruoyi.web.controller.base.admin;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RequireRole;
import com.ruoyi.common.constant.RequireRoleConstant;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.ForumPost;
import com.ruoyi.common.core.domain.dto.ForumPostCreateDTO;
import com.ruoyi.common.core.domain.dto.ForumPostQueryDTO;
import com.ruoyi.common.core.domain.dto.ForumPostUpdateDTO;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.service.ForumPostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "管理端-社区帖子接口", description = "帖子管理")
@RestController
@RequestMapping("/admin/forum/post")
public class AdminForumPostController extends BaseController {

    private final ForumPostService forumPostService;

    public AdminForumPostController(ForumPostService forumPostService) {
        this.forumPostService = forumPostService;
    }

    @RequireRole(RequireRoleConstant.ROLE_HIGHEST_CODE)
    @Operation(summary = "分页查询帖子（支持时间/热度排序）")
    @GetMapping("/page")
    public TableDataInfo page(ForumPostQueryDTO queryDTO) {
        startPage();
        List<ForumPost> list = forumPostService.selectPostListForAdmin(queryDTO);
        return getDataTable(list);
    }

    @RequireRole(RequireRoleConstant.ROLE_HIGHEST_CODE)
    @Operation(summary = "帖子详情")
    @GetMapping("/{id}")
    public AjaxResult detail(@PathVariable Long id) {
        return success(forumPostService.getPostDetailForAdmin(id));
    }

    @RequireRole(RequireRoleConstant.ROLE_HIGHEST_CODE)
    @Operation(summary = "新增帖子")
    @Log(title = "社区帖子", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Valid @RequestBody ForumPostCreateDTO dto) {
        try {
            return success(forumPostService.createPostByAdmin(dto));
        } catch (IllegalArgumentException e) {
            return error(e.getMessage());
        }
    }

    @RequireRole(RequireRoleConstant.ROLE_HIGHEST_CODE)
    @Operation(summary = "修改帖子")
    @Log(title = "社区帖子", businessType = BusinessType.UPDATE)
    @PutMapping("/{id}")
    public AjaxResult edit(@PathVariable Long id, @RequestBody ForumPostUpdateDTO dto) {
        try {
            return toAjax(forumPostService.updatePostByAdmin(id, dto));
        } catch (IllegalArgumentException e) {
            return error(e.getMessage());
        }
    }

    @RequireRole(RequireRoleConstant.ROLE_HIGHEST_CODE)
    @Operation(summary = "删除帖子")
    @Log(title = "社区帖子", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    public AjaxResult remove(@PathVariable Long id) {
        return toAjax(forumPostService.deletePostByAdmin(id));
    }
}
