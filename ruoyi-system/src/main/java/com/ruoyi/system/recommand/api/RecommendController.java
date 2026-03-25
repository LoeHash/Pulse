package com.ruoyi.system.recommand.api;

import com.ruoyi.common.annotation.RequireRole;
import com.ruoyi.common.constant.RequireRoleConstant;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.UserHolder;
import com.ruoyi.system.recommand.api.domain.dto.RecommendRequest;
import com.ruoyi.system.recommand.api.domain.vo.RecommendItemVO;
import com.ruoyi.system.recommand.domain.RankedPost;
import com.ruoyi.system.recommand.rank.RankService;
import com.ruoyi.system.recommand.service.IRecommendExposureService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 推荐接口
 */
@RestController
@Tag(name = "客户端-推荐接口", description = "推荐用户")
@RequestMapping("/client/recommend")
@RequiredArgsConstructor
public class RecommendController extends BaseController {

    private final RankService rankService;
    private final IRecommendExposureService exposureService;

    /**
     * 给当前用户推荐帖子
     * POST /system/recommend/posts
     */
    @PostMapping("/feed")
    @RequireRole(RequireRoleConstant.ROLE_NORMAL_CODE)
    @Operation(summary = "查询动作列表")
    public AjaxResult recommendPosts(@Validated @RequestBody(required = false) RecommendRequest req) {
        Long userId = UserHolder.getUser().getId();
        if (userId == null) {
            return AjaxResult.error("未登录或无法识别用户");
        }

        int size = 20;

        if (req != null && req.getSize() != null) {
            size = Math.max(1, Math.min(req.getSize(), 100)); // 限制1~100
        }

        // 1) 排序层产出推荐
        List<RankedPost> ranked = rankService.rank(userId, size);

        // 2) 记录曝光（只记录实际下发）
        List<Long> exposedPostIds = ranked.stream()
                .map(RankedPost::getPostId)
                .collect(Collectors.toList());
        exposureService.saveExposure(userId, exposedPostIds);

        // 3) 返回前端 包装post!
        List<RecommendItemVO> data = ranked.stream()
                .map(x -> new RecommendItemVO(x.getPostId(), x.getRankScore()))
                .collect(Collectors.toList());

        return AjaxResult.success(data);
    }
}