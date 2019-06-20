package net.brandontsang.tetroid.engine;

import net.brandontsang.tetroid.engine.gui.Rectangle;
import net.brandontsang.tetroid.engine.gui.Text;
import net.brandontsang.tetroid.engine.lights.Light;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.Arrays;

public class Scene {
    private ArrayList<Mesh>          meshes         = new ArrayList<>();
    private ArrayList<Collection>    collections    = new ArrayList<>();
    private ArrayList<Line>          lines          = new ArrayList<>();
    private ArrayList<Camera>        cameras        = new ArrayList<>();
    private ArrayList<Light>         lights         = new ArrayList<>();
    private ArrayList<Rectangle>     rectangles     = new ArrayList<>();
    private ArrayList<Text>          texts          = new ArrayList<>();
    private ArrayList<ShaderProgram> shaderPrograms = new ArrayList<>();
    private ArrayList<DepthMap>      depthMaps      = new ArrayList<>();
    
    private Window        window;
    private Vector3f      ambientLight;
    
    private int currentCamera;
    
    public Scene(Window window) {
        this.window = window;
    }
    
    public int add(Mesh mesh) {
        this.meshes.add(mesh);
        mesh.isInScene = true;
        return this.meshes.size() - 1;
    }
    
    public int add(Mesh[] meshes) {
        this.meshes.addAll(Arrays.asList(meshes));
        for (Mesh mesh : meshes) {
            mesh.isInScene = true;
        }
        return this.meshes.size() - 1;
    }
    
    public int add(Line line) {
        this.lines.add(line);
        return this.lines.size() - 1;
    }
    
    public int add(Line[] lines) {
        this.lines.addAll(Arrays.asList(lines));
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
    
    public int add(Rectangle rectangle) {
        this.rectangles.add(rectangle);
        return this.rectangles.size() - 1;
    }
    
    public int add(Text text) {
        this.texts.add(text);
        return this.texts.size() - 1;
    }
    
    public int add(ShaderProgram shaderProgram) {
        this.shaderPrograms.add(shaderProgram);
        return this.shaderPrograms.size() - 1;
    }
    
    public int add(Collection collection) {
        this.collections.add(collection);
        for (Mesh mesh : collection.getMeshes()) {
            add(mesh);
        }
        return this.collections.size() - 1;
    }
    
    public void add(DepthMap depthMap) {
        this.depthMaps.add(depthMap);
    }
    
    public void remove(Mesh mesh) {
        for (int i = 0; i < this.meshes.size(); i++) {
            if (this.meshes.get(i) == mesh) this.meshes.set(i, null);
        }
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
    
    public void setAmbientLight(Vector3f color) {
        this.ambientLight = color;
    }
    
    public Vector3f getAmbientLight() {
        return this.ambientLight;
    }
    
    public ShaderProgram getShaderProgram(int i) {
        return this.shaderPrograms.get(i);
    }
    
    public ArrayList<Mesh> getMeshes() {
        return this.meshes;
    }
    
    public Mesh getMesh(int i) {
        return this.meshes.get(i);
    }
    
    public ArrayList<Line> getLines() {
        return this.lines;
    }
    
    public Line getLine(int i) {
        return this.lines.get(i);
    }
    
    public ArrayList<Light> getLights() {
        return this.lights;
    }
    
    public Light getLight(int i) {
        return this.lights.get(i);
    }
    
    public ArrayList<Rectangle> getRectangles() {
        return this.rectangles;
    }
    
    public ArrayList<Text> getTexts() {
        return this.texts;
    }
    
    public ArrayList<DepthMap> getDepthMaps() {
        return this.depthMaps;
    }
    
    public DepthMap getDepthMap(int i) {
        return this.depthMaps.get(i);
    }
}
