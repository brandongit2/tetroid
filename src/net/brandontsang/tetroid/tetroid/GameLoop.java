package net.brandontsang.tetroid.tetroid;

import net.brandontsang.tetroid.engine.Scene;

class GameLoop implements Runnable {
    private static final long nsPerTick = 1000000000 / 60;
    private static boolean run = false;
    
    private int[][][] lattice;
    
    private Scene scene;
    
    public GameLoop(Scene scene, int xSize, int ySize, int zSize) {
        this.scene = scene;
        this.lattice = new int[xSize][ySize][zSize];
    }
    
    public void run() {
        run = true;
    
        long time = System.nanoTime();
        while (run) {
            // Game logic here
            
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
    
    static void start(Scene scene, int xSize, int ySize, int zSize) {
        new Thread(new GameLoop(scene, xSize, ySize, zSize)).start();
    }
    
    static void stop() {
        run = false;
    }
}
