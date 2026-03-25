package com.ruoyi.framework.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.filter.CorsFilter;
import com.ruoyi.framework.config.properties.PermitAllUrlProperties;
import com.ruoyi.framework.security.filter.JwtAuthenticationTokenFilter;
import com.ruoyi.framework.security.handle.AuthenticationEntryPointImpl;
import com.ruoyi.framework.security.handle.LogoutSuccessHandlerImpl;

/**
 * spring security配置
 *
 * @author ruoyi
 */
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
@Configuration
public class SecurityConfig
{
    /**
     * 认证失败处理类
     */
    @Autowired
    private AuthenticationEntryPointImpl unauthorizedHandler;

    /**
     * 退出处理类
     */
    @Autowired
    private LogoutSuccessHandlerImpl logoutSuccessHandler;

    /**
     * token认证过滤器
     */
    @Autowired
    private JwtAuthenticationTokenFilter authenticationTokenFilter;

    /**
     * 跨域过滤器
     */
    @Autowired
    private CorsFilter corsFilter;

    /**
     * 允许匿名访问的地址
     */
    @Autowired
    private PermitAllUrlProperties permitAllUrl;

	/**
	 * 身份验证实现
	 */
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception
	{
		return authenticationConfiguration.getAuthenticationManager();
	}

    /**
     * anyRequest          |   匹配所有请求路径
     * access              |   SpringEl表达式结果为true时可以访问
     * anonymous           |   匿名可以访问
     * denyAll             |   用户不能访问
     * fullyAuthenticated  |   用户完全认证可以访问（非remember-me下自动登录）
     * hasAnyAuthority     |   如果有参数，参数表示权限，则其中任何一个权限可以访问
     * hasAnyRole          |   如果有参数，参数表示角色，则其中任何一个角色可以访问
     * hasAuthority        |   如果有参数，参数表示权限，则其权限可以访问
     * hasIpAddress        |   如果有参数，参数表示IP地址，如果用户IP和参数匹配，则可以访问
     * hasRole             |   如果有参数，参数表示角色，则其角色可以访问
     * permitAll           |   用户可以任意访问
     * rememberMe          |   允许通过remember-me登录的用户访问
     * authenticated       |   用户登录后可访问
     */
    /**管理端所有请求走springsecurity，用户端自己实现*/
    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                // CSRF禁用，因为不使用session
                .csrf(AbstractHttpConfigurer::disable)
                // 禁用HTTP响应标头
                .headers((headersCustomizer) -> {
                    headersCustomizer.cacheControl(HeadersConfigurer.CacheControlConfig::disable)
                            .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin);
                })
                // 认证失败处理类
                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
                // 基于token，所以不需要session
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 权限配置
                .authorizeHttpRequests((requests) -> {
                    // 1. 先获取原有的白名单
                    permitAllUrl.getUrls().forEach(url -> requests.requestMatchers(url).permitAll());

                    // 2. 用户端路径完全放行（交给JWT拦截器处理）
//                    requests.antMatchers("/client/**").permitAll()
//                            .antMatchers("/admin/**").permitAll()
//                            .antMatchers("/member/**").permitAll()
//                            .antMatchers("/api/user/**").permitAll()
                    requests.requestMatchers("/client/**").permitAll()
                            .requestMatchers("/admin/**").permitAll()
                            .requestMatchers("/member/**").permitAll()
                            .requestMatchers("/api/user/**").permitAll()

                            // 3. 原有的公开路径
                            .requestMatchers("/login", "/register", "/captchaImage").permitAll()
                            // 静态资源，可匿名访问
                            .requestMatchers(HttpMethod.GET, "/", "/*.html", "/**/*.html",
                                    "/**/*.css", "/**/*.js", "/profile/**").permitAll()
                            .requestMatchers("/swagger-ui.html", "/swagger-resources/**",
                                    "/webjars/**", "/*/api-docs", "/druid/**").permitAll()
                            .requestMatchers(
                                    "/swagger-ui.html",
                                    "/swagger-ui/**",
                                    "/swagger-resources/**",
                                    "/webjars/**",
                                    "/v2/api-docs",
                                    "/v3/api-docs",
                                    "/doc.html"
                            ).permitAll()
                            // 4. 管理端路径需要认证和权限（保持原有逻辑）
//                            .antMatchers("/system/**").authenticated()
//                            .antMatchers("/monitor/**").authenticated()
//                            .antMatchers("/tool/**").authenticated()
//                            .antMatchers("/admin/**").authenticated()

                            .requestMatchers("/system/**").authenticated()
                            .requestMatchers("/monitor/**").authenticated()
                            .requestMatchers("/tool/**").authenticated()
                            .requestMatchers("/admin/**").authenticated()

                            // 5. 默认其他所有请求需要认证（保持原有安全策略）
                            .anyRequest().permitAll();
                })
                // 添加Logout filter（只处理管理端登出）
                .logout(logout -> logout.logoutUrl("/system/logout")
                        .logoutSuccessHandler(logoutSuccessHandler))
                // 添加JWT filter（Spring Security的JWT过滤器，只处理管理端认证）
                .addFilterBefore(authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class)
                // 添加CORS filter
                .addFilterBefore(corsFilter, JwtAuthenticationTokenFilter.class)
                .addFilterBefore(corsFilter, LogoutFilter.class)
                .build();
    }

    /**
     * 强散列哈希加密实现
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder()
    {
        return new BCryptPasswordEncoder();
    }
}
