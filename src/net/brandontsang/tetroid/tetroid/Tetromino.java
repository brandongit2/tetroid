package net.brandontsang.tetroid.tetroid;

import org.joml.Vector3f;
import org.joml.Vector3i;

public enum Tetromino {
    O(
        new Vector3i[] {
            new Vector3i(4, 17, 4),
            new Vector3i(5, 17, 4),
            new Vector3i(4, 17, 5),
            new Vector3i(5, 17, 5)
        },
        new Vector3f(1.0f, 1.0f, 0.0f),
        new Vector3i(4, 17, 4)
    ),
    I(
        new Vector3i[] {
            new Vector3i(3, 17, 4),
            new Vector3i(4, 17, 4),
            new Vector3i(5, 17, 4),
            new Vector3i(6, 17, 4)
        },
        new Vector3f(0.0f, 1.0f, 1.0f),
        new Vector3i(4, 17, 4)
    ),
    T(
        new Vector3i[] {
            new Vector3i(5, 17, 4),
            new Vector3i(4, 17, 5),
            new Vector3i(5, 17, 5),
            new Vector3i(6, 17, 5)
        },
        new Vector3f(1.0f, 0.0f, 1.0f),
        new Vector3i(5, 17, 5)
    ),
    L(
        new Vector3i[] {
            new Vector3i(3, 17, 5),
            new Vector3i(4, 17, 5),
            new Vector3i(5, 17, 5),
            new Vector3i(5, 17, 4)
        },
        new Vector3f(0.0f, 0.0f, 1.0f),
        new Vector3i(5, 17, 5)
    ),
    S(
        new Vector3i[] {
            new Vector3i(4, 17, 5),
            new Vector3i(5, 17, 5),
            new Vector3i(5, 17, 4),
            new Vector3i(6, 17, 4)
        },
        new Vector3f(0.0f, 1.0f, 0.0f),
        new Vector3i(5, 17, 5)
    ),
    RIGHT_SCREW(
        new Vector3i[] {
            new Vector3i(4, 17, 5),
            new Vector3i(4, 17, 4),
            new Vector3i(5, 17, 4),
            new Vector3i(5, 18, 4)
        },
        new Vector3f(0.6f, 0.6f, 0.6f),
        new Vector3i(5, 18, 4)
    ),
    LEFT_SCREW(
        new Vector3i[] {
            new Vector3i(4, 18, 4),
            new Vector3i(4, 17, 4),
            new Vector3i(5, 17, 4),
            new Vector3i(5, 17, 5)
        },
        new Vector3f(0.2f, 0.2f, 0.2f),
        new Vector3i(4, 17, 4)
    ),
    BRANCH(
        new Vector3i[] {
            new Vector3i(4, 17, 4),
            new Vector3i(5, 17, 4),
            new Vector3i(5, 17, 5),
            new Vector3i(5, 18, 4)
        },
        new Vector3f(0.5f, 0.2f, 0.2f),
        new Vector3i(5, 17, 4)
    );
    
    private Vector3i[] positions;
    private Vector3f   color;
    private Vector3i   pivot;
    
    Tetromino(Vector3i[] positions, Vector3f color, Vector3i pivot) {
        this.positions = positions;
        this.color     = color;
        this.pivot     = pivot;
    }
    
    public static Tetromino random() {
        int color = (int) (Math.random() * 8);
        return Tetromino.values()[color];
    }
    
    public Vector3i[] getPositions() {
        return this.positions;
    }
    
    public Vector3f getColor() {
        return this.color;
    }
    
    public Vector3i getPivot() {
        return this.pivot;
    }
}
