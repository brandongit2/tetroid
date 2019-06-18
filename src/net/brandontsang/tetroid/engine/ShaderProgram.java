package net.brandontsang.tetroid.engine;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.HashMap;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class ShaderProgram {
    private int                      program;
    private HashMap<String, Integer> uniforms;
    
    public ShaderProgram(String vertShaderLoc, String fragShaderLoc) {
        uniforms = new HashMap<>();
        
        int vertShader = glCreateShader(GL_VERTEX_SHADER);
        int fragShader = glCreateShader(GL_FRAGMENT_SHADER);
        
        String vertShaderSource, fragShaderSource;
        try {
            vertShaderSource = Util.readFileAsString(vertShaderLoc);
            fragShaderSource = Util.readFileAsString(fragShaderLoc);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        
        glShaderSource(vertShader, vertShaderSource);
        glShaderSource(fragShader, fragShaderSource);
        
        glCompileShader(vertShader);
        glCompileShader(fragShader);
        
        this.program = glCreateProgram();
        glAttachShader(this.program, vertShader);
        glAttachShader(this.program, fragShader);
        
        glLinkProgram(this.program);
        if (glGetProgrami(this.program, GL_LINK_STATUS) == NULL) {
            throw new RuntimeException("Error linking shader program.\nProgram info: " + glGetProgramInfoLog(this.program, 2048) + "\nVertex shader info: " + glGetShaderInfoLog(vertShader) + "\nFragment shader info: " + glGetShaderInfoLog(fragShader));
        }
    
        glValidateProgram(this.program);
        if (glGetProgrami(this.program, GL_VALIDATE_STATUS) == NULL) {
            System.err.println("Warning validating shader.\nProgram info: " + glGetProgramInfoLog(this.program, 2048) + "\nVertex shader info: " + glGetShaderInfoLog(vertShader) + "\nFragment shader info: " + glGetShaderInfoLog(fragShader));
        }
        
        glDeleteShader(vertShader);
        glDeleteShader(fragShader);
    }
    
    public int pointer() {
        return this.program;
    }
    
    public void createUniform(String name) throws RuntimeException {
        glUseProgram(this.program);
        int uniformLocation = glGetUniformLocation(this.program, name);
        if (uniformLocation < 0) {
            throw new RuntimeException("Could not find uniform " + name);
        }
        uniforms.put(name, uniformLocation);
    }
    
    public void setUniform(String name, Matrix4f value) {
        FloatBuffer uniformBuffer = BufferUtils.createFloatBuffer(16);
        value.get(uniformBuffer);
        glUseProgram(this.program);
        glUniformMatrix4fv(uniforms.get(name), false, uniformBuffer);
    }
    
    public void setUniform(String name, Vector3f value) {
        FloatBuffer uniformBuffer = BufferUtils.createFloatBuffer(3);
        value.get(uniformBuffer);
        glUseProgram(this.program);
        glUniform3fv(uniforms.get(name), uniformBuffer);
    }
    
    public void setUniform(String name, Vector4f value) {
        FloatBuffer uniformBuffer = BufferUtils.createFloatBuffer(4);
        value.get(uniformBuffer);
        glUseProgram(this.program);
        glUniform4fv(uniforms.get(name), uniformBuffer);
    }
    
    public void setUniform(String name, float value) {
        glUseProgram(this.program);
        glUniform1f(uniforms.get(name), value);
    }
    
    public void setUniform(String name, int value) {
        glUseProgram(this.program);
        glUniform1i(uniforms.get(name), value);
    }
}
