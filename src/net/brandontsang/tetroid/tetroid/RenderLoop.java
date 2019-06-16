package net.brandontsang.tetroid.tetroid;

import net.brandontsang.tetroid.engine.Renderer;
import net.brandontsang.tetroid.engine.Scene;
import org.joml.Vector3i;

import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;

class RenderLoop {
    private static final long    nsPerTick = 1000000000 / 60;
    private static       boolean run       = false;
    private static       long    tick      = 0;
    
    static void start(Scene scene) {
        run = true;
    
        long time = System.nanoTime();
        while (run) {
            if (glfwWindowShouldClose(scene.window().pointer())) run = false;
    
            if (tick % 1 == 0) {
                if (GameGrid.existsActiveTetromino) {
                    GameGrid.translate(new Vector3i(0, -1, 0));
                } else {
                    GameGrid.addTetromino();
                }
            }
            for (Block block : GameGrid.blocks) {
                if (!block.mesh.isInScene) scene.add(block.mesh);
            }
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
            tick++;
        }
    }
    
    static void stop() {
        run = false;
    }
}
