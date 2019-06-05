package net.brandontsang.tetroid;

public class GameLoop implements Runnable {
    private final long msPerTick = 1000 / 20;
    
    public void run() {
        long time = System.currentTimeMillis();
        while (Main.gameIsRunning) {
            // Game logic
    
            long delta = System.currentTimeMillis() - time;
            if (delta > msPerTick) {
                try {
                    Thread.sleep(delta);
                } catch (InterruptedException e) {
                    break;
                }
            }
        }
    }
}
