package net.brandontsang.tetroid.tetroid;

import net.brandontsang.tetroid.engine.*;
import net.brandontsang.tetroid.engine.lights.SunLight;
import net.brandontsang.tetroid.engine.materials.PhongMaterial;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFWErrorCallback;

import java.io.IOException;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL20.*;

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
    
    static final int GRID_LENGTH = 10;
    static final int GRID_WIDTH = 10;
    static final int GRID_HEIGHT = 20;
    
    private void run() {
        init();
        
        scene = new Scene(window);
        
        program = new ShaderProgram();
        glUseProgram(program.pointer());
        program.createUniform("projectionMatrix");
        program.createUniform("viewMatrix");
        program.createUniform("modelMatrix");
        program.createUniform("matId");
        program.createUniform("opacity");
        program.createUniform("ambient");
        program.createUniform("cameraPos");
        program.createUniform("reflectivity");
        program.createUniform("shininess");
        for (int i = 0; i < 3; i++) {
            program.createUniform("lightType[" + i + "]");
            program.createUniform("lightColor[" + i + "]");
            program.createUniform("lightPos[" + i + "]");
            program.createUniform("lightDir[" + i + "]");
        }
        scene.setShaderProgram(program);
        
        // Draw grid
        int[] posXWall = new int[GRID_LENGTH + GRID_HEIGHT + 2];
        int[] negXWall = new int[GRID_LENGTH + GRID_HEIGHT + 2];
        int[] posZWall = new int[GRID_WIDTH + GRID_HEIGHT + 2];
        int[] negZWall = new int[GRID_WIDTH + GRID_HEIGHT + 2];
        int posXCounter = 0;
        int negXCounter = 0;
        int posZCounter = 0;
        int negZCounter = 0;
        for (int i = 0; i <= GRID_WIDTH; i++) {
            scene.add(new Line(new Vector3f(i, 0.0f, 0.0f), new Vector3f(i, 0.0f, GRID_LENGTH), new Vector3f(1.0f, 1.0f, 1.0f), 0.5f));
            negZWall[negZCounter] = scene.add(new Line(new Vector3f(i, 0.0f, 0.0f), new Vector3f(i, GRID_HEIGHT, 0.0f), new Vector3f(1.0f, 1.0f, 1.0f), 0.5f));
            negZCounter++;
            posZWall[posZCounter] = scene.add(new Line(new Vector3f(i, 0.0f, GRID_LENGTH), new Vector3f(i, GRID_HEIGHT, GRID_LENGTH), new Vector3f(1.0f, 1.0f, 1.0f), 0.1f));
            posZCounter++;
        }
        for (int i = 0; i <= GRID_LENGTH; i++) {
            scene.add(new Line(new Vector3f(0.0f, 0.0f, i), new Vector3f(GRID_WIDTH, 0.0f, i), new Vector3f(1.0f, 1.0f, 1.0f), 0.5f));
            negXWall[negXCounter] = scene.add(new Line(new Vector3f(0.0f, 0.0f, i), new Vector3f(0.0f, GRID_HEIGHT, i), new Vector3f(1.0f, 1.0f, 1.0f), 0.1f));
            negXCounter++;
            posXWall[posXCounter] = scene.add(new Line(new Vector3f(GRID_WIDTH, 0.0f, i), new Vector3f(GRID_WIDTH, GRID_HEIGHT, i), new Vector3f(1.0f, 1.0f, 1.0f), 0.5f));
            posXCounter++;
        }
        for (int i = 0; i <= GRID_HEIGHT; i++) {
            negXWall[negXCounter] = scene.add(new Line(new Vector3f(0.0f, i, 0.0f), new Vector3f(0.0f, i, GRID_LENGTH), new Vector3f(1.0f, 1.0f, 1.0f), 0.1f));
            negXCounter++;
            posXWall[posXCounter] = scene.add(new Line(new Vector3f(GRID_WIDTH, i, 0.0f), new Vector3f(GRID_WIDTH, i, GRID_LENGTH), new Vector3f(1.0f, 1.0f, 1.0f), 0.5f));
            posXCounter++;
            negZWall[negZCounter] = scene.add(new Line(new Vector3f(0.0f, i, 0.0f), new Vector3f(GRID_WIDTH, i, 0.0f), new Vector3f(1.0f, 1.0f, 1.0f), 0.5f));
            negZCounter++;
            posZWall[posZCounter] = scene.add(new Line(new Vector3f(0.0f, i, GRID_LENGTH), new Vector3f(GRID_WIDTH, i, GRID_LENGTH), new Vector3f(1.0f, 1.0f, 1.0f), 0.1f));
            posZCounter++;
        }
        
        try {
            scene.add(Mesh.fromFile("./res/models/plane.obj", new PhongMaterial(new Vector3f(0.1f, 0.1f, 0.1f), 0.5f, 10.0f)).translate(0.0f, -0.01f, 0.0f));
        } catch (IOException err) {
            err.printStackTrace();
            System.exit(1);
        }
        
        scene.setAmbientLight(new Vector3f(0.1f, 0.1f, 0.1f));
        scene.add(new SunLight(new Vector3f(0.7f, 0.7f, 0.7f), new Vector3f(10.0f, 7.0f, 2.0f)));
        
        OrbitCamera camera = new OrbitCamera(5.0f, 8.0f, 5.0f, 5.0f, 70.0f, 0.1f, 1000.0f, window);
        camera.rotate(-30.0f, -55.0f);
        camera.zoom(4.0f);
        int camId = scene.add(camera);
        scene.setCurrentCamera(camId);
        
        glfwSetCursorPosCallback(window.pointer(), (long evWindow, double mouseX, double mouseY) -> {
            if (firstCall) {
                firstCall = false;
            } else if (glfwGetMouseButton(window.pointer(), GLFW_MOUSE_BUTTON_1) == GLFW_PRESS) {
                camera.rotate((float) (mouseY - prevMouseY) / -5, (float) (mouseX - prevMouseX) / -5);
                
                float rot = camera.getOrientation().y;
                if (Math.cos(rot) > 0.0 && Math.sin(rot) >= 0.0) {
                    for (int i : posXWall) {
                        scene.getLine(i).setOpacity(0.5f);
                    }
                    for (int i : posZWall) {
                        scene.getLine(i).setOpacity(0.1f);
                    }
                    for (int i : negXWall) {
                        scene.getLine(i).setOpacity(0.1f);
                    }
                    for (int i : negZWall) {
                        scene.getLine(i).setOpacity(0.5f);
                    }
                } else if (Math.cos(rot) <= 0.0 && Math.sin(rot) > 0.0) {
                    for (int i : posXWall) {
                        scene.getLine(i).setOpacity(0.5f);
                    }
                    for (int i : posZWall) {
                        scene.getLine(i).setOpacity(0.5f);
                    }
                    for (int i : negXWall) {
                        scene.getLine(i).setOpacity(0.1f);
                    }
                    for (int i : negZWall) {
                        scene.getLine(i).setOpacity(0.1f);
                    }
                } else if (Math.cos(rot) < 0.0 && Math.sin(rot) <= 0.0) {
                    for (int i : posXWall) {
                        scene.getLine(i).setOpacity(0.1f);
                    }
                    for (int i : posZWall) {
                        scene.getLine(i).setOpacity(0.5f);
                    }
                    for (int i : negXWall) {
                        scene.getLine(i).setOpacity(0.5f);
                    }
                    for (int i : negZWall) {
                        scene.getLine(i).setOpacity(0.1f);
                    }
                } else if (Math.cos(rot) >= 0.0 && Math.sin(rot) < 0.0) {
                    for (int i : posXWall) {
                        scene.getLine(i).setOpacity(0.1f);
                    }
                    for (int i : posZWall) {
                        scene.getLine(i).setOpacity(0.1f);
                    }
                    for (int i : negXWall) {
                        scene.getLine(i).setOpacity(0.5f);
                    }
                    for (int i : negZWall) {
                        scene.getLine(i).setOpacity(0.5f);
                    }
                }
            }
            prevMouseX = mouseX;
            prevMouseY = mouseY;
        });
        glfwSetScrollCallback(window.pointer(), (long window, double xoffset, double yoffset) -> {
            camera.zoom((float) -yoffset);
        });
        
        RenderLoop.start(scene);
        
        terminate();
    }
    
    private void init() {
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();
        
        // Initialize GLFW.
        if (!glfwInit()) throw new IllegalStateException("Unable to initialize GLFW");
        
        window = new Window(1280, 720, "Tetroid");
        
        glfwSetKeyCallback(window.pointer(), (long window, int key, int scancode, int action, int mods) -> {
            if (action == GLFW_RELEASE) {
                if (key == GLFW_KEY_ESCAPE) { // Close the window.
                    terminate();
                }
            }
        });
    }
    
    private void terminate() {
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
