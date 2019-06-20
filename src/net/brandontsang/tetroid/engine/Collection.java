package net.brandontsang.tetroid.engine;

import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.Arrays;

// Represents a collection of meshes (useful for bulk transformations).
public class Collection {
    private ArrayList<Mesh> meshes = new ArrayList<>();
    private Vector3f        origin = new Vector3f();
    
    public Collection(Mesh[] meshes) {
        this.meshes.addAll(Arrays.asList(meshes));
    }
    
    public Collection translate(float dx, float dy, float dz) {
        this.origin.add(dx, dy, dz);
        for (int i = 0; i < this.meshes.size(); i++) {
            this.meshes.get(i).translate(dx, dy, dz);
        }
        return this;
    }
    
    public Collection rotate(float rx, float ry, float rz) {
        for (int i = 0; i < this.meshes.size(); i++) {
            Mesh mesh = this.meshes.get(i);
            mesh.translate(-this.origin.x, -this.origin.y, -this.origin.z);
            Vector3f originalPos = new Vector3f(mesh.getPosition());
            originalPos.rotateAxis((float) Math.toRadians(rz), 0, 0, 1);
            originalPos.rotateAxis((float) Math.toRadians(rx), 1, 0, 0);
            originalPos.rotateAxis((float) Math.toRadians(ry), 0, 1, 0);
    
            mesh.translate(-mesh.getPosition().x, -mesh.getPosition().y, -mesh.getPosition().z);
            mesh.translate(originalPos.x, originalPos.y, originalPos.z);
            mesh.translate(this.origin.x, this.origin.y, this.origin.z);
    
            mesh.rotate(rx, ry, rz);
        }
        return this;
    }
    
    public Collection scale(float factor) {
        for (Mesh mesh : this.meshes) {
            Vector3f pos = new Vector3f(mesh.getPosition());
            Vector3f distanceToOrigin = new Vector3f();
            origin.sub(pos, distanceToOrigin);
            mesh.translate(distanceToOrigin.x, distanceToOrigin.y, distanceToOrigin.z);
            pos.sub(origin);
            pos.mul(factor);
            mesh.translate(pos.x, pos.y, pos.z);
            
            mesh.scale(factor);
        }
        return this;
    }
    
    public ArrayList<Mesh> getMeshes() {
        return this.meshes;
    }
}
