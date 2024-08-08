package com.example.hoidanit.configuration;

import com.example.hoidanit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class PermissionInterceptorConfiguration implements WebMvcConfigurer {

    private final UserService userService;

    @Bean
    PermissionInterceptor getPermissionInterceptor() {
        return new PermissionInterceptor(userService);
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        String[] whiteList = {
                "/", "/api/v1/auth/**", "/storage/**",
                "/api/v1/companies", "/api/v1/jobs", "/api/v1/skills", "/api/v1/files"
        };
        registry.addInterceptor(getPermissionInterceptor())
                .excludePathPatterns(whiteList);
    }
}
