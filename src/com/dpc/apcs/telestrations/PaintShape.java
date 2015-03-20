package com.dpc.apcs.telestrations;

public class PaintShape {
    private int x, y;
    private float stroke;
    
    public PaintShape(float stroke, int x, int y) {
        this.stroke = stroke;
        this.x = x;
        this.y = y;
    }
    
    public void setStroke(float stroke) {
        this.stroke = stroke;
    }
    
    public float getStroke() {
        return stroke;
    }
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
    
    public void setX(int x) {
        this.x = x;
    }
    
    public void setY(int y) {
        this.y = y;
    }
}
