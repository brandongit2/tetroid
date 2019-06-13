package net.brandontsang.tetroid.engine;

// A camera which orbits around a point. Does not support roll.
public class OrbitCamera extends Camera {
    private float x = 0.0f; // Pitch
    private float y = 0.0f; // Yaw
    private float radius;
    
    public OrbitCamera(float cx, float cy, float cz, float radius, float fov, float zNear, float zFar, Window window) {
        // Camera begins at (0, 0, radius) relative to the center.
        super(cx, cy, cz - radius, fov, zNear, zFar, window);
        
        this.radius = radius;
    }
    
    // `rx` represents change in pitch in degrees
    // `ry` represents change in yaw in degrees
    public void rotate(float rx, float ry) {
        this.x -= rx;
        this.y -= ry;
        
        this.position
            .set(0.0f, 0.0f, -this.radius)
            .rotateAxis((float) Math.toRadians(this.x), 1.0f, 0.0f, 0.0f)
            .rotateAxis((float) Math.toRadians(this.y), 0.0f, 1.0f, 0.0f);
        
        this.orientation.set((float) Math.toRadians(-this.x), (float) Math.toRadians(-this.y), 0.0f);
        
        transform();
    }
}
