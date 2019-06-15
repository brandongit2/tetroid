package net.brandontsang.tetroid.tetroid;

import net.brandontsang.tetroid.engine.Renderer;
import net.brandontsang.tetroid.engine.Scene;

import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;

class RenderLoop {
    private static final long    nsPerTick = 1000000000 / 30;
    private static       boolean run       = false;
    
    static void start(Scene scene) {
        run = true;
    
        long time = System.nanoTime();
        while (run) {
            if (glfwWindowShouldClose(scene.window().pointer())) run = false;
            Renderer.render(scene);
            
            long delta = System.nanoTime() - time;
            if (delta < nsPerTick) {
                try {
                    Thread.sleep((nsPerTick - delta) / 1000000, (int) (nsPerTick - delta) % 1000000);
                } catch (InterruptedException e) {
                    break;
                }
            }
            time = System.nanoTime();
        }
    }
    
    static void stop() {
        run = false;
    }
}
