package com.ruoyi.web.controller.base.client;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.Action;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.User;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.UserHolder;
import com.ruoyi.system.service.ActionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 客户端动作接口
 */
@Tag(name = "客户端-动作接口", description = "动作库浏览")
@RestController
@RequestMapping("/client/action")
public class ClientActionController extends BaseController {

    @Autowired
    private ActionService actionService;

    @Operation(summary = "动作列表")
    @GetMapping("/list")
    public TableDataInfo list(Action action) {
        User user = UserHolder.getUser();
        Long userId = (user != null) ? user.getId() : null;

        startPage();
        List<Action> list = actionService.selectActionListForClient(action, userId);
        return getDataTable(list);
    }

    @Operation(summary = "动作详情")
    @GetMapping("/{id}")
    public AjaxResult detail(@Parameter(description = "动作ID", required = true) @PathVariable("id") Long id) {
        Action action = actionService.getById(id);
        if (action == null || (action.getIsDeleted() != null && action.getIsDeleted() == 1)) {
            return error("动作不存在");
        }
        // 简单校验权限：如果是系统动作(createUserId==null)所有人可见
        // 如果是私有动作(createUserId!=null)，只有创建者可见
        if (action.getCreateUserId() != null) {
            User user = UserHolder.getUser();
            if (user == null || !action.getCreateUserId().equals(user.getId())) {
               return error("无权访问该动作");
            }
        }
        
        return validResult(action);
    }
    
    // 简单的成功返回包装
    private AjaxResult validResult(Action action) {
        if (action.getStatus() != null && action.getStatus() == 0) {
             return error("动作已停用");
        }
        return success(action);
    }
}

