package com.system.management.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

  public ModelMapper modelMapper() {
    return new ModelMapper();
  }
}
