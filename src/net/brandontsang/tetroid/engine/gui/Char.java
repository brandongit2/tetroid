package net.brandontsang.tetroid.engine.gui;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class Char {
    private int vao;
    private Matrix4f modelMatrix = new Matrix4f();
    
    private float x;
    private float y;
    private float width;
    private float height;
    
    private ByteBuffer bitmap;
    
    public Char(ByteBuffer bitmap, float x0, float y0, float x1, float y1, float u0, float v0, float u1, float v1, int fontHeight) {
        this.x      = x0;
        this.y      = y0;
        this.width  = x1 - x0;
        this.height = y1 - y0;
        this.bitmap = bitmap;
        
        this.vao = glGenVertexArrays();
        glBindVertexArray(this.vao);
        
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(12);
        vertexBuffer.put(new float[] {
            x0, y0 + fontHeight, 1.0f,
            x1, y0 + fontHeight, 1.0f,
            x0, y1 + fontHeight, 1.0f,
            x1, y1 + fontHeight, 1.0f
        }).flip();
        int vertexVboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vertexVboId);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(0);
        
        FloatBuffer texCoordBuffer = BufferUtils.createFloatBuffer(8);
        texCoordBuffer.put(new float[] {
            u0, v0,
            u1, v0,
            u0, v1,
            u1, v1
        }).flip();
        int texCoordVboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, texCoordVboId);
        glBufferData(GL_ARRAY_BUFFER, texCoordBuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(1);
        
        glBindVertexArray(0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }
    
    public Char translate(float dx, float dy) {
        this.modelMatrix.translate(dx, dy, 0.0f);
        return this;
    }
    
    public Char setWidth(float w) {
        this.modelMatrix.scale(w / this.width);
        return this;
    }
    
    public Char setHeight(float h) {
        this.modelMatrix.scale(h / this.height);
        return this;
    }
    
    public Char scale(float factor) {
        this.modelMatrix.scale(factor);
        return this;
    }
    
    public ByteBuffer getBitmap() {
        return this.bitmap;
    }
    
    public int getVao() {
        return this.vao;
    }
    
    public Matrix4f getModelMatrix() {
        return this.modelMatrix;
    }
    
    public float x() {
        return this.x;
    }
    
    public float y() {
        return this.y;
    }
    
    public float width() {
        return this.width;
    }
    
    public float height() {
        return this.height;
    }
}
