package net.brandontsang.tetroid.engine.gui;

import org.joml.Vector3f;

import java.util.ArrayList;

public class Text {
    private ArrayList<Char> aChars = new ArrayList<>();
    private Font            font;
    
    private Vector3f color   = new Vector3f(1.0f, 1.0f, 1.0f);
    private float    opacity = 1.0f;
    
    private float width = 0;
    
    public Text(Font font) {
        this.font = font;
    }
    
    public void addCharacter(Char aChar) {
        this.aChars.add(aChar);
        
        this.width = (aChars.get(aChars.size() - 1).x() - aChars.get(aChars.size() - 1).width()) - aChars.get(0).x();
    }
    
    public Text translate(float dx, float dy) {
        for (Char ch : this.aChars) {
            ch.translate(dx, dy);
        }
        return this;
    }
    
    public Text setText(String text) {
        this.aChars.clear();
        this.font.renderText(text, this);
        return this;
    }
    
    public Text setColor(Vector3f color) {
        this.color = color;
        return this;
    }
    
    public ArrayList<Char> getChars() {
        return this.aChars;
    }
    
    public Font getFont() {
        return this.font;
    }
    
    public Vector3f getColor() {
        return this.color;
    }
    
    public float getOpacity() {
        return this.opacity;
    }
    
    public float getWidth() {
        return this.width;
    }
}
