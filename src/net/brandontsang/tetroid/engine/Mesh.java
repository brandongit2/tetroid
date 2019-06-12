package net.brandontsang.tetroid.engine;

import org.joml.Matrix4f;
import org.lwjgl.system.MemoryStack;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL30.*;

public class Mesh extends GameObject {
    private int vao;
    private int numVerts;
    private int numIndices;
    
    private Matrix4f modelMatrix;
    
    public Mesh(float[] vertices, int[] indices) {
        this.numVerts   = vertices.length;
        this.numIndices = indices.length;
        
        System.out.println(Arrays.toString(vertices));
        System.out.println(Arrays.toString(indices));
        
        this.vao = glGenVertexArrays();
        glBindVertexArray(vao);
    
        FloatBuffer vertexBuffer;
        IntBuffer   indexBuffer;
        try (MemoryStack stack = MemoryStack.stackPush()) {
            vertexBuffer = stack.callocFloat(this.numVerts);
            vertexBuffer.put(vertices).flip();
            
            indexBuffer = stack.callocInt(this.numIndices);
            indexBuffer.put(indices).flip();
        }
    
        int vertexVboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vertexVboId);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(0);
        
        int indexVboId = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexVboId);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexBuffer, GL_STATIC_DRAW);
    
        glBindVertexArray(0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        
        this.modelMatrix = new Matrix4f();
    }
    
    public static Mesh fromFile(String fileLocation) throws IOException {
        BufferedReader buf = new BufferedReader(new FileReader(fileLocation));
    
        int numVerts     = 0;
        int numTexCoords = 0;
        int numVertNorms = 0;
        int numFaces     = 0;
    
        char ch;
        while ((ch = (char) buf.read()) != '\n') {
            if (ch == 0xFFFF) break;
        
            StringBuilder cmd = new StringBuilder(Character.toString(ch));
        
            while ((ch = (char) buf.read()) != ' ') {
                cmd.append(ch);
            }
        
            switch (cmd.toString()) {
                case "v": {
                    numVerts++;
                    break;
                }
                case "vn": {
                    numVertNorms++;
                    break;
                }
                case "vt": {
                    numTexCoords++;
                    break;
                }
                case "f": {
                    numFaces++;
                    break;
                }
            }
        
            buf.readLine();
        }
        buf.close();
    
        // Parse OBJ files.
        buf = new BufferedReader(new FileReader(fileLocation));
    
        float[] vertices  = new float[numVerts * 3];
        float[] texCoords = new float[numTexCoords];
        float[] vertNorms = new float[numVertNorms];
        int[]   faces     = new int[numFaces * 3];
    
        int currentVertex = 0;
        int currentFace   = 0;
    
        while ((ch = (char) buf.read()) != 0xFFFF) {
            if (ch == '#') {
                buf.readLine();
            } else {
                StringBuilder _cmd = new StringBuilder(String.valueOf(ch));
                while ((ch = (char) buf.read()) != ' ') {
                    _cmd.append(ch);
                }
            
                String cmd = _cmd.toString();
                switch (cmd) {
                    case "v": {
                        StringBuilder[] _vertex = new StringBuilder[3];
                        int i = 0;
                        while ((ch = (char) buf.read()) != '\n') {
                            if (_vertex[i] == null) {
                                _vertex[i] = new StringBuilder(Character.toString(ch));
                            } else if (ch == ' ') {
                                vertices[currentVertex * 3 + i] = Float.parseFloat(_vertex[i].toString());
                                i++;
                            } else {
                                _vertex[i].append(ch);
                            }
                        }
                        vertices[currentVertex * 3 + i] = Float.parseFloat(_vertex[i].toString());
                        currentVertex++;
                        break;
                    }
                    case "f": {
                        StringBuilder[] _face = new StringBuilder[9];
                        for (int i = 0; i < 9; i++) {
                            _face[i] = new StringBuilder();
                        }
                        
                        int i = 0; // The current vertex of the face.
                        
                        /* OBJ specifies, for each vertex:
                         * 1. The vertex's index
                         * 2. The UV coordinate of the face
                         * 3. The normal index of that vertex
                         *
                         * `j` represents the data represented by the current character.
                         */
                        int j = 0;
                        
                        while ((ch = (char) buf.read()) != '\n') {
                            if (ch == '/' || ch == ' ') {
                                switch (j) {
                                    case 0: {
                                        faces[currentFace * 3 + i] = Integer.parseInt(_face[i * 3].toString()) - 1;
                                        j++;
                                        break;
                                    }
                                    case 1: {
                                        j++;
                                        break;
                                    }
                                    case 2: {
                                        i++;
                                        j = 0;
                                    }
                                }
                            } else {
                                _face[i * 3 + j].append(ch);
                            }
                        }
                        currentFace++;
                        break;
                    }
                    default: {
                        buf.readLine();
                        break;
                    }
                }
            }
        }
        
        return new Mesh(vertices, faces);
    }
    
    public int vao() {
        return this.vao;
    }
    
    public int numVerts() {
        return this.numVerts;
    }
    
    public int numIndices() {
        return this.numIndices;
    }
    
    public Matrix4f modelMatrix() {
        return this.modelMatrix;
    }
    
    // Angle in degrees.
    public Matrix4f rotateY(float angle) {
        this.modelMatrix.rotate((float) Math.toRadians(angle), 0, 1, 0);
        return this.modelMatrix;
    }
    
    public Matrix4f translate(float dx, float dy, float dz) {
        this.modelMatrix.translate(dx, dy, dz);
        return this.modelMatrix;
    }
}
