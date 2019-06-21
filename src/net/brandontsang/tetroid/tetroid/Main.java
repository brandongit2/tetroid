package net.brandontsang.tetroid.tetroid;

import net.brandontsang.tetroid.engine.*;
import net.brandontsang.tetroid.engine.lights.SunLight;
import net.brandontsang.tetroid.engine.materials.PhongMaterial;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector3i;
import org.lwjgl.glfw.GLFWErrorCallback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL20.*;

public class Main {
    private Window window;
    
    public static int   program_3d;
    public static int   program_gui;
    public static int   program_depthMap;
    public static Scene scene;
           static Gui   gui;
    
    private double prevMouseX = 0;
    private double prevMouseY = 0;
    // Since mouse input checks movement deltas, the first check will be erroneous. Don't check
    // the first tick.
    private boolean firstCall = true;
    
    static final int GRID_LENGTH = 10;
    static final int GRID_WIDTH = 10;
    static final int GRID_HEIGHT = 20;
    
    private int[] posXWall = new int[GRID_LENGTH + GRID_HEIGHT + 2];
    private int[] negXWall = new int[GRID_LENGTH + GRID_HEIGHT + 2];
    private int[] posZWall = new int[GRID_WIDTH + GRID_HEIGHT + 2];
    private int[] negZWall = new int[GRID_WIDTH + GRID_HEIGHT + 2];
    
    private static int score;
    
