package net.brandontsang.tetroid.engine;

import org.joml.Vector3f;

public interface Material {
    public Vector3f getColor();
    public int matId();
    
    default float getReflectivity() {
        return 1.0f;
    }
    
    default float getShininess() {
        return 1.0f;
    }
}
