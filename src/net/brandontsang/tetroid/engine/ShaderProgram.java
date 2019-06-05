package net.brandontsang.tetroid;

import java.io.IOException;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glDeleteShader;
import static org.lwjgl.system.MemoryUtil.NULL;

public class ShaderProgram {
    private int program;
    
    public ShaderProgram() {
        int vertShader = glCreateShader(GL_VERTEX_SHADER);
        int fragShader = glCreateShader(GL_FRAGMENT_SHADER);
        
        String vertShaderSource, fragShaderSource;
        try {
            vertShaderSource = Util.readFile("./src/net/brandontsang/tetroid/shaders/vertex.glsl");
            fragShaderSource = Util.readFile("./src/net/brandontsang/tetroid/shaders/fragment.glsl");
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
    
    public int getProgram() {
        return program;
    }
}
