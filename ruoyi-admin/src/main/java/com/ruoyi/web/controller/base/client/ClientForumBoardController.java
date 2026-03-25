package com.ruoyi.web.controller.base.client;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.ForumBoard;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.service.ForumBoardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "客户端-社区版块接口", description = "版块浏览")
@RestController
@RequestMapping("/client/forum/board")
public class ClientForumBoardController extends BaseController {

    private final ForumBoardService forumBoardService;

    public ClientForumBoardController(ForumBoardService forumBoardService) {
        this.forumBoardService = forumBoardService;
    }

    @Operation(summary = "分页查询版块")
    @GetMapping("/page")
    public TableDataInfo page(ForumBoard query) {
        startPage();
        List<ForumBoard> list = forumBoardService.selectBoardList(query, true);
        return getDataTable(list);
    }

    @Operation(summary = "版块详情")
    @GetMapping("/{id}")
    public AjaxResult detail(@PathVariable Long id) {
        ForumBoard board = forumBoardService.getById(id);
        if (board == null || !Integer.valueOf(1).equals(board.getStatus())) {
            return error("版块不存在或已禁用");
        }
        return success(board);
    }
}

