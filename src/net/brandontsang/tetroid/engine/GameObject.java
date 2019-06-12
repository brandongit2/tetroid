package net.brandontsang.tetroid.engine;

abstract class GameObject {
    float[] pos = new float[3];
    
    public GameObject() {
        move(0, 0, 0);
    }
    
    public GameObject(float x, float y, float z) {
        move(x, y, z);
    }
    
    public void move(float x, float y, float z) {
        this.pos[0] = x;
        this.pos[1] = y;
        this.pos[2] = z;
    }
}
