package net.brandontsang.tetroid.tetroid;

import net.brandontsang.tetroid.engine.*;
import org.lwjgl.glfw.GLFWErrorCallback;

import java.io.IOException;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;

public class Main {
    private Window window;
    
    private ShaderProgram program;
    private int           vao;
            Scene         scene;
    
    private double prevMouseX = 0;
    private double prevMouseY = 0;
    // Since mouse input checks movement deltas, the first check will be erroneous. Don't check
    // the first tick.
    private boolean firstCall = true;
    
    private void run() {
        init();
        
        scene = new Scene(window);
        
        program = new ShaderProgram();
        program.createUniform("projectionMatrix");
        program.createUniform("viewMatrix");
        program.createUniform("modelMatrix");
        scene.setShaderProgram(program);
        
        Mesh mesh;
        try {
            mesh = Mesh.fromFile("./res/models/monkey.obj");
            scene.add(mesh);
        } catch (IOException err) {
            err.printStackTrace();
            System.exit(1);
        }
        
        OrbitCamera camera = new OrbitCamera(0.0f, 0.0f, 0.0f, 5.0f, 70.0f, 0.1f, 100f, window);
        
        int camId = scene.add(camera);
        scene.setCurrentCamera(camId);
    
        glfwSetInputMode(window.pointer(), GLFW_CURSOR, GLFW_CURSOR_DISABLED);
        glfwSetCursorPosCallback(window.pointer(), (long evWindow, double mouseX, double mouseY) -> {
            if (firstCall) {
                firstCall = false;
            } else {
                camera.rotate((float) (mouseY - prevMouseY) / 10, (float) (mouseX - prevMouseX) / 10);
            }
            prevMouseX = mouseX;
            prevMouseY = mouseY;
        });
        
        GameLoop.start(scene, 10, 10, 15);
        RenderLoop.start(scene);
        
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
    }
    
    private void terminate() {
        GameLoop.stop();
        RenderLoop.stop();
        
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
