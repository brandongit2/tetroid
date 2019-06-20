package net.brandontsang.tetroid.tetroid;

import net.brandontsang.tetroid.engine.Scene;
import net.brandontsang.tetroid.engine.gui.Font;
import net.brandontsang.tetroid.engine.gui.Text;

import java.util.HashMap;

class Gui {
    private Scene scene;
    private Font  openSans;
    
    private RenderLoopAttatchment a;
    
    Gui(Scene scene) {
        this.scene = scene;
        this.openSans = new Font("/res/fonts/OpenSans-Regular.ttf", 36, scene);
        
        Text text = this.openSans.renderText("hello there");
        this.scene.add(text);
        
        this.a = new RenderLoopAttatchment() {
            HashMap<String, Object> data = new HashMap<>();
            
            @Override
            public void run() {
                if (!data.containsKey("score")) data.put("score", 0);
                text.setText("Score: " + Integer.toString((int) data.get("score")));
            }
            
            public void setData(String name, Object value) {
                this.data.put(name, value);
            }
        };
        
        RenderLoop.attach(a);
    }
    
    void updateScore(int score) {
        this.a.setData("score", score);
    }
}
