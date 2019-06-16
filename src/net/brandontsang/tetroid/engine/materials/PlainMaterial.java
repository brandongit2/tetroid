package net.brandontsang.tetroid.engine.materials;

import org.joml.Vector3f;

public class PlainMaterial implements Material {
    private Vector3f color;
    private float opacity = 1.0f;
    
    public PlainMaterial(Vector3f color) {
        this.color = color;
    }
    
    public Vector3f getColor() {
        return this.color;
    }
    
    public void setOpacity(float opacity) {
        this.opacity = opacity;
    }
    
    public float getOpacity() {
        return this.opacity;
    }
    
    public int matId() {
        return 0;
    }
}
