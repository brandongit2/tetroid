package net.brandontsang.tetroid.tetroid;

import net.brandontsang.tetroid.engine.*;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFWErrorCallback;

import java.io.IOException;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL20.glUseProgram;

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
        glUseProgram(program.pointer());
        program.createUniform("projectionMatrix");
        program.createUniform("viewMatrix");
        program.createUniform("modelMatrix");
        program.createUniform("matId");
        program.createUniform("ambient");
        program.createUniform("cameraPos");
        program.createUniform("reflectivity");
        program.createUniform("shininess");
        for (int i = 0; i < 10; i++) {
            program.createUniform("lightColor[" + i + "]");
            program.createUniform("lightPos[" + i + "]");
        }
        scene.setShaderProgram(program);
        
        scene.add(new Line(new Vector3f(-10.0f, 15.0f, -10.0f), new Vector3f(10.0f, 15.0f, 10.0f), new Vector4f(1.0f, 0.0f, 0.0f, 1.0f)));
        try {
            scene.add(new Mesh[] {
                Mesh.fromFile("./res/models/monkey.obj", new PhongMaterial(new Vector3f(1.0f, 1.0f, 1.0f), 0.8f, 50.0f)),
                Mesh.fromFile("./res/models/cube.obj", new PhongMaterial(new Vector3f(1.0f, 0.0f, 0.0f), 0.8f, 5.0f)).translate(5.0f, 0.0f, 0.0f),
                Mesh.fromFile("./res/models/cube.obj", new PhongMaterial(new Vector3f(0.0f, 1.0f, 0.0f), 0.8f, 5.0f)).translate(0.0f, 5.0f, 0.0f),
                Mesh.fromFile("./res/models/cube.obj", new PhongMaterial(new Vector3f(0.0f, 0.0f, 1.0f), 0.8f, 5.0f)).translate(0.0f, 0.0f, 5.0f),
                Mesh.fromFile("./res/models/cube.obj", new PhongMaterial(new Vector3f(1.0f, 1.0f, 0.0f), 0.8f, 5.0f)).translate(-5.0f, 0.0f, 0.0f),
                Mesh.fromFile("./res/models/cube.obj", new PhongMaterial(new Vector3f(0.0f, 1.0f, 1.0f), 0.8f, 5.0f)).translate(0.0f, -5.0f, 0.0f),
                Mesh.fromFile("./res/models/cube.obj", new PhongMaterial(new Vector3f(1.0f, 0.0f, 1.0f), 0.8f, 5.0f)).translate(0.0f, 0.0f, -5.0f),
                Mesh.fromFile("./res/models/plane.obj", new PhongMaterial(new Vector3f(1.0f, 1.0f, 1.0f), 1.0f, 100.0f)).translate(0.0f, -6.0f, 0.0f)
            });
        } catch (IOException err) {
            err.printStackTrace();
            System.exit(1);
        }
        
        scene.setAmbientLight(new Vector3f(0.1f, 0.1f, 0.1f));
        scene.add(new PointLight(new Vector3f(2.0f, 2.0f, 2.0f), new Vector3f(10.0f, 7.0f, 0.0f)));
        scene.add(new PointLight(new Vector3f(2.0f, 0.3f, 2.0f), new Vector3f(-10.0f, 7.0f, 5.0f)));
        
//        SunLight sunLight = new SunLight(-1.0f, 1.0f, -1.0f);
//        scene.add(sunLight);
        
        OrbitCamera camera = new OrbitCamera(0.0f, 0.0f, 0.0f, 5.0f, 70.0f, 0.1f, 1000f, window);
        int camId = scene.add(camera);
        scene.setCurrentCamera(camId);
        
        glfwSetCursorPosCallback(window.pointer(), (long evWindow, double mouseX, double mouseY) -> {
            if (firstCall) {
                firstCall = false;
            } else if (glfwGetMouseButton(window.pointer(), GLFW_MOUSE_BUTTON_1) == GLFW_PRESS) {
                camera.rotate((float) (mouseY - prevMouseY) / -5, (float) (mouseX - prevMouseX) / -5);
            }
            prevMouseX = mouseX;
            prevMouseY = mouseY;
        });
        glfwSetScrollCallback(window.pointer(), (long window, double xoffset, double yoffset) -> {
            camera.zoom((float) -yoffset);
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
