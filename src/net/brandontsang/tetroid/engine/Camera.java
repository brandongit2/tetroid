package net.brandontsang.tetroid.engine;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera {
    protected Vector3f position    = new Vector3f(0.0f, 0.0f, 0.0f);
    protected Vector3f orientation = new Vector3f(0.0f, 0.0f, 0.0f);
    private   float    fov;
    
    private Matrix4f projectionMatrix = new Matrix4f();
    private Matrix4f viewMatrix       = new Matrix4f();
    
    public Camera(float x, float y, float z, float fov, float zNear, float zFar, Window window) {
        this.position.set(x, y, z);
        this.fov = fov;
        this.projectionMatrix.perspective(fov, ((float) window.width()) / window.height(), zNear, zFar);
        
        transform();
    }
    
    public void setPosition(float x, float y, float z) {
        this.position.set(x, y, z);
        transform();
    }
    
    public void setOrientation(float x, float y, float z) {
        this.orientation.set(x, y, z);
        transform();
    }
    
    // Apply all transformations in the right order.
    protected void transform() {
        viewMatrix.identity();
        viewMatrix.rotate(this.orientation.z, new Vector3f(0f, 0f, 1f));
        viewMatrix.rotate(this.orientation.x, new Vector3f(1f, 0f, 0f));
        viewMatrix.rotate(this.orientation.y, new Vector3f(0f, 1f, 0f));
        
        viewMatrix.translate(position);
    }
    
    public void setFov(float fov) {
        this.fov = fov;
    }
    
    public float fov() {
        return this.fov;
    }
    
    public Vector3f position() {
        return this.position;
    }
    
    public Matrix4f projectionMatrix() {
        return this.projectionMatrix;
    }
    
    public Matrix4f viewMatrix() {
        return this.viewMatrix;
    }
}
