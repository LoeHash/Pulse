
package com.ruoyi.web.controller.base.admin;

import com.ruoyi.common.annotation.RequireRole;
import com.ruoyi.common.constant.RequireRoleConstant;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.UserEvent;
import com.ruoyi.common.core.domain.dto.UserEventQueryDTO;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.service.UserEventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 管理端推荐事件接口
 */
@Tag(name = "管理端-用户行为事件接口", description = "用户行为事件管理")
@RestController
@RequestMapping("/admin/recommend/event")
public class AdminRecommendEventController extends BaseController {

    private final UserEventService userEventService;

    public AdminRecommendEventController(UserEventService userEventService) {
        this.userEventService = userEventService;
    }

    @RequireRole(RequireRoleConstant.ROLE_HIGHEST_CODE)
    @Operation(summary = "分页查询用户行为事件")
    @GetMapping("/page")
    public TableDataInfo page(UserEventQueryDTO queryDTO) {
        // 分页必须在首次 ORM 调用前声明
        startPage();
        List<UserEvent> list = userEventService.selectUserEventListForAdmin(queryDTO);
        return getDataTable(list);
    }

    @RequireRole(RequireRoleConstant.ROLE_HIGHEST_CODE)
    @Operation(summary = "查询用户行为事件详情")
    @GetMapping("/{id}")
    public AjaxResult detail(@PathVariable Long id) {
        UserEvent event = userEventService.getUserEventByIdForAdmin(id);
        if (event == null) {
            return error("用户行为事件不存在");
        }
        return success(event);
    }

    @RequireRole(RequireRoleConstant.ROLE_HIGHEST_CODE)
    @Operation(summary = "新增用户行为事件")
    @PostMapping
    public AjaxResult add(@RequestBody UserEvent userEvent) {
        return toAjax(userEventService.createUserEventByAdmin(userEvent));
    }

    @RequireRole(RequireRoleConstant.ROLE_HIGHEST_CODE)
    @Operation(summary = "修改用户行为事件")
    @PutMapping("/{id}")
    public AjaxResult edit(@PathVariable Long id, @RequestBody UserEvent userEvent) {
        try {
            return toAjax(userEventService.updateUserEventByAdmin(id, userEvent));
        } catch (IllegalArgumentException e) {
            return error(e.getMessage());
        }
    }

    @RequireRole(RequireRoleConstant.ROLE_HIGHEST_CODE)
    @Operation(summary = "删除用户行为事件")
    @DeleteMapping("/{id}")
    public AjaxResult remove(@PathVariable Long id) {
        return toAjax(userEventService.deleteUserEventByAdmin(id));
    }

    @RequireRole(RequireRoleConstant.ROLE_HIGHEST_CODE)
    @Operation(summary = "批量删除用户行为事件")
    @DeleteMapping("/{ids}")
    public AjaxResult removeBatch(@PathVariable List<Long> ids) {
        return toAjax(userEventService.deleteUserEventByIds(ids));
    }
}

