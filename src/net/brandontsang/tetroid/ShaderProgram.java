package net.brandontsang.tetroid;

import java.io.IOException;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glDeleteShader;

public class ShaderProgram {
    private int program;
    
    public ShaderProgram() {
        int vertShader = glCreateShader(GL_VERTEX_SHADER);
        int fragShader = glCreateShader(GL_FRAGMENT_SHADER);
    
        String vertShaderSource, fragShaderSource;
        try {
            vertShaderSource = Util.readFile("./src/net/brandontsang/tetroid/shaders/shader.vert");
            fragShaderSource = Util.readFile("./src/net/brandontsang/tetroid/shaders/shader.frag");
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
    
        glDeleteShader(vertShader);
        glDeleteShader(fragShader);
    }
    
    public int getProgram() {
        return program;
    }
}
