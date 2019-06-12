package net.brandontsang.tetroid.engine;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Util {
    static byte[] readFile(String path) throws IOException {
        return Files.readAllBytes(Paths.get(path));
    }
    
    static String readFileAsString(String path) throws IOException {
        return new String(readFile(path));
    }
}
