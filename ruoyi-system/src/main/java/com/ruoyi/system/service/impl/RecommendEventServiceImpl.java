package com.ruoyi.system.service.impl;


import com.ruoyi.common.core.domain.*;
import com.ruoyi.common.enums.EventTypeWeight;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.UserHolder;
import com.ruoyi.system.mapper.ForumPostMapper;
import com.ruoyi.system.mapper.UserEventMapper;
import com.ruoyi.system.mapper.UserProfileMapper;
import com.ruoyi.system.service.RecommendEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 推荐事件服务实现类
 *
 * 处理流程：
 * 1. 参数校验 + 获取当前登录用户
 * 2. 保证用户画像存在（不存在则初始化）
 * 3. 遍历事件：事件落库 -> 计算事件分 -> 更新标签偏好/版块偏好
 * 4. 轻衰减历史偏好，避免旧兴趣长期主导
 * 5. 更新用户画像
 */
@Service
@RequiredArgsConstructor
public class RecommendEventServiceImpl implements RecommendEventService {

    /** 用户行为事件 Mapper */
    private final UserEventMapper userEventMapper;
    /** 用户画像 Mapper */
    private final UserProfileMapper userProfileMapper;
    /** 帖子 Mapper（用于读取帖子标签和版块） */
    private final ForumPostMapper postMapper;

    /**
     * 批量处理事件（事务保证一致性）
     *
     * @param req 批量事件请求
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void handleBatch(UserEventBatchReq req) {
        // 1) 空请求直接返回
        if (req == null || req.getEvents() == null || req.getEvents().isEmpty()) {
            return;
        }

        // 2) 获取当前登录用户ID
        Long userId = UserHolder.getUser().getId();
        if (userId == null) {
            return;
        }

        // 3) 查询用户画像，不存在则初始化
        UserProfile profile = userProfileMapper.selectByUserId(userId);
        if (profile == null) {
            profile = new UserProfile();
            profile.setUserId(userId);
            profile.setTagPref(new HashMap<>());
            profile.setBoardPref(new HashMap<>());
            profile.setProfileVersion(1);
            userProfileMapper.insert(profile);
        }

        // 4) 取出偏好Map，避免空指针
        Map<String, Double> tagPref = profile.getTagPref() == null ? new HashMap<>() : profile.getTagPref();
        Map<String, Double> boardPref = profile.getBoardPref() == null ? new HashMap<>() : profile.getBoardPref();

        // 5) 遍历每条事件
        List<UserEventReq> events = req.getEvents();
        for (UserEventReq e : events) {

            // 基础校验：事件对象、帖子ID、事件类型合法性
            if (e == null || e.getPostId() == null || !EventTypeWeight.isValid(e.getEventType())) {
                continue;
            }

            // 时长清洗（避免异常值污染画像）
            long durationMs = sanitizeDuration(e.getDurationMs());

            // 5.1 落库行为明细
            UserEvent event = new UserEvent();
            event.setUserId(userId);
            event.setPostId(e.getPostId());
            event.setEventType(e.getEventType());
            event.setScene(e.getScene());
            event.setPageNo(e.getPageNo());
            event.setStartTs(e.getStartTs());
            event.setEndTs(e.getEndTs());
            event.setDurationMs(durationMs);
            event.setEventTime(new Date());
            userEventMapper.insert(event);

            // 5.2 查询帖子信息（用于更新偏好）
            ForumPost post = postMapper.selectById(e.getPostId());
            if (post == null) {
                continue;
            }

            // 5.3 计算事件分（如点赞高于点击，评论高于点赞）
            double score = EventTypeWeight.score(e.getEventType(), durationMs);

            // 5.4 更新标签偏好：帖子有几个标签就分别加分
            if (post.getTags() != null) {
                for (String tag : post.getTags()) {
                    tagPref.put(tag, tagPref.getOrDefault(tag, 0.0D) + score);
                }
            }

            // 5.5 更新版块偏好
            if (post.getBoardId() != null) {
                String boardKey = String.valueOf(post.getBoardId());
                boardPref.put(boardKey, boardPref.getOrDefault(boardKey, 0.0D) + score);
            }
        }

        // 6) 轻微衰减（每次*0.99），防止旧兴趣永久占优
        decay(tagPref, 0.99D);
        decay(boardPref, 0.99D);

        // 7) 回写画像
        profile.setTagPref(tagPref);
        profile.setBoardPref(boardPref);
        profile.setLastActiveTime(new Date());
        userProfileMapper.updateById(profile);
    }

    /**
     * 时长清洗
     * 规则：
     * - null 或负数 -> 0
     * - 超过10分钟 -> 截断到10分钟
     *
     * @param durationMs 原始时长
     * @return 清洗后时长
     */
    private long sanitizeDuration(Long durationMs) {
        if (durationMs == null || durationMs < 0) {
            return 0L;
        }
        return Math.min(durationMs, 10 * 60 * 1000L);
    }

    /**
     * 对偏好分做统一衰减
     *
     * @param map    偏好map（标签或版块）
     * @param factor 衰减系数（0~1）
     */
    private void decay(Map<String, Double> map, double factor) {
        for (Map.Entry<String, Double> entry : map.entrySet()) {
            entry.setValue(entry.getValue() * factor);
        }
    }
}