package net.brandontsang.tetroid.engine;

import org.joml.Vector3f;

public class PointLight extends Light {
    private Vector3f position;
    
    public PointLight(Vector3f color, Vector3f position) {
        this.color = color;
        this.position = position;
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
