package com.min.testapi;

import jakarta.servlet.Filter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;


@Configuration
public class WebConfig {

    @Bean
    public FilterRegistrationBean<Filter> gzipFilterRegistrationBean(final RequestMappingHandlerMapping requestMappingHandlerMapping) {
        final FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new GzipResponseCompressionFilter(requestMappingHandlerMapping));
        return registration;
    }

}
