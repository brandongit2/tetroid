package net.brandontsang.tetroid.tetroid;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector3i;

import java.util.ArrayList;
import java.util.Collections;

class GameGrid {
    static ArrayList<Block> blocks = new ArrayList<>();
    
    static boolean existsActiveTetromino = false;
    
    public static int[]     active = new int[4];
    private static Block[]  ghosts = new Block[4];
    private static Vector3i pivot;
    
    static {
        for (int i = 0; i < GameGrid.ghosts.length; i++) {
            GameGrid.ghosts[i] = new Block(new Vector3f(1.0f, 1.0f, 1.0f), new Vector3i(0, -2, 0)).setOpacity(0.3f);
            Main.scene.add(GameGrid.ghosts[i].getMesh());
        }
    }
    
    static void addTetromino() {
        GameGrid.existsActiveTetromino = true;
        Tetromino tetromino = Tetromino.random();
        for (int i = 0; i < tetromino.getPositions().length; i++) {
            blocks.add(new Block(tetromino.getColor(), tetromino.getPositions()[i]));
            active[i] = blocks.size() - 1;
            GameGrid.ghosts[i].setColor(tetromino.getColor());
        }
        GameGrid.pivot = tetromino.getPivot();
        
        updateGhosts();
    }
    
    static void translate(Vector3i pos) {
        boolean isValidMove = true;
        activeIterator:
        for (int a : active) {
            if (a < 0) return;
            
            Vector3i newPos = new Vector3i();
            blocks.get(a).pos.add(pos, newPos);
            
            if (newPos.y < 0 || newPos.y >= Main.GRID_HEIGHT) {
                clearActive();
                isValidMove = false;
                break;
            }
            if (newPos.x < 0 || newPos.x >= Main.GRID_WIDTH || newPos.z < 0 || newPos.z >= Main.GRID_LENGTH) {
                isValidMove = false;
                break;
            }
            
            for (int i = 0; i < blocks.size(); i++) {
                boolean blockIsActive = false;
                for (int el : active) if (el == i) blockIsActive = true;
                if (!blockIsActive && blocks.get(i).pos.equals(newPos)) {
                    isValidMove = false;
                    if (pos.y != 0) clearActive();
                    break activeIterator;
                }
            }
        }
    
        if (isValidMove) {
            for (int a : active) {
                Vector3i newPos = new Vector3i();
                blocks.get(a).pos.add(pos, newPos);
                blocks.get(a).setPos(newPos);
            }
            GameGrid.pivot.add(pos);
            
            updateGhosts();
        }
    }
    
    // Counter-clockwise.
    // 0 - x-axis
    // 1 - y-axis
    // 2 - z-axis
    static void rotate(int axis, int amt) {
        boolean isValidMove = true;
        activeIterator:
        for (int a : active) {
            if (a < 0) return;
            
            Vector3i newPos = new Vector3i();
            blocks.get(a).pos.sub(pivot, newPos);
            for (int i = 0; i < amt; i++) {
                switch (axis) {
                    case 0: {
                        int yTemp = newPos.y;
                        newPos.y = newPos.z;
                        newPos.z = -yTemp;
                        break;
                    }
                    case 1: {
                        int zTemp = newPos.z;
                        newPos.z = newPos.x;
                        newPos.x = -zTemp;
                        break;
                    }
                    case 2: {
                        int yTemp = newPos.y;
                        newPos.y = newPos.x;
                        newPos.x = -yTemp;
                        break;
                    }
                }
            }
            newPos.add(pivot);
            
            if (newPos.x < 0 || newPos.x >= Main.GRID_WIDTH || newPos.y < 0 || newPos.y >= Main.GRID_HEIGHT || newPos.z < 0 || newPos.z >= Main.GRID_LENGTH) {
                isValidMove = false;
                break;
            }
            
            for (int i = 0; i < blocks.size(); i++) {
                boolean blockIsActive = false;
                for (int el : active) if (el == i) blockIsActive = true;
                if (!blockIsActive && blocks.get(i).pos.equals(newPos)) {
                    isValidMove = false;
                    break activeIterator;
                }
            }
        }
        
        if (isValidMove) {
            for (int a : active) {
                Vector3i newPos   = new Vector3i();
                blocks.get(a).pos.sub(pivot, newPos);
                for (int i = 0; i < amt; i++) {
                    switch (axis) {
                        case 0: {
                            int yTemp = newPos.y;
                            newPos.y = newPos.z;
                            newPos.z = -yTemp;
                            break;
                        }
                        case 1: {
                            int zTemp = newPos.z;
                            newPos.z = newPos.x;
                            newPos.x = -zTemp;
                            break;
                        }
                        case 2: {
                            int yTemp = newPos.y;
                            newPos.y = newPos.x;
                            newPos.x = -yTemp;
                            break;
                        }
                    }
                }
                newPos.add(pivot);
                blocks.get(a).setPos(newPos);
            }
            
            updateGhosts();
        }
    }
    
    private static void updateGhosts() {
        int[] translationDown = new int[4];
        for (int i = 0; i < GameGrid.active.length; i++) {
            Block activeBlock = GameGrid.blocks.get(GameGrid.active[i]);
            Vector2f xzPos = new Vector2f(activeBlock.getPos().x, activeBlock.getPos().z);
            
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
        
        ArrayList<Integer> activeHeights = new ArrayList<>();
        for (int i = 0; i < GameGrid.active.length; i++) {
            activeHeights.add(GameGrid.blocks.get(GameGrid.active[i]).getPos().y);
        }
        int activeHeight = Collections.max(activeHeights) - Collections.min(activeHeights) + 1;
        
        for (int i = 0; i < GameGrid.active.length; i++) {
            if (min >= activeHeight) {
                Block activeBlock = GameGrid.blocks.get(GameGrid.active[i]);
                GameGrid.ghosts[i].setPos(new Vector3i(activeBlock.getPos()).add(0, -min, 0));
            } else {
                GameGrid.ghosts[i].setPos(new Vector3i(0, -2, 0));
            }
        }
    }
    
    private static void clearActive() {
        Main.incrementScore(10);
        existsActiveTetromino = false;
        for (int i = 0; i < active.length; i++) {
            active[i] = -1;
        }
        
        for (Block ghost : GameGrid.ghosts) {
            ghost.setPos(new Vector3i(0, -2, 0));
        }
    }
    
    public static ArrayList<Block> getBlocks() {
        return blocks;
    }
}
