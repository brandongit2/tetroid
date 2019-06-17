package net.brandontsang.tetroid.tetroid;

import org.joml.Vector3i;

import java.util.ArrayList;

class GameGrid {
    static ArrayList<Block> blocks = new ArrayList<>();
    
    static boolean existsActiveTetromino = false;
    private static int[] active = new int[4];
    private static int pivot;
    
    static void addTetromino() {
        existsActiveTetromino = true;
//        int tetromino = (int) (Math.random() * 8);
        int tetromino = 5;
        switch (tetromino) {
            case 0:
                blocks.add(new Block(tetromino, new Vector3i(4, 17, 4)));
                active[0] = blocks.size() - 1;
                pivot = blocks.size() - 1;
                blocks.add(new Block(tetromino, new Vector3i(5, 17, 4)));
                active[1] = blocks.size() - 1;
                blocks.add(new Block(tetromino, new Vector3i(4, 17, 5)));
                active[2] = blocks.size() - 1;
                blocks.add(new Block(tetromino, new Vector3i(5, 17, 5)));
                active[3] = blocks.size() - 1;
                break;
            case 1:
                blocks.add(new Block(tetromino, new Vector3i(3, 17, 4)));
                active[0] = blocks.size() - 1;
                blocks.add(new Block(tetromino, new Vector3i(4, 17, 4)));
                active[1] = blocks.size() - 1;
                pivot = blocks.size() - 1;
                blocks.add(new Block(tetromino, new Vector3i(5, 17, 4)));
                active[2] = blocks.size() - 1;
                blocks.add(new Block(tetromino, new Vector3i(6, 17, 4)));
                active[3] = blocks.size() - 1;
                break;
            case 2:
                blocks.add(new Block(tetromino, new Vector3i(5, 17, 4)));
                active[0] = blocks.size() - 1;
                blocks.add(new Block(tetromino, new Vector3i(4, 17, 5)));
                active[1] = blocks.size() - 1;
                blocks.add(new Block(tetromino, new Vector3i(5, 17, 5)));
                active[2] = blocks.size() - 1;
                pivot = blocks.size() - 1;
                blocks.add(new Block(tetromino, new Vector3i(6, 17, 5)));
                active[3] = blocks.size() - 1;
                break;
            case 3:
                blocks.add(new Block(tetromino, new Vector3i(3, 17, 5)));
                active[0] = blocks.size() - 1;
                blocks.add(new Block(tetromino, new Vector3i(4, 17, 5)));
                active[1] = blocks.size() - 1;
                blocks.add(new Block(tetromino, new Vector3i(5, 17, 5)));
                active[2] = blocks.size() - 1;
                pivot = blocks.size() - 1;
                blocks.add(new Block(tetromino, new Vector3i(5, 17, 4)));
                active[3] = blocks.size() - 1;
                break;
            case 4:
                blocks.add(new Block(tetromino, new Vector3i(4, 17, 5)));
                active[0] = blocks.size() - 1;
                blocks.add(new Block(tetromino, new Vector3i(5, 17, 5)));
                active[1] = blocks.size() - 1;
                pivot = blocks.size() - 1;
                blocks.add(new Block(tetromino, new Vector3i(5, 17, 4)));
                active[2] = blocks.size() - 1;
                blocks.add(new Block(tetromino, new Vector3i(6, 17, 4)));
                active[3] = blocks.size() - 1;
                break;
            case 5:
                blocks.add(new Block(tetromino, new Vector3i(4, 17, 5)));
                active[0] = blocks.size() - 1;
                blocks.add(new Block(tetromino, new Vector3i(4, 17, 4)));
                active[1] = blocks.size() - 1;
                blocks.add(new Block(tetromino, new Vector3i(5, 17, 4)));
                active[2] = blocks.size() - 1;
                blocks.add(new Block(tetromino, new Vector3i(5, 18, 4)));
                active[3] = blocks.size() - 1;
                pivot = blocks.size() - 1;
                break;
            case 6:
                blocks.add(new Block(tetromino, new Vector3i(4, 18, 4)));
                active[0] = blocks.size() - 1;
                blocks.add(new Block(tetromino, new Vector3i(4, 17, 4)));
                active[1] = blocks.size() - 1;
                pivot = blocks.size() - 1;
                blocks.add(new Block(tetromino, new Vector3i(5, 17, 4)));
                active[2] = blocks.size() - 1;
                blocks.add(new Block(tetromino, new Vector3i(5, 17, 5)));
                active[3] = blocks.size() - 1;
                break;
            case 7:
                blocks.add(new Block(tetromino, new Vector3i(4, 17, 4)));
                active[0] = blocks.size() - 1;
                blocks.add(new Block(tetromino, new Vector3i(5, 17, 4)));
                active[1] = blocks.size() - 1;
                pivot = blocks.size() - 1;
                blocks.add(new Block(tetromino, new Vector3i(5, 17, 5)));
                active[2] = blocks.size() - 1;
                blocks.add(new Block(tetromino, new Vector3i(5, 18, 4)));
                active[3] = blocks.size() - 1;
                break;
        }
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
            
            Vector3i pivotPos = blocks.get(pivot).pos;
            Vector3i newPos = new Vector3i();
            blocks.get(a).pos.sub(pivotPos, newPos);
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
            newPos.add(pivotPos);
            
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
                Vector3i pivotPos = blocks.get(pivot).pos;
                Vector3i newPos   = new Vector3i();
                blocks.get(a).pos.sub(pivotPos, newPos);
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
                newPos.add(pivotPos);
                blocks.get(a).setPos(newPos);
            }
        }
    }
    
    private static void clearActive() {
        existsActiveTetromino = false;
        for (int i = 0; i < active.length; i++) {
            active[i] = -1;
        }
    }
}
