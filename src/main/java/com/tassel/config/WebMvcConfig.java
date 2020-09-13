package com.tassel.config;

import com.tassel.interceptor.LoginInterceptor;
import com.tassel.interceptor.LoginRequiredInterceptor;
import com.tassel.interceptor.MessageInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * @Description
 * @Author Zhang Shuaiyin
 * @Date 2020/07/05
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Resource
    LoginInterceptor loginInterceptor;

    @Resource
    LoginRequiredInterceptor loginRequiredInterceptor;

    @Resource
    MessageInterceptor messageInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .excludePathPatterns("/css/**", "/js/**", "/img/**");

        registry.addInterceptor(loginRequiredInterceptor)
                .excludePathPatterns("/css/**", "/js/**", "/img/**");

        registry.addInterceptor(messageInterceptor)
                .excludePathPatterns("/css/**", "/js/**", "/img/**");
    }
}
