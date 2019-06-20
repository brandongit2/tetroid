package net.brandontsang.tetroid.tetroid;

public interface RenderLoopAttatchment {
    void run();
    default void setData(String name, Object value) {}
    default Object getData(String name) {
        return 0;
    }
}
