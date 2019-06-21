package net.brandontsang.tetroid.tetroid;

import net.brandontsang.tetroid.engine.Mesh;
import net.brandontsang.tetroid.engine.materials.PhongMaterial;
import org.joml.Vector3f;
import org.joml.Vector3i;

import java.io.IOException;

/* Block types:
 * 0 - O
 * 1 - I
 * 2 - T
 * 3 - L
 * 4 - S
 * 5 - Right screw
 * 6 - Left screw
 * 7 - Branch
 */

public class Block {
    Mesh     mesh;
    Vector3i pos;
    float    opacity = 1.0f;
    
    public Block(Tetromino color, Vector3i pos) {
        this(color.getColor(), pos);
    }
    
    public Block(Vector3f color, Vector3i pos) {
        this.pos = pos;
        
        try {
            this.mesh = Mesh.fromFile("/res/models/block.obj", new PhongMaterial(color, 0.8f, 1000.0f)).translate(pos.x, pos.y, pos.z);
        } catch (IOException err) {
            err.printStackTrace();
            System.exit(1);
        }
    }
    
    void setPos(Vector3i pos) {
        this.pos = pos;
        this.mesh.setTranslation(pos.x, pos.y, pos.z);
    }
    
    Vector3i getPos() {
        return this.pos;
    }
    
    public Block setColor(Vector3f color) {
        this.mesh.getMaterial().setColor(color);
        return this;
    }
    
    public Block setOpacity(float opacity) {
        this.opacity = opacity;
        this.mesh.getMaterial().setOpacity(opacity);
        return this;
    }
    
    public Mesh getMesh() {
        return this.mesh;
    }
}
