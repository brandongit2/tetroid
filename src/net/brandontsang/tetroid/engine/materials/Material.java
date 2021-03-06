package net.brandontsang.tetroid.engine.materials;

import org.joml.Vector3f;

public interface Material {
    Vector3f getColor();
    float getOpacity();
    void setOpacity(float opacity);
    int matId();
    Material setColor(Vector3f color);
    
    default float getReflectivity() {
        return 1.0f;
    }
    
    default float getShininess() {
        return 1.0f;
    }
}
