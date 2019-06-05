package net.brandontsang.tetroid;

public class GameObject {
    private float x;
    private float y;
    private float z;
    
    public GameObject() {
        move(0, 0, 0);
    }
    
    public GameObject(float x, float y, float z) {
        move(x, y, z);
    }
    
    public void move(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
}
