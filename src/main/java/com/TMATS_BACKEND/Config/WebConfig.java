package com.TMATS_BACKEND.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.concurrent.TimeUnit;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Register resource handlers for CSS and JS
        registry.addResourceHandler("/css/**")
                .addResourceLocations("classpath:/static/css/")
                .setCacheControl(CacheControl.maxAge(365, TimeUnit.DAYS));

        registry.addResourceHandler("/js/**")
                .addResourceLocations("classpath:/static/js/")
                .setCacheControl(CacheControl.maxAge(365, TimeUnit.DAYS));

        registry.addResourceHandler("/images/**")
                .addResourceLocations("classpath:/static/images/")
                .setCacheControl(CacheControl.maxAge(365, TimeUnit.DAYS));
        
        // Add a handler for all static resources
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/")
                .setCacheControl(CacheControl.maxAge(365, TimeUnit.DAYS));
                
        // Add a handler for the favicon
        registry.addResourceHandler("/favicon.png")
                .addResourceLocations("classpath:/static/images/favicon.png")
                .setCacheControl(CacheControl.maxAge(365, TimeUnit.DAYS));
    }
    
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // Add direct view controller mappings if needed
        // This can be useful for simple views that don't require controller logic
        // Example: registry.addViewController("/login").setViewName("login");
    }
} 