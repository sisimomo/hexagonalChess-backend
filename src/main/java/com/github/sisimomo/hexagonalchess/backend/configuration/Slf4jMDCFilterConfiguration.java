package com.github.sisimomo.hexagonalchess.backend.configuration;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <a href=
 * "https://medium.com/@d.lopez.j/spring-boot-setting-a-unique-id-per-request-dd648efef2b">Source</a>
 */
@Configuration
public class Slf4jMDCFilterConfiguration {

  @Bean
  public FilterRegistrationBean<Slf4jMDCFilter> servletRegistrationBean() {
    final FilterRegistrationBean<Slf4jMDCFilter> registrationBean = new FilterRegistrationBean<>();
    registrationBean.setFilter(new Slf4jMDCFilter());
    registrationBean.setOrder(2);
    return registrationBean;
  }

}
