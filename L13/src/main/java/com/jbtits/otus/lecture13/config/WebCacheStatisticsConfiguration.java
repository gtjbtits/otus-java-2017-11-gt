package com.jbtits.otus.lecture13.config;

import com.jbtits.otus.lecture13.cache.CacheService;
import com.jbtits.otus.lecture13.cache.CacheServiceImpl;
import com.jbtits.otus.lecture13.dbService.DBService;
import com.jbtits.otus.lecture13.dbService.DBServiceHibernateImpl;
import com.jbtits.otus.lecture13.dbService.dataSets.DataSet;
import com.jbtits.otus.lecture13.auth.AuthService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

@Configuration
@EnableWebMvc
@ComponentScan({"com.jbtits.otus.lecture13"})
public class WebCacheStatisticsConfiguration implements WebMvcConfigurer {
    private ApplicationContext applicationContext;

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/content/**")
                .addResourceLocations("/content/");
    }

    @Bean
    public FreeMarkerViewResolver freemarkerViewResolver() {
        FreeMarkerViewResolver resolver = new FreeMarkerViewResolver();
        resolver.setCache(true);
        resolver.setPrefix("");
        resolver.setSuffix(".html");
        return resolver;
    }

    @Bean
    public FreeMarkerConfigurer freemarkerConfig() {
        FreeMarkerConfigurer freeMarkerConfigurer = new FreeMarkerConfigurer();
        freeMarkerConfigurer.setTemplateLoaderPath("/WEB-INF/tml/");
        return freeMarkerConfigurer;
    }
}
