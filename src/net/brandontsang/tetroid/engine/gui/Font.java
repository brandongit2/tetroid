package net.brandontsang.tetroid.engine.gui;

import net.brandontsang.tetroid.engine.Scene;
import net.brandontsang.tetroid.engine.Util;
import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBTTAlignedQuad;
import org.lwjgl.stb.STBTTBakedChar;
import org.lwjgl.stb.STBTTFontinfo;
import org.lwjgl.system.MemoryStack;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;
import static org.lwjgl.stb.STBTruetype.*;
import static org.lwjgl.system.MemoryStack.stackPush;

public class Font {
    private STBTTFontinfo         fontInfo;
    private ByteBuffer            fontBuffer;
    private ByteBuffer            bitmap;
    private STBTTBakedChar.Buffer charData;
    
    private int ascent;
    private int descent;
    private int lineGap;
    
    private Scene scene;
    
    private final int bitmapWidth  = 512;
    private final int bitmapHeight = 512;
    private final int fontHeight;
    
    public int textureLoc;
    
    public Font(String fileLocation, int fontHeight, Scene scene) {
        this.scene = scene;
        this.fontHeight = fontHeight;
        
        try {
            byte[] data = Util.readFile(fileLocation);
            this.fontBuffer = BufferUtils.createByteBuffer(data.length);
            for (byte b : data) {
                this.fontBuffer.put(b);
            }
            this.fontBuffer.flip();
    
            this.fontInfo = STBTTFontinfo.create();
            if (!stbtt_InitFont(this.fontInfo, this.fontBuffer)) {
                System.err.println("Error loading font.");
            }
            
            // Get font metrics.
            try (MemoryStack stack = stackPush()) {
                IntBuffer pAscent  = stack.mallocInt(1);
                IntBuffer pDescent = stack.mallocInt(1);
                IntBuffer pLineGap = stack.mallocInt(1);
        
                stbtt_GetFontVMetrics(this.fontInfo, pAscent, pDescent, pLineGap);
        
                this.ascent = pAscent.get(0);
                this.descent = pDescent.get(0);
                this.lineGap = pLineGap.get(0);
            }
            
            // Generate a bitmap for the font from Unicode code points 0x20 to 0x80.
            int texID     = glGenTextures();
            this.charData = STBTTBakedChar.malloc(96);
            
            this.bitmap = BufferUtils.createByteBuffer(bitmapWidth * bitmapHeight);
            stbtt_BakeFontBitmap(
                fontBuffer,
                this.fontHeight * scene.window().dpiScale()[1],
                this.bitmap,
                this.bitmapWidth,
                this.bitmapHeight,
                32,
                this.charData
            );
    
            this.textureLoc = glGenTextures();
            glBindTexture(GL_TEXTURE_2D, this.textureLoc);
            glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RED, this.bitmapWidth, this.bitmapHeight, 0, GL_RED, GL_UNSIGNED_BYTE, this.bitmap);
            glGenerateMipmap(GL_TEXTURE_2D);
        } catch (IOException err) {
            err.printStackTrace();
            System.exit(1);
        }
    }
    
    private static float scale(float center, float offset, float factor) {
        return (offset - center) * factor + center;
    }
    
    public Text renderText(String text) {
        Text textObj = new Text(this);
        renderText(text, textObj);
        return textObj;
    }
    
    public void renderText(String text, Text instance) {
        float scale = stbtt_ScaleForPixelHeight(this.fontInfo, this.fontHeight);
        
        try (MemoryStack stack = stackPush()) {
            IntBuffer pCodePoint = stack.mallocInt(1);
    
            FloatBuffer x = stack.floats(0.0f);
            FloatBuffer y = stack.floats(0.0f);
            
            STBTTAlignedQuad q = STBTTAlignedQuad.mallocStack(stack);
            
            float factorX = 1.0f / this.scene.window().dpiScale()[0];
            float factorY = 1.0f / this.scene.window().dpiScale()[1];
            
            float lineY = 0.0f;
            
            for (int i = 0, to = text.length(); i < to;) {
                i += getCodePoint(text, to, i, pCodePoint);
                
                int codePoint = pCodePoint.get(0);
                if (codePoint == '\n') {
                    lineY = (ascent - descent + lineGap) * scale;
                    y.put(0, lineY = y.get(0) + (ascent - descent + lineGap) * scale);
                    x.put(0, 0.0f);
                    continue;
                } else if (codePoint < 32 || 128 <= codePoint) {
                    continue;
                }
            
                float cpX = x.get(0);
                stbtt_GetBakedQuad(this.charData, this.bitmapWidth, this.bitmapHeight, codePoint - 32, x, y, q, true);
                x.put(0, scale(cpX, x.get(0), factorX));
                if (i < to) {
                    getCodePoint(text, to, i, pCodePoint);
                    x.put(0, x.get(0) + stbtt_GetCodepointKernAdvance(this.fontInfo, codePoint, pCodePoint.get(0)) * scale);
                }
                
                float x0 = scale(cpX, q.x0(), factorX);
                float x1 = scale(cpX, q.x1(), factorX);
                float y0 = scale(lineY, q.y0(), factorY);
                float y1 = scale(lineY, q.y1(), factorY);
                
                instance.addCharacter(new Char(this.bitmap, x0, y0, x1, y1, q.s0(), q.t0(), q.s1(), q.t1(), this.fontHeight));
            }
        }
    }
    
    private static int getCodePoint(String text, int to, int i, IntBuffer cpOut) {
        char c1 = text.charAt(i);
        if (Character.isHighSurrogate(c1) && i + 1 < to) {
            char c2 = text.charAt(i + 1);
            if (Character.isLowSurrogate(c2)) {
                cpOut.put(0, Character.toCodePoint(c1, c2));
                return 2;
            }
        }
        cpOut.put(0, c1);
        return 1;
    }
}
