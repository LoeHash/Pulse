package com.ruoyi.web.controller.base.admin;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.annotation.RequireRole;
import com.ruoyi.common.constant.RequireRoleConstant;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.dto.ConfigUpdateDTO;
import com.ruoyi.system.config.service.ConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "管理端-动态配置接口", description = "动态配置")
@RestController
@RequestMapping("/admin/config")
public class AdminConfigController extends BaseController {

    @Autowired
    private ConfigService configService;

    /**
     * 更新配置
     */
    @RequireRole(RequireRoleConstant.ROLE_HIGHEST_CODE)
    @Operation(summary = "更新配置")
    @PostMapping("/update")
    public AjaxResult update(@RequestBody ConfigUpdateDTO dto) {

        if (dto.getKey() == null || dto.getValue() == null) {
            throw new IllegalArgumentException("key/value不能为空");
        }

        // 直接存 JSON（通用）
        configService.update(dto.getKey(), parseJson(dto.getValue()));

        return AjaxResult.success();
    }

    /**
     * 查询配置
     */
    @RequireRole(RequireRoleConstant.ROLE_HIGHEST_CODE)
    @Operation(summary = "查询配置")
    @GetMapping("/get")
    public AjaxResult get(@RequestParam String key) {
        return AjaxResult.success(configService.getConfig(key, String.class));
    }

    private String parseJson(Object obj) {

        try {
            return JSONObject.toJSONString(obj);
        } catch (Exception e) {
            throw new RuntimeException("JSON格式错误", e);
        }
    }
}