package com.ocr.p3back.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  /**
   * Configures the resource handlers for serving static resources.
   *
   * @param registry The ResourceHandlerRegistry to be configured.
   */
  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("/pictures/**")
        .addResourceLocations("file:./pictures/");
  }
}