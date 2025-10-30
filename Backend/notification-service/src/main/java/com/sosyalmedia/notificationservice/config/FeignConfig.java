package com.sosyalmedia.notificationservice.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import feign.codec.Decoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

@Configuration
public class FeignConfig {

    @Bean
    public Decoder feignDecoder() {
        ObjectMapper objectMapper = new ObjectMapper();

        //  Bilinmeyen field'ları yoksay
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // Null field'ları yoksay
        objectMapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);

        //  Java 8 Date/Time desteği
        objectMapper.registerModule(new JavaTimeModule());

        MappingJackson2HttpMessageConverter converter =
                new MappingJackson2HttpMessageConverter(objectMapper);

        ObjectFactory<HttpMessageConverters> objectFactory =
                () -> new HttpMessageConverters(converter);

        return new SpringDecoder(objectFactory);
    }
}