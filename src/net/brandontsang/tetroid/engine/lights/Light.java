package net.brandontsang.tetroid.engine.lights;

import org.joml.Vector3f;

public interface Light {
    public int lightType();
    
    public Vector3f getColor();
    
    default public void setPosition(Vector3f pos) {}
    
    default public Vector3f getPosition() {
        return new Vector3f(0.0f, 0.0f, 0.0f);
    }
    
    default public Vector3f getDirection() {
        return new Vector3f(0.0f, -1.0f, 0.0f);
    }
}
