package com.ruoyi.system.recommand.datain.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.core.domain.UserProfile;
import com.ruoyi.system.mapper.UserProfileMapper;
import com.ruoyi.system.recommand.datain.AbstractUserPreferenceProvider;
import com.ruoyi.system.recommand.domain.UserPreferenceView;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 默认用户偏好提供者（
 * V1：直接读取 tb_user_profile 的 tag_pref/board_pref
 * V2：可在此处无缝改成“短期+长期融合”，不影响推荐调用方
 */
@Component
@RequiredArgsConstructor
public class DefaultUserPreferenceProvider implements AbstractUserPreferenceProvider {

    private final UserProfileMapper userProfileMapper;
    private static final com.fasterxml.jackson.databind.ObjectMapper MAPPER = new com.fasterxml.jackson.databind.ObjectMapper();

    @Override
    public UserPreferenceView getPreference(Long userId) {
        UserPreferenceView view = new UserPreferenceView();
        view.setUserId(userId);
        view.setTagPref(new HashMap<>());
        view.setBoardPref(new HashMap<>());
        view.setLastActiveTs(null);

        if (userId == null) {
            return view;
        }


        // 获取用户画像
        UserProfile profile = userProfileMapper.selectOne(
                new LambdaQueryWrapper<UserProfile>()
                        .eq(UserProfile::getUserId, userId)
                        .eq(UserProfile::getIsDeleted, 0)
                        .last("limit 1")
        );


        if (profile == null) {
            return view;
        }

        Map<String, Double> tagPref = parseToMap(profile.getTagPref());
        Map<String, Double> boardPref = parseToMap(profile.getBoardPref());

        view.setTagPref(new HashMap<>(tagPref));
        view.setBoardPref(new HashMap<>(boardPref));

        Date lastActive = profile.getLastActiveTime();
        view.setLastActiveTs(lastActive == null ? null : lastActive.getTime());
        return view;
    }




    private Map<String, Double> parseToMap(Object jsonObj) {
        if (jsonObj == null) return new HashMap<>();
        try {
            // 兼容：数据库字段可能是 String / JSONObject / LinkedHashMap
            String json = (jsonObj instanceof String)
                    ? (String) jsonObj
                    : MAPPER.writeValueAsString(jsonObj);

            if (json == null || json.isBlank()) return new HashMap<>();

            return MAPPER.readValue(
                    json,
                    new com.fasterxml.jackson.core.type.TypeReference<Map<String, Double>>() {}
            );
        } catch (Exception e) {
            return new HashMap<>();
        }
    }
}
