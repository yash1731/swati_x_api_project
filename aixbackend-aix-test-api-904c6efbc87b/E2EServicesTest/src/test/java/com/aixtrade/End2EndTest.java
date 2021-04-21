package com.aixtrade;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@CucumberContextConfiguration
@SpringBootTest(classes = {TestConfiguration.class})
public abstract class End2EndTest {
}
