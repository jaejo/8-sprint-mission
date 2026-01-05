package com.sprint.mission.discodeit;

import java.nio.file.Paths;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Value("${file.dir}")
  private String fileDir;

  @Override
  public void configurePathMatch(PathMatchConfigurer configurer) {
    configurer.addPathPrefix("/api",
        c -> c.isAnnotationPresent(RestController.class));
  }

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    String resourceLocation = Paths.get(fileDir).toUri().toString();

    registry.addResourceHandler("/upload/**")
        .addResourceLocations(resourceLocation);
  }
}