    private void run() {
        init();
        
        ShaderProgram _program_3d = new ShaderProgram(
            "/res/shaders/3d.vert.glsl",
            "/res/shaders/3d.frag.glsl"
        );
        glUseProgram(_program_3d.pointer());
        _program_3d.createUniform("projectionMatrix");
        _program_3d.createUniform("viewMatrix");
        _program_3d.createUniform("modelMatrix");
        _program_3d.createUniform("lightSpaceMatrix");
        _program_3d.createUniform("matId");
        _program_3d.createUniform("color");
        _program_3d.createUniform("isTextured");
        _program_3d.createUniform("textureTile");
        _program_3d.createUniform("depthMap");
        _program_3d.createUniform("opacity");
        _program_3d.createUniform("ambient");
        _program_3d.createUniform("cameraPos");
        _program_3d.createUniform("reflectivity");
        _program_3d.createUniform("shininess");
        for (int i = 0; i < 3; i++) {
            _program_3d.createUniform("lightType[" + i + "]");
            _program_3d.createUniform("lightColor[" + i + "]");
            _program_3d.createUniform("lightPos[" + i + "]");
            _program_3d.createUniform("lightDir[" + i + "]");
        }
        program_3d = scene.add(_program_3d);
        
        ShaderProgram _program_gui = new ShaderProgram(
            "/res/shaders/gui.vert.glsl",
            "/res/shaders/gui.frag.glsl"
        );
        glUseProgram(_program_gui.pointer());
        _program_gui.createUniform("projectionMatrix");
        _program_gui.createUniform("modelMatrix");
        _program_gui.createUniform("elementType");
        _program_gui.createUniform("isTextured");
        _program_gui.createUniform("opacity");
        _program_gui.createUniform("textureTile");
        _program_gui.createUniform("textColor");
        program_gui = scene.add(_program_gui);
        
        ShaderProgram _program_depthMap = new ShaderProgram(
            "/res/shaders/depthMap.vert.glsl",
            "/res/shaders/depthMap.frag.glsl"
        );
        glUseProgram(_program_depthMap.pointer());
        _program_depthMap.createUniform("lightProjection");
        _program_depthMap.createUniform("lightView");
        _program_depthMap.createUniform("modelMatrix");
        program_depthMap = scene.add(_program_depthMap);
    
        OrbitCamera camera = new OrbitCamera(5.0f, 8.0f, 5.0f, 5.0f, 70.0f, 0.1f, 1000.0f, window);
        camera.rotate(-25.0f, -20.0f);
        camera.zoom(4.0f);
        int camId = scene.add(camera);
        scene.setCurrentCamera(camId);
    
        scene.setAmbientLight(new Vector3f(0.2f, 0.2f, 0.2f));
        scene.add(new SunLight(new Vector3f(1.0f, 1.0f, 1.0f), new Vector3f(2.0f, -5.0f, -4.0f), scene));
        
        drawWalls();
        
        try {
            scene.add(Mesh.fromFile("/res/models/plane.obj", new PhongMaterial(new Vector3f(0.3f, 0.3f, 0.3f), 0.4f, 100.0f)).translate(0.0f, -0.001f, 0.0f).scale(10.0f));
            Collection logo = new Collection(new Mesh[] {
                Mesh.fromFile("/res/models/logo/capital-t.obj", new PhongMaterial(new Vector3f(1.0f, 0.2f, 0.2f), 0.8f, 1000.0f)).translate(-13.0f, 0.0f, 0.0f),
                Mesh.fromFile("/res/models/logo/e.obj", new PhongMaterial(new Vector3f(1.0f, 0.8f, 0.2f), 0.8f, 1000.0f)).translate(-9.0f, -3.0f, 0.0f),
                Mesh.fromFile("/res/models/logo/t.obj", new PhongMaterial(new Vector3f(1.0f, 1.0f, 0.2f), 0.8f, 1000.0f)).translate(-4.5f, -1.0f, 0.0f),
                Mesh.fromFile("/res/models/logo/r.obj", new PhongMaterial(new Vector3f(0.2f, 1.0f, 0.2f), 0.8f, 1000.0f)).translate(0.0f, -1.5f, 0.0f),
                Mesh.fromFile("/res/models/logo/o.obj", new PhongMaterial(new Vector3f(0.2f, 0.8f, 1.0f), 0.8f, 1000.0f)).translate(4.5f, -1.0f, 0.0f),
                Mesh.fromFile("/res/models/logo/i.obj", new PhongMaterial(new Vector3f(0.2f, 0.2f, 1.0f), 0.8f, 1000.0f)).translate(8.0f, 0.5f, 0.0f),
                Mesh.fromFile("/res/models/logo/d.obj", new PhongMaterial(new Vector3f(0.8f, 0.2f, 1.0f), 0.8f, 1000.0f)).translate(12.0f, -1.0f, 0.0f)
            });
            logo.translate(5.0f, 23.0f, 5.0f).rotate(0.0f, -45.0f, 0.0f).scale(0.4f);
            scene.add(logo);
        } catch (IOException err) {
            err.printStackTrace();
            System.exit(1);
        }
    
        glfwSetKeyCallback(window.pointer(), (long window, int key, int scancode, int action, int mods) -> {
            if (action == GLFW_PRESS) {
                int forward   = 0;
                int rightward = 0;
                int upward    = 0;
                
                int rotForward   = 0;
                int rotRightward = 0;
                int rotClockwise = 0;
    
                switch (key) {
                    case GLFW_KEY_ESCAPE:
                        terminate();
                        break;
                    case GLFW_KEY_SPACE:
                        upward = -1;
                        break;
                    case GLFW_KEY_UP:
                        forward = 1;
                        break;
                    case GLFW_KEY_RIGHT:
                        rightward = 1;
                        break;
                    case GLFW_KEY_DOWN:
                        forward = -1;
                        break;
                    case GLFW_KEY_LEFT:
                        rightward = -1;
                        break;
                    case GLFW_KEY_W:
                        rotForward = 1;
                        break;
                    case GLFW_KEY_D:
                        rotRightward = 1;
                        break;
                    case GLFW_KEY_S:
                        rotForward = 3;
                        break;
                    case GLFW_KEY_A:
                        rotRightward = 3;
                        break;
                    case GLFW_KEY_Q:
                        rotClockwise = 1;
                        break;
                    case GLFW_KEY_E:
                        rotClockwise = 3;
                        break;
                    case GLFW_KEY_ENTER: {
                        int[] translationDown = new int[4];
                        for (int i = 0; i < GameGrid.active.length; i++) {
                            Block activeBlock;
                            try {
                                activeBlock = GameGrid.blocks.get(GameGrid.active[i]);
                            } catch (ArrayIndexOutOfBoundsException err) {
                                break;
                            }
                            Vector2f xzPos       = new Vector2f(activeBlock.getPos().x, activeBlock.getPos().z);
        
                            ArrayList<Integer> sameColumn = new ArrayList<>();
                            iterateAllBlocks:
                            for (Block block : GameGrid.blocks) {
                                for (int a : GameGrid.active) {
                                    if (GameGrid.blocks.get(a) == block) continue iterateAllBlocks;
                                }
            
                                if (new Vector2f(block.getPos().x, block.getPos().z).equals(xzPos)) {
                                    sameColumn.add(block.getPos().y);
                                }
                            }
        
                            if (sameColumn.size() == 0) {
                                translationDown[i] = activeBlock.getPos().y + 1;
                            } else {
                                translationDown[i] = activeBlock.getPos().y - Collections.max(sameColumn);
                            }
                        }
                        int min = 100;
                        for (int t : translationDown) {
                            if (t < min) min = t;
                        }
                        min--; // `min` is the furthest the active block can travel down without hitting anything.
                        
                        upward = -min;
                        
                        break;
                    }
                }
    
                float rot = camera.getOrientation().y;
                rot -= Math.PI / 4;
                if (Math.cos(rot) > 0 && Math.sin(rot) >= 0) {
                    GameGrid.translate(new Vector3i(forward, upward, rightward));
                    GameGrid.rotate(0, 4 - rotRightward);
                    GameGrid.rotate(1, 4 - rotClockwise);
                    GameGrid.rotate(2, 4 - rotForward);
                } else if (Math.cos(rot) <= 0.0 && Math.sin(rot) > 0.0) {
                    GameGrid.translate(new Vector3i(-rightward, upward, forward));
                    GameGrid.rotate(0, 4 - rotForward);
                    GameGrid.rotate(1, 4 - rotClockwise);
                    GameGrid.rotate(2, rotRightward);
                } else if (Math.cos(rot) < 0.0 && Math.sin(rot) <= 0.0) {
                    GameGrid.translate(new Vector3i(-forward, upward, -rightward));
                    GameGrid.rotate(0, rotRightward);
                    GameGrid.rotate(1, 4 - rotClockwise);
                    GameGrid.rotate(2, rotForward);
                } else if (Math.cos(rot) >= 0.0 && Math.sin(rot) < 0.0) {
                    GameGrid.translate(new Vector3i(rightward, upward, -forward));
                    GameGrid.rotate(0, rotForward);
                    GameGrid.rotate(1, 4 - rotClockwise);
                    GameGrid.rotate(2, 4 - rotRightward);
                }
            }
        });
        glfwSetCursorPosCallback(window.pointer(), (long evWindow, double mouseX, double mouseY) -> {
            if (firstCall) {
                firstCall = false;
            } else if (glfwGetMouseButton(window.pointer(), GLFW_MOUSE_BUTTON_1) == GLFW_PRESS) {
                camera.rotate((float) (mouseY - prevMouseY) / -5, (float) (mouseX - prevMouseX) / -5);
                
                // Set near walls transparent
                float rot = camera.getOrientation().y;
                if (Math.cos(rot) > 0.0 && Math.sin(rot) >= 0.0) {
                    for (int i : posXWall) scene.getLine(i).setOpacity(0.5f);
                    for (int i : posZWall) scene.getLine(i).setOpacity(0.1f);
                    for (int i : negXWall) scene.getLine(i).setOpacity(0.1f);
                    for (int i : negZWall) scene.getLine(i).setOpacity(0.5f);
                } else if (Math.cos(rot) <= 0.0 && Math.sin(rot) > 0.0) {
                    for (int i : posXWall) scene.getLine(i).setOpacity(0.5f);
                    for (int i : posZWall) scene.getLine(i).setOpacity(0.5f);
                    for (int i : negXWall) scene.getLine(i).setOpacity(0.1f);
                    for (int i : negZWall) scene.getLine(i).setOpacity(0.1f);
                } else if (Math.cos(rot) < 0.0 && Math.sin(rot) <= 0.0) {
                    for (int i : posXWall) scene.getLine(i).setOpacity(0.1f);
                    for (int i : posZWall) scene.getLine(i).setOpacity(0.5f);
                    for (int i : negXWall) scene.getLine(i).setOpacity(0.5f);
                    for (int i : negZWall) scene.getLine(i).setOpacity(0.1f);
                } else if (Math.cos(rot) >= 0.0 && Math.sin(rot) < 0.0) {
                    for (int i : posXWall) scene.getLine(i).setOpacity(0.1f);
                    for (int i : posZWall) scene.getLine(i).setOpacity(0.1f);
                    for (int i : negXWall) scene.getLine(i).setOpacity(0.5f);
                    for (int i : negZWall) scene.getLine(i).setOpacity(0.5f);
                }
            }
            this.prevMouseX = mouseX;
            this.prevMouseY = mouseY;
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
        
        this.window = new Window(1280, 720, "Tetroid");
        scene = new Scene(this.window);
        
        gui = new Gui(scene);
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
    
    private void drawWalls() {
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
    }
    
    static void incrementScore(int amt) {
        Main.score += amt;
        gui.updateScore(Main.score);
    }
}
