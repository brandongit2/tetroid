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
    
    private float opacity;
    
    public Line(Vector3f pos1, Vector3f pos2, Vector3f color, float opacity) {
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
    
        FloatBuffer colorBuffer = BufferUtils.createFloatBuffer(6);
        // Fill `colorBuffer` with the same color.
        for (int i = 0; i < 6; i += 3) {
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
    
    public float getOpacity() {
        return this.opacity;
    }
    
    public void setOpacity(float opacity) {
        this.opacity = opacity;
    }
}
