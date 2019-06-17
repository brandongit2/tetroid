package net.brandontsang.tetroid.engine;

import net.brandontsang.tetroid.engine.materials.Material;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL30.*;

public class Mesh {
    private int vao;
    private int numVerts;
    private int numTexCoords;
    private int numVertNorms;
    
    public boolean isInScene = false;
    private Material material;
    
    private Matrix4f modelMatrix = new Matrix4f();
    
    public Mesh(float[] vertices, float[] texCoords, float[] vertNorms, Material material) {
        this.numVerts     = vertices.length;
        this.numTexCoords = texCoords.length;
        this.numVertNorms = vertNorms.length;
        
        this.material = material;
        
        this.vao = glGenVertexArrays();
        glBindVertexArray(vao);
    
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(this.numVerts);
        vertexBuffer.put(vertices).flip();
        int vertexVboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vertexVboId);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(0);
        
        FloatBuffer texCoordBuffer = BufferUtils.createFloatBuffer(this.numTexCoords);
        texCoordBuffer.put(texCoords).flip();
        int texCoordVboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, texCoordVboId);
        glBufferData(GL_ARRAY_BUFFER, texCoordBuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(1);
        
        FloatBuffer vertNormBuffer = BufferUtils.createFloatBuffer(this.numVertNorms);
        vertNormBuffer.put(vertNorms).flip();
        int vertNormVboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vertNormVboId);
        glBufferData(GL_ARRAY_BUFFER, vertNormBuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(2, 3, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(2);
        
        FloatBuffer colorBuffer = BufferUtils.createFloatBuffer(this.numVerts * 3);
        // Fill `colorBuffer` with the same color.
        for (int i = 0; i < this.numVerts * 3; i += 3) {
            material.getColor().get(i, colorBuffer);
        }
        int colorVboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, colorVboId);
        glBufferData(GL_ARRAY_BUFFER, colorBuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(3, 3, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(3);
    
        glBindVertexArray(0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }
    
    public static Mesh fromFile(String fileLocation, Material material) throws IOException {
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
        
        // OpenGL only supports one index per VAO. Since OBJs store three separate indices
        // (one for each of vertices, texture coordinates, and vertex normals), it is
        // necessary to re-index the texture coordinates and vertex normals. The following
        // variables are indexed by the indices specified in the OBJ file. They will be
        // organized later by their indices.
        float[] verticesIndexed  = new float[numVerts * 3];
        float[] texCoordsIndexed = new float[numTexCoords * 2];
        float[] vertNormsIndexed = new float[numVertNorms * 3];
        
        float[] vertices  = new float[numFaces * 9];
        float[] texCoords = new float[numFaces * 6];
        float[] vertNorms = new float[numFaces * 9];
    
        int currentIndexedVertex   = 0;
        int currentIndexedTexCoord = 0;
        int currentIndexedVertNorm = 0;
        
        int currentVertex = 0;
        int currentTexCoord = 0;
        int currentVertNorm = 0;
    
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
                                verticesIndexed[currentIndexedVertex * 3 + i] = Float.parseFloat(_vertex[i].toString());
                                i++;
                            } else {
                                _vertex[i].append(ch);
                            }
                        }
                        verticesIndexed[currentIndexedVertex * 3 + i] = Float.parseFloat(_vertex[i].toString());
                        currentIndexedVertex++;
                        break;
                    }
                    case "vt": {
                        StringBuilder[] _vt = new StringBuilder[3];
                        int i = 0;
                        while ((ch = (char) buf.read()) != '\n') {
                            if (_vt[i] == null) {
                                _vt[i] = new StringBuilder(Character.toString(ch));
                            } else if (ch == ' ') {
                                texCoordsIndexed[currentIndexedTexCoord * 2 + i] = Float.parseFloat(_vt[i].toString());
                                i++;
                            } else {
                                _vt[i].append(ch);
                            }
                        }
                        texCoordsIndexed[currentIndexedTexCoord * 2 + i] = Float.parseFloat(_vt[i].toString());
                        currentIndexedTexCoord++;
                        break;
                    }
                    case "vn": {
                        StringBuilder[] _vn = new StringBuilder[3];
                        int i = 0;
                        while ((ch = (char) buf.read()) != '\n') {
                            if (_vn[i] == null) {
                                _vn[i] = new StringBuilder(Character.toString(ch));
                            } else if (ch == ' ') {
                                vertNormsIndexed[currentIndexedVertNorm * 3 + i] = Float.parseFloat(_vn[i].toString());
                                i++;
                            } else {
                                _vn[i].append(ch);
                            }
                        }
                        vertNormsIndexed[currentIndexedVertNorm * 3 + i] = Float.parseFloat(_vn[i].toString());
                        currentIndexedVertNorm++;
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
                        
                        while (true) {
                            ch = (char) buf.read();
                            
                            if (ch == '/' || ch == ' ' || ch == '\n') {
                                switch (j) {
                                    case 0: { // Vertex index
                                        int vertexIndex = Integer.parseInt(_face[i * 3].toString()) - 1;
                                        vertices[currentVertex] = verticesIndexed[vertexIndex * 3];
                                        vertices[currentVertex + 1] = verticesIndexed[vertexIndex * 3 + 1];
                                        vertices[currentVertex + 2] = verticesIndexed[vertexIndex * 3 + 2];
                                        currentVertex += 3;
                                        j++;
                                        break;
                                    }
                                    case 1: { // Texture coordinate index
                                        int texCoordIndex = Integer.parseInt(_face[i * 3 + 1].toString()) - 1;
                                        texCoords[currentTexCoord] = texCoordsIndexed[texCoordIndex * 2];
                                        texCoords[currentTexCoord + 1] = texCoordsIndexed[texCoordIndex * 2 + 1];
                                        currentTexCoord += 2;
                                        j++;
                                        break;
                                    }
                                    case 2: { // Vertex normal index
                                        int vertNormIndex = Integer.parseInt(_face[i * 3 + 2].toString()) - 1;
                                        vertNorms[currentVertNorm] = vertNormsIndexed[vertNormIndex * 3];
                                        vertNorms[currentVertNorm + 1] = vertNormsIndexed[vertNormIndex * 3 + 1];
                                        vertNorms[currentVertNorm + 2] = vertNormsIndexed[vertNormIndex * 3 + 2];
                                        currentVertNorm += 3;
                                        i++;
                                        j = 0;
                                        break;
                                    }
                                }
                            } else {
                                _face[i * 3 + j].append(ch);
                            }
                            
                            if (ch == '\n') break;
                        }
                        break;
                    }
                    default: {
                        buf.readLine();
                        break;
                    }
                }
            }
        }
        
        return new Mesh(vertices, texCoords, vertNorms, material);
    }
    
    public int getVao() {
        return this.vao;
    }
    
    public int numVerts() {
        return this.numVerts;
    }
    
    public Matrix4f getModelMatrix() {
        return this.modelMatrix;
    }
    
    public Material getMaterial() {
        return this.material;
    }
    
    public void setMaterial(Material material) {
        this.material = material;
    }
    
    // Angle in degrees.
    public Mesh rotate(float rx, float ry, float rz) {
        this.modelMatrix.rotate((float) Math.toRadians(rz), 0, 0, 1);
        this.modelMatrix.rotate((float) Math.toRadians(rx), 1, 0, 0);
        this.modelMatrix.rotate((float) Math.toRadians(ry), 0, 1, 0);
        return this;
    }
    
    public Mesh translate(float dx, float dy, float dz) {
        this.modelMatrix.translate(dx, dy, dz);
        return this;
    }
    
    public Mesh scale(float factor) {
        this.modelMatrix.scale(factor);
        return this;
    }
    
    public Mesh setTranslation(float x, float y, float z) {
        this.modelMatrix.setTranslation(x, y, z);
        return this;
    }
}
