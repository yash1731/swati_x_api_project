package com.aixtrade.utilities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

@Configuration
@Data
@Slf4j
@AllArgsConstructor
//@NoArgsConstructor
public class EnvConfig {

//    @Value("${marketDataService.baseUrl}")
//    private String marketDataServiceBaseUrl;
//
//    @Value("${orderService.baseUrl}")
//    private String orderBookServiceBaseUrl;
//
//    @Value("${quoteService.baseUrl}")
//    private String quoteServiceBaseUrl;

    private static final Properties properties = new Properties();

    // todo: replace with reading values from spring config
    static {
        String env = System.getProperty("env");
        if (StringUtils.isEmpty(env)) {
            env = "qa1";
        }
//        log.debug("Environment is "+env);

        String configPath = "/src/main/resources/config/application-" + env + ".properties";
        String sysPath = System.getProperty("user.dir");
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(sysPath + configPath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            properties.load(fileInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    private EnvConfig() {}

    public static String getValue(String keyName) {

        if (keyName.isEmpty() || keyName.contains(" ")) {
            keyName = keyName.replaceAll("\\s", ".");
        } else {
            throw new IllegalArgumentException("Not formatted properly");
        }

        return properties.getProperty(keyName);
    }
}
