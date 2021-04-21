package com.aixtrade.utilities;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class TestData {

    private static final Properties properties = new Properties();

    static {

        String configPath = "/src/main/resources/testData.properties";
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

    private TestData() {}

    public static String getValue(String keyName) {
        if (keyName.isEmpty() || keyName.contains(" ")) {
            keyName = keyName.replaceAll("\\s", ".");
        } else {
            throw new IllegalArgumentException("Not formatted properly");
        }

        return properties.getProperty(keyName);
    }
}
