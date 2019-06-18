package net.brandontsang.tetroid.engine;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Util {
    public static byte[] readFile(String path) throws IOException {
        return Files.readAllBytes(Paths.get(path));
    }
    
    public static String readFileAsString(String path) throws IOException {
        return new String(readFile(path));
    }
}
