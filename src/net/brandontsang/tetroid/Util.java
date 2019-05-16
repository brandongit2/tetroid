package net.brandontsang.tetroid;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Util {
    // Only reads UTF-8.
    static String readFile(String path) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded);
    }
}
