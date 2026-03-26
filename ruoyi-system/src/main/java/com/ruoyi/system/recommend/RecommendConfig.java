package com.ruoyi.system.recommend;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RecommendConfig {

    @Value("${recommendation.user-profile-provider-bean-name:userPreferenceProvider}")
    private static final String USER_PROFILE_PROVIDER_BEAN_NAME = "userPreferenceProvider";

}
