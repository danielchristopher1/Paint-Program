package com.dpc.apcs.telestrations;

public class Circle extends PaintShape {
    private int radius;
    
    public Circle(int radius, float stroke, int x, int y) {
        super(stroke, x, y);
        this.radius = radius;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }
}
