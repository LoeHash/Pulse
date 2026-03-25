package com.ruoyi.web.controller.base.client;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.Sport;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.service.SportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "客户端-运动接口", description = "运动栏目")
@RestController
@RequestMapping("/client/sport")
public class ClientSportController extends BaseController {

    private final SportService sportService;

    public ClientSportController(SportService sportService) {
        this.sportService = sportService;
    }

    @Operation(summary = "运动列表")
    @GetMapping("/list")
    public TableDataInfo list(Sport query) {
        startPage();
        List<Sport> list = sportService.selectSportListForClient(query);
        return getDataTable(list);
    }

    @Operation(summary = "运动详情")
    @GetMapping("/{id}")
    public AjaxResult detail(@PathVariable Long id) {
        Sport sport = sportService.getById(id);
        if (sport == null || !Integer.valueOf(1).equals(sport.getStatus())) {
            return error("运动不存在或已禁用");
        }
        return success(sport);
    }
}

