package com.project.e_commerce.configurations;

import com.fasterxml.jackson.databind.MapperFeature;
import org.modelmapper.ModelMapper;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer customizeJackson() {
        return builder -> builder.featuresToEnable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS);
    }
}
