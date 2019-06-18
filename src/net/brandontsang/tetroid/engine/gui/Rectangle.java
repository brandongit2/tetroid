package net.brandontsang.tetroid.engine.gui;

import net.brandontsang.tetroid.engine.Texture;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class Rectangle {
    private int vao;
    private Matrix4f modelMatrix = new Matrix4f();
    
    public  boolean  isTextured = false; // If false, this rectangle only has a solid color.
    private Vector3f color;
    private Texture  texture;
    private float    opacity = 1.0f;
    
    private Rectangle(float x, float y, float width, float height) {
        this.vao = glGenVertexArrays();
        glBindVertexArray(this.vao);
        
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(12);
        vertexBuffer.put(new float[] {
            x, y, 1.0f,
            width, y, 1.0f,
            x, height, 1.0f,
            width, height, 1.0f
        }).flip();
        int vertexVboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vertexVboId);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(0);
    
        FloatBuffer texCoordBuffer = BufferUtils.createFloatBuffer(8);
        texCoordBuffer.put(new float[] {
            0.0f, 1.0f,
            1.0f, 1.0f,
            0.0f, 0.0f,
            1.0f, 0.0f
        }).flip();
        int texCoordVboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, texCoordVboId);
        glBufferData(GL_ARRAY_BUFFER, texCoordBuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(1);
    
        glBindVertexArray(0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }
    
    public Rectangle(float x, float y, float width, float height, Texture texture) {
        this(x, y, width, height);
        this.isTextured = true;
        this.texture = texture;
    }
    
    public Rectangle(float x, float y, float width, float height, Vector3f color) {
        this(x, y, width, height);
        this.color = color;
        
        glBindVertexArray(this.vao);
        
        FloatBuffer colorBuffer = BufferUtils.createFloatBuffer(12);
        for (int i = 0; i < 12; i += 3) {
            color.get(i, colorBuffer);
        }
        int colorVboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, colorVboId);
        glBufferData(GL_ARRAY_BUFFER, colorBuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(3, 3, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(3);
    
        glBindVertexArray(0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }
    
    public int getVao() {
        return this.vao;
    }
    
    public Matrix4f getModelMatrix() {
        return this.modelMatrix;
    }
    
    public boolean isTextured() {
        return this.isTextured;
    }
    
    public Vector3f getColor() {
        return this.color;
    }
    
    public int getTextureLoc() {
        return this.texture.getLoc();
    }
    
    public Rectangle setOpacity(float opacity) {
        this.opacity = opacity;
        return this;
    }
    
    public float getOpacity() {
        return this.opacity;
    }
}
