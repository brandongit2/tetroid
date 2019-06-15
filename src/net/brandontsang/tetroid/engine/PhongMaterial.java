package net.brandontsang.tetroid.engine;

import org.joml.Vector3f;

public class PhongMaterial implements Material {
    private Vector3f color;
    private float reflectivity;
    private float shininess;
    
    public PhongMaterial(Vector3f color, float reflectivity, float shininess) {
        this.color = color;
        this.reflectivity = reflectivity;
        this.shininess = shininess;
    }
    
    public Vector3f getColor() {
        return this.color;
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
