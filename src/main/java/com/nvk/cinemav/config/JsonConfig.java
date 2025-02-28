package com.nvk.cinemav.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JsonConfig {
  @Bean
  public ObjectMapper objectMapper() {
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());  // Hỗ trợ LocalDateTime
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // Format ISO 8601
    return mapper;
  }
}
