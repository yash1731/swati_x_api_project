package com.aixtrade.utilities;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class JsonReader {

    public static String getData(String fileName) throws IOException {
        String path = System.getProperty("user.dir") + "/src/main/resources/testData/collections/" + fileName;
        return new String(Files.readAllBytes(Paths.get(path)));
    }
}
