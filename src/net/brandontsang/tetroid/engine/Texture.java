package net.brandontsang.tetroid.engine;

import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;
import static org.lwjgl.stb.STBImage.*;

public class Texture {
    private int loc;
    
    public Texture(String fileLocation) {
        try {
            byte[] bytes = Util.readFile(fileLocation);
            ByteBuffer buf = BufferUtils.createByteBuffer(bytes.length);
            for (byte b : bytes) {
                buf.put(b);
            }
            buf.flip();
    
            IntBuffer _w = BufferUtils.createIntBuffer(1);
            IntBuffer _h = BufferUtils.createIntBuffer(1);
            IntBuffer numChannels = BufferUtils.createIntBuffer(1);
            ByteBuffer img = stbi_load_from_memory(buf, _w, _h, numChannels, 0);
            if (img == null) {
                throw new RuntimeException("Failed to load image: " + stbi_failure_reason());
            }
            
            int w = _w.get(0);
            int h = _h.get(0);
            
            loc = glGenTextures();
            glBindTexture(GL_TEXTURE_2D, loc);
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, w, h, 0, GL_RGB, GL_UNSIGNED_BYTE, img);
            glGenerateMipmap(GL_TEXTURE_2D);
            
            stbi_image_free(img);
        } catch (IOException err) {
            err.printStackTrace();
            System.exit(1);
        }
    }
    
    public int getLoc() {
        return this.loc;
    }
}
