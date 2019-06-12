package net.brandontsang.tetroid.tetroid;

import net.brandontsang.tetroid.engine.Renderer;
import net.brandontsang.tetroid.engine.Scene;

import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;

class RenderLoop {
    private static boolean run = false;
    
    static void start(Scene scene) {
        run = true;
        
        while (run) {
            if (glfwWindowShouldClose(scene.window().pointer())) run = false;
            
            Renderer.render(scene);
        }
    }
    
    static void stop() {
        run = false;
    }
}
