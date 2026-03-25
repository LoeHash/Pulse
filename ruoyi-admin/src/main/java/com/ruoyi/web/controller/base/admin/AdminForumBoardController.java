package com.ruoyi.web.controller.base.admin;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RequireRole;
import com.ruoyi.common.constant.RequireRoleConstant;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.ForumBoard;
import com.ruoyi.common.core.domain.dto.ForumBoardCreateDTO;
import com.ruoyi.common.core.domain.dto.ForumBoardUpdateDTO;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.service.ForumBoardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "管理端-社区版块接口", description = "版块管理")
@RestController
@RequestMapping("/admin/forum/board")
public class AdminForumBoardController extends BaseController {

    private final ForumBoardService forumBoardService;

    public AdminForumBoardController(ForumBoardService forumBoardService) {
        this.forumBoardService = forumBoardService;
    }

    @RequireRole(RequireRoleConstant.ROLE_HIGHEST_CODE)
    @Operation(summary = "分页查询版块")
    @GetMapping("/page")
    public TableDataInfo page(ForumBoard query) {
        startPage();
        List<ForumBoard> list = forumBoardService.selectBoardList(query, false);
        return getDataTable(list);
    }

    @RequireRole(RequireRoleConstant.ROLE_HIGHEST_CODE)
    @Operation(summary = "获取版块详情")
    @GetMapping("/{id}")
    public AjaxResult detail(@PathVariable Long id) {
        return success(forumBoardService.getById(id));
    }

    @RequireRole(RequireRoleConstant.ROLE_HIGHEST_CODE)
    @Operation(summary = "新增版块")
    @Log(title = "社区版块", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Valid @RequestBody ForumBoardCreateDTO dto) {
        return success(forumBoardService.createBoard(dto));
    }

    @RequireRole(RequireRoleConstant.ROLE_HIGHEST_CODE)
    @Operation(summary = "修改版块")
    @Log(title = "社区版块", businessType = BusinessType.UPDATE)
    @PutMapping("/{id}")
    public AjaxResult edit(@PathVariable Long id, @RequestBody ForumBoardUpdateDTO dto) {
        try {
            return toAjax(forumBoardService.updateBoard(id, dto));
        } catch (IllegalArgumentException e) {
            return error(e.getMessage());
        }
    }

    @RequireRole(RequireRoleConstant.ROLE_HIGHEST_CODE)
    @Operation(summary = "删除版块")
    @Log(title = "社区版块", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    public AjaxResult remove(@PathVariable Long id) {
        return toAjax(forumBoardService.deleteBoard(id));
    }
}
