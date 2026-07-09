package com.example.pai_demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * @author GilbertYoung
 * @date 2026/07/09 15:20
 */
@Configuration
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        // BCrypt自动随机加盐，相同明文每次加密结果不同
        return new BCryptPasswordEncoder();
    }

    // 放行全部接口，关闭一切鉴权、csrf、登录弹窗
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 关闭csrf防护（无登录表单不需要）
                .csrf().disable()
                // 所有请求全部放行，不校验登录、不拦截
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
        return http.build();
    }
}
