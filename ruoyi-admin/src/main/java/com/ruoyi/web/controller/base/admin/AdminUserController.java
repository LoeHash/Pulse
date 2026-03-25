package com.ruoyi.web.controller.base.admin;

import com.ruoyi.common.annotation.RequireRole;
import com.ruoyi.common.constant.RequireRoleConstant;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.User;
import com.ruoyi.common.core.domain.dto.UserAdminUpdateDTO;
import com.ruoyi.common.core.domain.dto.UserCreateDTO;
import com.ruoyi.common.core.domain.dto.UserPageQueryDTO;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.VoUtils;
import com.ruoyi.system.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 管理端-用户管理Controller
 */
@Tag(name = "管理端-用户接口", description = "用户基础管理")
@RestController
@RequestMapping("/admin/user")
@Validated
public class AdminUserController extends BaseController {

    @Autowired
    private UserService userService;

    /**
     * 管理端新增用户
     */
    @RequireRole(RequireRoleConstant.ROLE_HIGHEST_CODE)
    @Operation(summary = "新增用户")
    @PostMapping
    public AjaxResult add(@Valid @RequestBody UserCreateDTO dto) {
        try {
            Long id = userService.createUserByAdmin(dto);
            return AjaxResult.success("新增成功", id);
        } catch (IllegalArgumentException e) {
            return error(e.getMessage());
        }
    }

    /**
     * 管理端删除用户（软删除）
     */
    @RequireRole(RequireRoleConstant.ROLE_HIGHEST_CODE)
    @Operation(summary = "删除用户")
    @DeleteMapping("/{id}")
    public AjaxResult remove(
            @Parameter(description = "用户ID", required = true, example = "1")
            @PathVariable Long id) {
        return toAjax(userService.deleteUserByAdmin(id));
    }

    /**
     * 管理端修改用户
     */
    @RequireRole(RequireRoleConstant.ROLE_HIGHEST_CODE)
    @Operation(summary = "修改用户")
    @PutMapping("/{id}")
    public AjaxResult edit(
            @Parameter(description = "用户ID", required = true, example = "1")
            @PathVariable Long id,
            @Valid @RequestBody UserAdminUpdateDTO dto) {
        try {
            return toAjax(userService.updateUserByAdmin(id, dto));
        } catch (IllegalArgumentException e) {
            return error(e.getMessage());
        }
    }

    /**
     * 管理端获取用户详情
     */
    @RequireRole(RequireRoleConstant.ROLE_HIGHEST_CODE)
    @Operation(summary = "获取用户详情")
    @GetMapping("/{id}")
    public AjaxResult getInfo(
            @Parameter(description = "用户ID", required = true, example = "1")
            @PathVariable Long id) {
        User user = userService.getUserDetailByAdmin(id);
        if (user == null) {
            return error("用户不存在");
        }
        return success(VoUtils.transUserToVo(user));
    }



    /**
     * 管理端分页查询用户
     */
    @RequireRole(RequireRoleConstant.ROLE_HIGHEST_CODE)
    @Operation(summary = "分页查询用户")
    @GetMapping("/page")
    public TableDataInfo page(UserPageQueryDTO queryDTO) {
        startPage();
        User query = new User();
        BeanUtils.copyProperties(queryDTO, query);

        List<User> userList = userService.selectUserList(query);
        List<?> voList = userList.stream().map(VoUtils::transUserToVo).toList();
        return getDataTable(voList);
    }
}

