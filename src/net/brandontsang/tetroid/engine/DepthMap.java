package net.brandontsang.tetroid.engine;

import net.brandontsang.tetroid.engine.lights.Light;
import net.brandontsang.tetroid.engine.lights.PointLight;
import net.brandontsang.tetroid.engine.lights.SunLight;
import net.brandontsang.tetroid.tetroid.Main;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class DepthMap {
    private int fbo;
    private int depthMap;
    
    private Scene    scene;
    private Light    light;
    private int      lightType;
    private Matrix4f lightProjection = new Matrix4f();
    private Matrix4f lightView       = new Matrix4f();
    
    private static final int   SHADOW_WIDTH  = 4096;
    private static final int   SHADOW_HEIGHT = 4096;
    private static final float MAP_WIDTH     = 20.0f;
    private static final float MAP_HEIGHT    = 20.0f;
    
    public DepthMap(PointLight light, Scene scene) {
        this(light, 0, scene);
    }
    
    public DepthMap(SunLight light, Scene scene) {
        this(light, 1, scene);
    }
    
    private DepthMap(Light light, int lightType, Scene scene) {
        this.scene     = scene;
        this.light     = light;
        this.lightType = lightType;
    
        Vector3f cameraPos = scene.getCurrentCamera().getPosition();
        this.lightProjection.ortho(
            -50.0f, 50.0f, -50.0f, 50.0f,
            -100.0f, 100.0f);
        Vector3f lightDir = new Vector3f(light.getDirection());
        lightDir.mul(-1);
        this.lightView.lookAt(
            lightDir,
            new Vector3f(),
            new Vector3f(0.0f, 1.0f, 0.0f)
        );
        
        this.fbo = glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER, this.fbo);
        
        this.depthMap = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, this.depthMap);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT, SHADOW_WIDTH, SHADOW_HEIGHT, 0, GL_DEPTH_COMPONENT, GL_FLOAT, NULL);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_BORDER);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_BORDER);
        float borderColor[] = { 1.0f, 1.0f, 1.0f, 1.0f };
        glTexParameterfv(GL_TEXTURE_2D, GL_TEXTURE_BORDER_COLOR, borderColor);
    
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_TEXTURE_2D, this.depthMap, 0);
        glDrawBuffer(GL_NONE);
        glReadBuffer(GL_NONE);
    
        if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
            System.err.println("Framebuffer is not complete!");
        }
        
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }
    
    // Center the orthographic projection matrix on the camera position.
    // Unused since the only shadows in the scene are at the origin.
    private void calulateLightView() {
        Vector3f cameraPos = new Vector3f(this.scene.getCurrentCamera().position);
        Vector3f lightDir = new Vector3f(light.getDirection());

        cameraPos.y = 0.0f;
        cameraPos.mul(-1);

        lightDir.mul(-1);
        lightDir.add(cameraPos);

        this.lightView.identity();
        this.lightView.lookAt(
            lightDir,
            cameraPos,
            new Vector3f(0.0f, 1.0f, 0.0f)
        );
    }
    
    public void renderDepthMap() {
        glViewport(0, 0, SHADOW_WIDTH, SHADOW_HEIGHT);
        glBindFramebuffer(GL_FRAMEBUFFER, this.fbo);
        glClear(GL_DEPTH_BUFFER_BIT);
        
        glUseProgram(Main.program_depthMap);
        ShaderProgram program_depthMap = this.scene.getShaderProgram(Main.program_depthMap);
        program_depthMap.setUniform("lightProjection", this.lightProjection);
        program_depthMap.setUniform("lightView", this.lightView);
        
        for (Mesh mesh : this.scene.getMeshes()) {
            if (mesh == null) continue;
            
            program_depthMap.setUniform("modelMatrix", mesh.getModelMatrix());
            
            glBindVertexArray(mesh.getVao());
            glDrawArrays(GL_TRIANGLES, 0, mesh.numVerts());
        }
        
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }
    
    public int getDepthMap() {
        return this.depthMap;
    }
    
    public Matrix4f getLightSpaceMatrix() {
        return (new Matrix4f(this.lightProjection)).mul(this.lightView);
    }
}
