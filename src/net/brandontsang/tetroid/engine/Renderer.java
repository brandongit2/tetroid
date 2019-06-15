package net.brandontsang.tetroid.engine;

import org.joml.Vector3f;

import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class Renderer {
    public static void render(Scene scene) {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        
        glUseProgram(scene.getShaderProgram().pointer());
        
        scene.getShaderProgram().setUniform("ambient", scene.getAmbientLight());
        scene.getShaderProgram().setUniform("cameraPos", scene.getCurrentCamera().position());
    
        scene.getShaderProgram().setUniform("projectionMatrix", scene.getCurrentCamera().projectionMatrix());
        scene.getShaderProgram().setUniform("viewMatrix", scene.getCurrentCamera().viewMatrix());
        
        for (Mesh mesh : scene.getMeshes()) {
            scene.getShaderProgram().setUniform("modelMatrix", mesh.modelMatrix());
            scene.getShaderProgram().setUniform("matId", mesh.material().matId());
            scene.getShaderProgram().setUniform("reflectivity", mesh.material().getReflectivity());
            scene.getShaderProgram().setUniform("shininess", mesh.material().getShininess());
            
            glBindVertexArray(mesh.vao());
            glDrawArrays(GL_TRIANGLES, 0, mesh.numVerts());
        }
        
        for (Line line : scene.getLines()) {
            scene.getShaderProgram().setUniform("modelMatrix", line.getModelMatrix());
            scene.getShaderProgram().setUniform("matId", 0);
    
            glBindVertexArray(line.getVao());
            glDrawArrays(GL_LINES, 0, 2);
        }
        
        ArrayList<Light> lights = scene.getLights();
        for (int i = 0; i < 10; i++) {
            if (i < lights.size()) {
                scene.getShaderProgram().setUniform("lightColor[" + i + "]", lights.get(i).getColor());
                scene.getShaderProgram().setUniform("lightPos[" + i + "]", lights.get(i).getPosition());
            } else {
                scene.getShaderProgram().setUniform("lightColor[" + i + "]", new Vector3f(0.0f, 0.0f, 0.0f));
            }
        }
        
        glBindVertexArray(0);
        
        glfwSwapBuffers(scene.window().pointer());
        glfwPollEvents();
    }
}
