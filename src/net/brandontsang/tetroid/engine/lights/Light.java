package net.brandontsang.tetroid.engine.lights;

import org.joml.Vector3f;

public class Light {
    protected Vector3f color = new Vector3f(1.0f, 1.0f, 1.0f);
    
    public Vector3f getColor() {
        return this.color;
    }
    
    public void setPosition(Vector3f pos) {}
    
    public Vector3f getPosition() {
        return new Vector3f(0.0f, 0.0f, 0.0f);
    }
    
    public Vector3f getDirection() {
        return new Vector3f(0.0f, 0.0f, 0.0f);
    }
}
