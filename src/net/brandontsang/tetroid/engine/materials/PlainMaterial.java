package net.brandontsang.tetroid.engine.materials;

import org.joml.Vector3f;

public class PlainMaterial implements Material {
    private Vector3f color;
    
    public PlainMaterial(Vector3f color) {
        this.color = color;
    }
    
    public Vector3f getColor() {
        return this.color;
    }
    
    public int matId() {
        return 0;
    }
}
