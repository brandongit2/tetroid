package net.brandontsang.tetroid;

import java.util.ArrayList;

public class Scene {
    private ArrayList<Mesh>   meshes = new ArrayList<>();
    private ArrayList<Camera> cameras = new ArrayList<>();
    private ArrayList<Light>  lights = new ArrayList<>();
    
    public void add(Mesh mesh) {
        meshes.add(mesh);
    }
    
    public void add(Camera camera) {
        cameras.add(camera);
    }
    
    public void add(Light light) {
        lights.add(light);
    }
}
