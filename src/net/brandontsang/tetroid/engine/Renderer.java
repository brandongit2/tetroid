package net.brandontsang.tetroid.engine;

import net.brandontsang.tetroid.engine.gui.Char;
import net.brandontsang.tetroid.engine.gui.Font;
import net.brandontsang.tetroid.engine.gui.Rectangle;
import net.brandontsang.tetroid.engine.gui.Text;
import net.brandontsang.tetroid.engine.lights.Light;
import net.brandontsang.tetroid.tetroid.Main;
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
        
        glUseProgram(scene.getShaderProgram(Main.program_3d).pointer());
        ShaderProgram program = scene.getShaderProgram(Main.program_3d);
        
        program.setUniform("ambient", scene.getAmbientLight());
        program.setUniform("cameraPos", scene.getCurrentCamera().getPosition());
    
        program.setUniform("projectionMatrix", scene.getCurrentCamera().projectionMatrix());
        program.setUniform("viewMatrix", scene.getCurrentCamera().viewMatrix());
        
        int numTextures = 0;
        for (Mesh mesh : scene.getMeshes()) {
            program.setUniform("modelMatrix", mesh.getModelMatrix());
            program.setUniform("matId", mesh.getMaterial().matId());
            program.setUniform("opacity", mesh.getMaterial().getOpacity());
            program.setUniform("reflectivity", mesh.getMaterial().getReflectivity());
            program.setUniform("shininess", mesh.getMaterial().getShininess());
            
            if (mesh.isTextured) {
                program.setUniform("isTextured", 1);
                
                glActiveTexture(GL_TEXTURE0 + numTextures);
                glBindTexture(GL_TEXTURE_2D, mesh.getTextureLoc());
                numTextures++;
            } else {
                program.setUniform("isTextured", 0);
            }
            
            glBindVertexArray(mesh.getVao());
            glDrawArrays(GL_TRIANGLES, 0, mesh.numVerts());
        }
        
        for (Line line : scene.getLines()) {
            program.setUniform("modelMatrix", line.getModelMatrix());
            program.setUniform("matId", 0);
            program.setUniform("opacity", line.getOpacity());
    
            glBindVertexArray(line.getVao());
            glDrawArrays(GL_LINES, 0, 2);
        }
        
        ArrayList<Light> lights = scene.getLights();
        for (int i = 0; i < 3; i++) {
            if (i < lights.size()) {
                Light light = lights.get(i);
                program.setUniform("lightType[" + i + "]", light.lightType());
                program.setUniform("lightColor[" + i + "]", light.getColor());
                program.setUniform("lightPos[" + i + "]", light.getPosition());
                program.setUniform("lightDir[" + i + "]", light.getDirection());
            } else {
                program.setUniform("lightType[" + i + "]", 0);
                program.setUniform("lightColor[" + i + "]", new Vector3f(0.0f, 0.0f, 0.0f));
                program.setUniform("lightPos[" + i + "]", new Vector3f(0.0f, 0.0f, 0.0f));
                program.setUniform("lightDir[" + i + "]", new Vector3f(0.0f, -1.0f, 0.0f));
            }
        }
        
        glUseProgram(scene.getShaderProgram(Main.program_gui).pointer());
        program = scene.getShaderProgram(Main.program_gui);
        
        glDisable(GL_DEPTH_TEST);
        glDisable(GL_CULL_FACE);
        program.setUniform("projectionMatrix", scene.window().guiProjectionMatrix);
        
        numTextures = 0;
        for (Rectangle rectangle : scene.getRectangles()) {
            program.setUniform("modelMatrix", rectangle.getModelMatrix());
            program.setUniform("opacity", rectangle.getOpacity());
            if (rectangle.isTextured()) {
                program.setUniform("isTextured", 1);
                program.setUniform("textureId", numTextures);
        
                glActiveTexture(GL_TEXTURE0 + numTextures);
                glBindTexture(GL_TEXTURE_2D, rectangle.getTextureLoc());
                numTextures++;
            } else {
                program.setUniform("isTextured", 0);
            }
            
            glBindVertexArray(rectangle.getVao());
            glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
        }
        
        for (Text text : scene.getTexts()) {
            program.setUniform("isTextured", 1);
            program.setUniform("textColor", text.getColor());
            program.setUniform("opacity", text.getOpacity());
            
            for (Char ch : text.getChars()) {
                program.setUniform("modelMatrix", ch.getModelMatrix());
                program.setUniform("textureId", numTextures);
                
                glActiveTexture(GL_TEXTURE0 + numTextures);
                glBindTexture(GL_TEXTURE_2D, text.getFont().textureLoc);
                numTextures++;
                
                glBindVertexArray(ch.getVao());
                glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
            }
        }
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_CULL_FACE);
        
        glBindVertexArray(0);
        
        glfwSwapBuffers(scene.window().pointer());
        glfwPollEvents();
    }
}
