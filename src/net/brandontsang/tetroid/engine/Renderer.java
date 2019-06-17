package net.brandontsang.tetroid.engine;

import net.brandontsang.tetroid.engine.lights.Light;
import org.joml.Vector3f;

import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class Renderer {
    public static void render(Scene scene) {
        glClearColor(0.1f, 0.1f, 0.1f, 1.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        
        glUseProgram(scene.getShaderProgram().pointer());
        
        scene.getShaderProgram().setUniform("ambient", scene.getAmbientLight());
        scene.getShaderProgram().setUniform("cameraPos", scene.getCurrentCamera().getPosition());
    
        scene.getShaderProgram().setUniform("projectionMatrix", scene.getCurrentCamera().projectionMatrix());
        scene.getShaderProgram().setUniform("viewMatrix", scene.getCurrentCamera().viewMatrix());
        
        for (Mesh mesh : scene.getMeshes()) {
            scene.getShaderProgram().setUniform("modelMatrix", mesh.getModelMatrix());
            scene.getShaderProgram().setUniform("matId", mesh.getMaterial().matId());
            scene.getShaderProgram().setUniform("opacity", mesh.getMaterial().getOpacity());
            scene.getShaderProgram().setUniform("reflectivity", mesh.getMaterial().getReflectivity());
            scene.getShaderProgram().setUniform("shininess", mesh.getMaterial().getShininess());
            
            if (mesh.isTextured) {
                scene.getShaderProgram().setUniform("isTextured", 1);
                glActiveTexture(GL_TEXTURE0);
                glBindTexture(GL_TEXTURE_2D, mesh.getTextureLoc());
            } else {
                scene.getShaderProgram().setUniform("isTextured", 0);
            }
            
            glBindVertexArray(mesh.getVao());
            glDrawArrays(GL_TRIANGLES, 0, mesh.numVerts());
        }
        
        for (Line line : scene.getLines()) {
            scene.getShaderProgram().setUniform("modelMatrix", line.getModelMatrix());
            scene.getShaderProgram().setUniform("matId", 0);
            scene.getShaderProgram().setUniform("opacity", line.getOpacity());
    
            glBindVertexArray(line.getVao());
            glDrawArrays(GL_LINES, 0, 2);
        }
        
        ArrayList<Light> lights = scene.getLights();
        for (int i = 0; i < 3; i++) {
            if (i < lights.size()) {
                Light light = lights.get(i);
                scene.getShaderProgram().setUniform("lightType[" + i + "]", light.lightType());
                scene.getShaderProgram().setUniform("lightColor[" + i + "]", light.getColor());
                scene.getShaderProgram().setUniform("lightPos[" + i + "]", light.getPosition());
                scene.getShaderProgram().setUniform("lightDir[" + i + "]", light.getDirection());
            } else {
                scene.getShaderProgram().setUniform("lightType[" + i + "]", 0);
                scene.getShaderProgram().setUniform("lightColor[" + i + "]", new Vector3f(0.0f, 0.0f, 0.0f));
                scene.getShaderProgram().setUniform("lightPos[" + i + "]", new Vector3f(0.0f, 0.0f, 0.0f));
                scene.getShaderProgram().setUniform("lightDir[" + i + "]", new Vector3f(0.0f, -1.0f, 0.0f));
            }
        }
        
        glBindVertexArray(0);
        
        glfwSwapBuffers(scene.window().pointer());
        glfwPollEvents();
    }
}
