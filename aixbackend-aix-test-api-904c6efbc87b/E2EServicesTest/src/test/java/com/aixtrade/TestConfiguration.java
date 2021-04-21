package com.aixtrade;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({TestApplication.class})
public class TestConfiguration {
}
