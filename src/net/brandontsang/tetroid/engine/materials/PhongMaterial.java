package net.brandontsang.tetroid.engine.materials;

import org.joml.Vector3f;

public class PhongMaterial implements Material {
    private Vector3f color;
    private float    opacity = 1.0f;
    private float    reflectivity;
    private float    shininess;
    
    public PhongMaterial(Vector3f color, float reflectivity, float shininess) {
        this.color = color;
        this.reflectivity = reflectivity;
        this.shininess = shininess;
    }
    
    public Vector3f getColor() {
        return this.color;
    }
    
    public PhongMaterial setColor(Vector3f color) {
        this.color = color;
        return this;
    }
    
    public void setOpacity(float opacity) {
        this.opacity = opacity;
    }
    
    public float getOpacity() {
        return this.opacity;
    }
    
    public float getReflectivity() {
        return this.reflectivity;
    }
    
    public float getShininess() {
        return this.shininess;
    }
    
    public int matId() {
        return 1;
    }
}
