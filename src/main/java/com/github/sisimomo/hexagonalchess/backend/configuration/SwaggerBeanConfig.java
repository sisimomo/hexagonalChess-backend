package com.github.sisimomo.hexagonalchess.backend.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.github.sisimomo.hexagonalchess.backend.commons.dto.converter.CustomModelConverter;
import com.github.sisimomo.hexagonalchess.backend.game.controller.converter.StringToDirection;

import io.swagger.v3.core.converter.ModelConverters;
import jakarta.annotation.PostConstruct;

@Configuration
public class SwaggerBeanConfig {

  @PostConstruct
  public void postSetup() {
    ModelConverters.getInstance().addConverter(new CustomModelConverter());
  }

  @Bean
  public WebMvcConfigurer corsConfigurer() {
    return new WebMvcConfigurer() {
      @Override
      public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToDirection());
      }

    };
  }

}
