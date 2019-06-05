package net.brandontsang.tetroid.engine;

import java.util.ArrayList;

public class MeshGroup {
    private ArrayList<Mesh> meshes;
    
    public MeshGroup() {
        this(new ArrayList<Mesh>());
    }
    
    public MeshGroup(ArrayList<Mesh> meshes) {
        this.meshes = meshes;
    }
}
