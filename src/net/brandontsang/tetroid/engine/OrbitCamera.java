package net.brandontsang.tetroid.engine;

import org.joml.Vector3f;

// A camera which orbits around a point. Does not support roll.
public class OrbitCamera extends Camera {
    private Vector3f center;
    private float x = 0.0f; // Pitch
    private float y = 0.0f; // Yaw
    private float radius;
    private float zoom = 1;
    
    public OrbitCamera(float cx, float cy, float cz, float radius, float fov, float zNear, float zFar, Window window) {
        // Camera begins at (0, 0, radius) relative to the center.
        super(-cx, -cy, -cz - radius, fov, zNear, zFar, window);
        
        this.center = new Vector3f(cx, cy, cz);
        this.radius = radius;
    }
    
    // `rx` represents change in pitch in degrees
    // `ry` represents change in yaw in degrees
    public void rotate(float rx, float ry) {
        this.x += rx;
        this.y += ry;
        if (this.x > 90.1f) this.x = 90.0f;
        if (this.x < -90.1f) this.x = -90.0f;
        
        this.position
            .set(0.0f, 0.0f, -radius)
            .rotateAxis((float) Math.toRadians(this.x), 1.0f, 0.0f, 0.0f)
            .rotateAxis((float) Math.toRadians(this.y), 0.0f, 1.0f, 0.0f)
            .mul(this.zoom)
            .sub(this.center)
            .add(new Vector3f(0.0f, this.x / 10.0f, 0.0f));
        
        this.orientation.set((float) Math.toRadians(this.x), (float) Math.toRadians(this.y), 0.0f).mul(-1);
        
        transform();
    }
    
    public void zoom(float factor) {
        float nextZoom = (float) Math.pow(2.0, Math.log(this.zoom) / Math.log(2.0)) + factor;
        this.zoom = nextZoom < 2.0f ? 2.0f : nextZoom;
    
        this.position
            .set(0.0f, 0.0f, -radius)
            .rotateAxis((float) Math.toRadians(this.x), 1.0f, 0.0f, 0.0f)
            .rotateAxis((float) Math.toRadians(this.y), 0.0f, 1.0f, 0.0f)
            .mul(this.zoom)
            .sub(this.center)
            .add(new Vector3f(0.0f, this.x / 10.0f, 0.0f));
        
        transform();
    }
}
