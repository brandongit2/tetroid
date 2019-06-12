package net.brandontsang.tetroid.engine;

import org.joml.Matrix4f;
import org.lwjgl.system.MemoryStack;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.HashMap;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class ShaderProgram {
    private int                      program;
    private HashMap<String, Integer> uniforms;
    
    public ShaderProgram() {
        uniforms = new HashMap<>();
        
        int vertShader = glCreateShader(GL_VERTEX_SHADER);
        int fragShader = glCreateShader(GL_FRAGMENT_SHADER);
        
        String vertShaderSource, fragShaderSource;
        try {
            vertShaderSource = Util.readFileAsString("./src/net/brandontsang/tetroid/tetroid/shaders/vertex.glsl");
            fragShaderSource = Util.readFileAsString("./src/net/brandontsang/tetroid/tetroid/shaders/fragment.glsl");
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        
        glShaderSource(vertShader, vertShaderSource);
        glShaderSource(fragShader, fragShaderSource);
        
        glCompileShader(vertShader);
        glCompileShader(fragShader);
        
        program = glCreateProgram();
        glAttachShader(program, vertShader);
        glAttachShader(program, fragShader);
        
        glLinkProgram(program);
        if (glGetProgrami(program, GL_LINK_STATUS) == NULL) {
            throw new RuntimeException("Error linking shader program. Code " + glGetProgramInfoLog(program, 2048));
        }
    
        glValidateProgram(program);
        if (glGetProgrami(program, GL_VALIDATE_STATUS) == NULL) {
            System.err.println("Warning validating shader. Code " + glGetProgramInfoLog(program, 2048));
        }
        
        glDeleteShader(vertShader);
        glDeleteShader(fragShader);
    }
    
    public int pointer() {
        return program;
    }
    
    public void createUniform(String name) throws RuntimeException {
        int uniformLocation = glGetUniformLocation(this.program, name);
        if (uniformLocation < 0) {
            throw new RuntimeException("Could not find uniform " + name);
        }
        uniforms.put(name, uniformLocation);
    }
    
    public void setUniform(String name, Matrix4f value) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer uniformBuffer = stack.mallocFloat(16);
            value.get(uniformBuffer);
            glUniformMatrix4fv(uniforms.get(name), false, uniformBuffer);
        }
    }
}
