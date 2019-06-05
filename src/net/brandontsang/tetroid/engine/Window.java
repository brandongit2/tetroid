package net.brandontsang.tetroid;

import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
    private long window;
    
    public Window(int width, int height, String title) {
        // Set window context hints.
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 0);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_SCALE_TO_MONITOR, GLFW_TRUE);
        glfwWindowHint(GLFW_TRANSPARENT_FRAMEBUFFER, GLFW_TRUE);
    
        // Create the window.
        window = glfwCreateWindow(width, height, "Hello", NULL, NULL);
        if (window == NULL) throw new RuntimeException("Failed to create the GLFW window");
    
        // Create the OpenGL context.
        glfwMakeContextCurrent(window);
        GL.createCapabilities();
        System.out.println("OpenGL version " + glGetString(GL_VERSION));
    
        glfwSetFramebufferSizeCallback(window, (long window, int w, int h) -> {
            glViewport(0, 0, w, h);
        });
    
        glfwSwapInterval(1);
    
        // Get screen dimensions and display scaling factor.
        long monitor = glfwGetPrimaryMonitor();
        float[] xscale = new float[1];
        float[] yscale = new float[1];
        glfwGetMonitorContentScale(monitor, xscale, yscale);
        GLFWVidMode vidMode      = glfwGetVideoMode(monitor);
        float       screenWidth  = vidMode.width();
        float       screenHeight = vidMode.height();
    
        // Center screen.
        int xpos = (int) (screenWidth - width * xscale[0]) / 2;
        int ypos = (int) (screenHeight- height * yscale[0]) / 2;
        glfwSetWindowPos(window, xpos, ypos);
    }
    
    public long pointer() {
        return window;
    }
}
