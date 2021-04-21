package com.aixtrade.utilities;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.fasterxml.jackson.databind.DeserializationFeature.READ_ENUMS_USING_TO_STRING;
import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_ENUMS_USING_TO_STRING;

@Configuration
public class CoreConfig {

    @Bean
    public ObjectMapper objectMapper() {
        final ObjectMapper objectMapper = new ObjectMapper();

        objectMapper.disable(MapperFeature.DEFAULT_VIEW_INCLUSION);

        // Uses Enum.toString() for serialization of an Enum
        objectMapper.enable(WRITE_ENUMS_USING_TO_STRING);

        // Uses Enum.toString() for deserialization of an Enum
        objectMapper.enable(READ_ENUMS_USING_TO_STRING);

        objectMapper.enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);

        objectMapper.disable(DeserializationFeature.ACCEPT_FLOAT_AS_INT);

        objectMapper.registerModule(new JavaTimeModule());

        return objectMapper;
    }

}
