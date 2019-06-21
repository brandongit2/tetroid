package net.brandontsang.tetroid.engine;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class Line {
    private int      vao;
    private Matrix4f modelMatrix = new Matrix4f();
    
    private Vector3f color;
    private float    opacity;
    
    public Line(Vector3f pos1, Vector3f pos2, Vector3f color, float opacity) {
        this.color = color;
        this.opacity = opacity;
        
        this.vao = glGenVertexArrays();
        glBindVertexArray(vao);
    
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(6);
        vertexBuffer.put(new float[] {pos1.x, pos1.y, pos1.z, pos2.x, pos2.y, pos2.z}).flip();
        int vertexVboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vertexVboId);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(0);
    
        glBindVertexArray(0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }
    
    public int getVao() {
        return this.vao;
    }
    
    public Matrix4f getModelMatrix() {
        return this.modelMatrix;
    }
    
    public void setColor(Vector3f color) {
        this.color = color;
    }
    
    public Vector3f getColor() {
        return this.color;
    }
    
    public float getOpacity() {
        return this.opacity;
    }
    
    public void setOpacity(float opacity) {
        this.opacity = opacity;
    }
}
