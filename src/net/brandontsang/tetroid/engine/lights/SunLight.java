package net.brandontsang.tetroid.engine.lights;

import org.joml.Vector3f;

public class SunLight implements Light {
    private Vector3f color;
    private Vector3f direction;
    
    public int lightType() {
        return 1;
    }
    
    public Vector3f getColor() {
        return this.color;
    }
    
    public SunLight(Vector3f color, Vector3f direction) {
        this.color = color;
        this.direction = direction;
    }
    
    @Override
    public Vector3f getDirection() {
        return this.direction;
    }
}
