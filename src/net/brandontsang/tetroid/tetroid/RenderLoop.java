package net.brandontsang.tetroid.tetroid;

import net.brandontsang.tetroid.engine.Renderer;
import net.brandontsang.tetroid.engine.Scene;
import org.joml.Vector3f;
import org.joml.Vector3i;

import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;

class RenderLoop {
    private static final long    nsPerTick = 1000000000 / 60;
    private static       boolean run       = false;
    private static       long    tick      = 0;
    
    private static ArrayList<RenderLoopAttatchment> attatchments = new ArrayList<>();
    
    static void start(Scene scene) {
        run = true;
    
        long time = System.nanoTime();
        while (run) {
            if (glfwWindowShouldClose(scene.window().pointer())) run = false;
            
            if (tick % 30 == 0) {
                if (GameGrid.existsActiveTetromino) {
                    GameGrid.translate(new Vector3i(0, -1, 0));
                    
                    // Eliminate completed rows
                    ArrayList<ArrayList<Integer>> rows = new ArrayList<>();
                    for (int i = 0; i < Main.GRID_HEIGHT; i++) {
                        rows.add(new ArrayList<>());
                    }
                    
                    for (Block block : GameGrid.getBlocks()) {
                        rows.get(block.getPos().y).add(1);
                    }
                    
                    for (int i = 0; i < Main.GRID_HEIGHT; i++) {
                        if (rows.get(i).size() == Main.GRID_LENGTH * Main.GRID_WIDTH) {
                            Main.incrementScore(500);
                            for (Block block : GameGrid.getBlocks()) {
                                if (block.getPos().y == i) {
                                    scene.remove(block.mesh);
                                } else if (block.getPos().y > i) {
                                    Vector3i newPos = new Vector3i();
                                    block.pos.add(0, -1, 0, newPos);
                                    block.setPos(newPos);
                                }
                            }
                        }
                    }
                } else {
                    GameGrid.addTetromino();
                }
            }
            
            for (RenderLoopAttatchment a : attatchments) {
                a.run();
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
    
    static void attach(RenderLoopAttatchment attatchment) {
        attatchments.add(attatchment);
    }
}
