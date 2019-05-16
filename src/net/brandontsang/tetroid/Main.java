package net.brandontsang.tetroid;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import java.nio.DoubleBuffer;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Main {
    private long window;
    private long monitor;
    
    private static int WINDOW_WIDTH = 1280;
    private static int WINDOW_HEIGHT = 720;
    
    private static double[] vertices = new double[] {
      -1.0, -1.0,
       0.0,  1.0,
       1.0, -1.0
    };
    
    private ShaderProgram program;
    
    public void run() {
        init();
        loop();
        terminate();
    }
    
    private void init() {
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();
        
        // Initialize GLFW.
        if (!glfwInit()) throw new IllegalStateException("Unable to initialize GLFW");
        
        // Set window context hints.
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 0);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_SCALE_TO_MONITOR, GLFW_TRUE);
        glfwWindowHint(GLFW_TRANSPARENT_FRAMEBUFFER, GLFW_TRUE);
        
        // Create the window.
        window = glfwCreateWindow(WINDOW_WIDTH, WINDOW_HEIGHT, "Hello", NULL, NULL);
        if (window == NULL) throw new RuntimeException("Failed to create the GLFW window");
        
        // Create the OpenGL context.
        glfwMakeContextCurrent(window);
        GL.createCapabilities();
        
        glViewport(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
        
        // Get screen dimensions and display scaling factor.
        monitor = glfwGetPrimaryMonitor();
        float[] xscale = new float[1];
        float[] yscale = new float[1];
        glfwGetMonitorContentScale(monitor, xscale, yscale);
        GLFWVidMode vidMode = glfwGetVideoMode(monitor);
        float screenWidth = vidMode.width();
        float screenHeight = vidMode.height();
        
        // Center screen.
        int xpos = (int) (screenWidth - WINDOW_WIDTH * xscale[0]) / 2;
        int ypos = (int) (screenHeight- WINDOW_HEIGHT * yscale[0]) / 2;
        glfwSetWindowPos(window, xpos, ypos);
        
        glfwSetKeyCallback(window, (long window, int key, int scancode, int action, int mods) -> {
            if (action == GLFW_RELEASE) {
                if (key == GLFW_KEY_ESCAPE) { // Close the window.
                    terminate();
                }
            }
        });
        
        program = new ShaderProgram();
    
        DoubleBuffer verts = BufferUtils.createDoubleBuffer(vertices.length);
        verts.put(vertices);
        verts.flip();
        
        int vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, verts, GL_STATIC_DRAW);
        
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 3, 0L);
        glEnableVertexAttribArray(0);
        
        int vao = glGenVertexArrays();
        glBindVertexArray(vao);
    }
    
    private void loop() {
        while (!glfwWindowShouldClose(window)) {
            glUseProgram(program.getProgram());
            glDrawArrays(GL_TRIANGLES, 0, 3);
            
            glfwSwapBuffers(window);
            glfwPollEvents();
        }
    }
    
    private void terminate() {
        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);
    
        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
        
        System.exit(0);
    }
    
    public static void main(String[] args) {
        new Main().run();
    }
}
