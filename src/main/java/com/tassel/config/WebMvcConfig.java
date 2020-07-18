package com.tassel.config;

import com.tassel.interceptor.LoginInterceptor;
import com.tassel.interceptor.LoginRequiredInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Description
 * @Author Zhang Shuaiyin
 * @Date 2020/07/05
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    LoginInterceptor loginInterceptor;

    @Autowired
    LoginRequiredInterceptor loginRequiredInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .excludePathPatterns("/css/**", "/js/**", "/img/**");

        registry.addInterceptor(loginRequiredInterceptor)
                .excludePathPatterns("/css/**", "/js/**", "/img/**");
    }
}
