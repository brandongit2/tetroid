package net.brandontsang.tetroid;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWErrorCallback;

import java.nio.FloatBuffer;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class Main {
    private Window window;
    public static volatile boolean gameIsRunning = true;
    
    private static float[] vertices = new float[] {
      -0.5f, -0.5f, 0.0f,
       0.5f, -0.5f, 0.0f,
       0.0f,  0.5f, 0.0f
    };
    
    private ShaderProgram program;
    private int vao;
    
    private void run() {
        init();
        
        Scene scene = new Scene();
        
        Cube cube = new Cube(1);
        scene.add(cube);
        
        Camera camera = new Camera();
        scene.add(camera);
        
        GameLoop.start();
        RenderLoop.start();
        
        terminate();
    }
    
    private void init() {
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();
        
        // Initialize GLFW.
        if (!glfwInit()) throw new IllegalStateException("Unable to initialize GLFW");
        
        window = new Window(1280, 720, "Hello");
        
        glfwSetKeyCallback(window.pointer(), (long window, int key, int scancode, int action, int mods) -> {
            if (action == GLFW_RELEASE) {
                if (key == GLFW_KEY_ESCAPE) { // Close the window.
                    terminate();
                }
            }
        });
        
        program = new ShaderProgram();
        
        (new Thread(new GameLoop())).start();
    
        FloatBuffer verts = BufferUtils.createFloatBuffer(vertices.length);
        verts.put(vertices);
        verts.flip();
    
        vao = glGenVertexArrays();
        glBindVertexArray(vao);
        
        int vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, verts, GL_STATIC_DRAW);
        
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0L);
        glEnableVertexAttribArray(0);
    }
    
    private void render() {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glClear(GL_COLOR_BUFFER_BIT);
        
        glUseProgram(program.getProgram());
        glBindVertexArray(vao);
        glDrawArrays(GL_TRIANGLES, 0, 3);
        
        glfwSwapBuffers(window.pointer());
        glfwPollEvents();
    }
    
    private void terminate() {
        gameIsRunning = false;
        
        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(window.pointer());
        glfwDestroyWindow(window.pointer());
    
        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
        
        System.exit(0);
    }
    
    public static void main(String[] args) {
        new Main().run();
    }
}
