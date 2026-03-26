package com.ruoyi.web.controller.base.admin;

import com.ruoyi.common.annotation.RequireRole;
import com.ruoyi.common.constant.RequireRoleConstant;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.UserProfile;
import com.ruoyi.common.core.domain.dto.UserProfileQueryDTO;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.service.UserProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 管理端用户画像接口
 */
@Tag(name = "管理端-用户画像接口", description = "用户画像管理")
@RestController
@RequestMapping("/admin/user-profile")
public class AdminUserProfileController extends BaseController {

    private final UserProfileService userProfileService;

    public AdminUserProfileController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @RequireRole(RequireRoleConstant.ROLE_HIGHEST_CODE)
    @Operation(summary = "分页查询用户画像")
    @GetMapping("/page")
    public TableDataInfo page(UserProfileQueryDTO queryDTO) {
        // 分页必须在首次 ORM 调用前声明
        startPage();
        List<UserProfile> list = userProfileService.selectUserProfileListForAdmin(queryDTO);
        return getDataTable(list);
    }

    @RequireRole(RequireRoleConstant.ROLE_HIGHEST_CODE)
    @Operation(summary = "查询用户画像详情")
    @GetMapping("/{id}")
    public AjaxResult detail(@PathVariable Long id) {
        UserProfile profile = userProfileService.getUserProfileByIdForAdmin(id);
        if (profile == null) {
            return error("用户画像不存在");
        }
        return success(profile);
    }

    @RequireRole(RequireRoleConstant.ROLE_HIGHEST_CODE)
    @Operation(summary = "新增用户画像")
    @PostMapping
    public AjaxResult add(@RequestBody UserProfile profile) {
        return toAjax(userProfileService.createUserProfileByAdmin(profile));
    }

    @RequireRole(RequireRoleConstant.ROLE_HIGHEST_CODE)
    @Operation(summary = "修改用户画像")
    @PutMapping("/{id}")
    public AjaxResult edit(@PathVariable Long id, @RequestBody UserProfile profile) {
        try {
            return toAjax(userProfileService.updateUserProfileByAdmin(id, profile));
        } catch (IllegalArgumentException e) {
            return error(e.getMessage());
        }
    }

    @RequireRole(RequireRoleConstant.ROLE_HIGHEST_CODE)
    @Operation(summary = "删除用户画像")
    @DeleteMapping("/{id}")
    public AjaxResult remove(@PathVariable Long id) {
        return toAjax(userProfileService.deleteUserProfileByAdmin(id));
    }
}

