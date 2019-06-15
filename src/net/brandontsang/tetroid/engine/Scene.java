package net.brandontsang.tetroid.engine;

import net.brandontsang.tetroid.engine.lights.Light;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.Arrays;

public class Scene {
    private ArrayList<Mesh>   meshes  = new ArrayList<>();
    private ArrayList<Line>   lines   = new ArrayList<>();
    private ArrayList<Camera> cameras = new ArrayList<>();
    private ArrayList<Light>  lights  = new ArrayList<>();
    
    private Window        window;
    private ShaderProgram shaderProgram;
    private Vector3f      ambientLight;
    
    private int currentCamera;
    
    public Scene(Window window) {
        this.window        = window;
        this.shaderProgram = new ShaderProgram();
    }
    
    public int add(Mesh mesh) {
        this.meshes.add(mesh);
        return this.meshes.size() - 1;
    }
    
    public int add(Mesh[] meshes) {
        this.meshes.addAll(Arrays.asList(meshes));
        return this.meshes.size() - 1;
    }
    
    public int add(Line line) {
        this.lines.add(line);
        return this.lines.size() - 1;
    }
    
    public int add(Camera camera) {
        this.cameras.add(camera);
        return this.cameras.size() - 1;
    }
    
    public int add(Light light) {
        this.lights.add(light);
        return this.lights.size() - 1;
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
    
    public void setAmbientLight(Vector3f color) {
        this.ambientLight = color;
    }
    
    public Vector3f getAmbientLight() {
        return this.ambientLight;
    }
    
    public ShaderProgram getShaderProgram() {
        return this.shaderProgram;
    }
    
    public ArrayList<Mesh> getMeshes() {
        return this.meshes;
    }
    
    public ArrayList<Line> getLines() {
        return this.lines;
    }
    
    public ArrayList<Light> getLights() {
        return this.lights;
    }
    
    public Light getLight(int i) {
        return this.lights.get(i);
    }
}
