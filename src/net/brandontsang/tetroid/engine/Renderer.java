package net.brandontsang.tetroid.engine;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL32.*;

public class Renderer {
    public static void render(Scene scene) {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glProvokingVertex(GL_FIRST_VERTEX_CONVENTION);
        
        glUseProgram(scene.getShaderProgram().pointer());
        
        scene.getShaderProgram().setUniform("projectionMatrix", scene.getCurrentCamera().projectionMatrix());
        scene.getShaderProgram().setUniform("viewMatrix", scene.getCurrentCamera().viewMatrix());
        
        for (Mesh mesh : scene.getMeshes()) {
            scene.getShaderProgram().setUniform("modelMatrix", mesh.modelMatrix());
            
            glBindVertexArray(mesh.vao());
            glDrawArrays(GL_TRIANGLES, 0, mesh.numVerts());
        }
        
        glBindVertexArray(0);
        
        glfwSwapBuffers(scene.window().pointer());
        glfwPollEvents();
    }
}
