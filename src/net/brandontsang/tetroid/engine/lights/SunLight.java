package net.brandontsang.tetroid.engine.lights;

import net.brandontsang.tetroid.engine.DepthMap;
import net.brandontsang.tetroid.engine.Scene;
import org.joml.Vector3f;

public class SunLight implements Light {
    private Vector3f color;
    private Vector3f direction;
    
    private DepthMap depthMap;
    
    public int lightType() {
        return 1;
    }
    
    public Vector3f getColor() {
        return this.color;
    }
    
    public SunLight(Vector3f color, Vector3f direction, Scene scene) {
        this.color = color;
        this.direction = direction;
        
        this.depthMap = new DepthMap(this, scene);
        scene.add(this.depthMap);
    }
    
    @Override
    public Vector3f getDirection() {
        return this.direction;
    }
}
