package net.brandontsang.tetroid.engine;

import com.google.common.io.ByteStreams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.*;

public class Util {
    public static byte[] readFile(String path) throws IOException {
        InputStream in = Util.class.getResourceAsStream(path);
        return ByteStreams.toByteArray(in);
    }
    
    public static String readFileAsString(String path) throws IOException {
        return new String(readFile(path));
    }
}
