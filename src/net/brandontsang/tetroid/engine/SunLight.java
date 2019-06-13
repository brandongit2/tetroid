package net.brandontsang.tetroid.engine;

import org.joml.Vector3f;

public class SunLight extends Light {
    private Vector3f direction;
    
    public SunLight(float dirX, float dirY, float dirZ) {
        this.direction = new Vector3f(dirX, dirY, dirZ);
    }
    
    public Vector3f direction() {
        return this.direction;
    }
}
