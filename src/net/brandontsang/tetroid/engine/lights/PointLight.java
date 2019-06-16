package net.brandontsang.tetroid.engine.lights;

import org.joml.Vector3f;

public class PointLight implements Light {
    private Vector3f color;
    private Vector3f position;
    
    public int lightType() {
        return 0;
    }
    
    public PointLight(Vector3f color, Vector3f position) {
        this.color = color;
        this.position = position;
    }
    
    public Vector3f getColor() {
        return this.color;
    }
    
    @Override
    public void setPosition(Vector3f pos) {
        this.position = pos;
    }
    
    @Override
    public Vector3f getPosition() {
        return this.position;
    }
}
