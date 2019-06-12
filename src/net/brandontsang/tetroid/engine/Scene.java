package net.brandontsang.tetroid.engine;

import java.util.ArrayList;

public class Scene {
    private ArrayList<Mesh>   meshes  = new ArrayList<>();
    private ArrayList<Camera> cameras = new ArrayList<>();
    private ArrayList<Light>  lights  = new ArrayList<>();
    
    private Window        window;
    private ShaderProgram shaderProgram;
    
    private int currentCamera;
    
    public Scene(Window window) {
        this.window        = window;
        this.shaderProgram = new ShaderProgram();
    }
    
    public int add(Mesh mesh) {
        this.meshes.add(mesh);
        return this.meshes.size();
    }
    
    public int add(Camera camera) {
        this.cameras.add(camera);
        return this.cameras.size() - 1;
    }
    
    public int add(Light light) {
        this.lights.add(light);
        return this.lights.size();
    }
    
    public void setCurrentCamera(int i) {
        this.currentCamera = i;
    }
    
    public Camera getCurrentCamera() {
        return this.cameras.get(this.currentCamera);
    }
    
    public Window window() {
        return window;
    }
    
    public void setShaderProgram(ShaderProgram program) {
        this.shaderProgram = program;
    }
    
    public ShaderProgram getShaderProgram() {
        return this.shaderProgram;
    }
    
    public ArrayList<Mesh> getMeshes() {
        return this.meshes;
    }
    
    public Mesh getMesh(int i) {
        return this.meshes.get(i);
    }
}
