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
    
    public Block(int type, Vector3i pos) {
        this.pos = pos;
        
        Vector3f color;
        switch (type) {
            case 0:
                color = new Vector3f(1.0f, 1.0f, 0.0f);
                break;
            case 1:
                color = new Vector3f(0.0f, 1.0f, 1.0f);
                break;
            case 2:
                color = new Vector3f(1.0f, 0.0f, 1.0f);
                break;
            case 3:
                color = new Vector3f(0.0f, 0.0f, 1.0f);
                break;
            case 4:
                color = new Vector3f(0.0f, 1.0f, 0.0f);
                break;
            case 5:
                color = new Vector3f(0.2f, 0.2f, 0.2f);
                break;
            case 6:
                color = new Vector3f(0.6f, 0.6f, 0.6f);
                break;
            case 7:
                color = new Vector3f(0.5f, 0.2f, 0.2f);
                break;
            default:
                color = new Vector3f(1.0f, 1.0f, 1.0f);
                break;
        }
        
        try {
            this.mesh = Mesh.fromFile("./res/models/block.obj", new PhongMaterial(color, 0.5f, 1000.0f)).translate(pos.x, pos.y, pos.z);
        } catch (IOException err) {
            err.printStackTrace();
            System.exit(1);
        }
    }
    
    void setPos(Vector3i pos) {
        this.pos = pos;
        this.mesh.setTranslation(pos.x, pos.y, pos.z);
    }
    
    public Mesh getMesh() {
        return this.mesh;
    }
}
