package com.dpc.apcs.telestrations;

import java.awt.Color;

public class ColoredLine extends PaintShape {
    private int x1, y1;
    private Color color;
    
    public ColoredLine(Color c, float stroke, int x, int y, int x1, int y1) {
        super(stroke, x, y);
        
        this.color = c;
        
        this.x1 = x1;
        this.y1 = y1;
    }
    
    public void setColor(Color color) {
        this.color = color;
    }
    
    public Color getColor() {
        return color;
    }
    
    public int getX1() {
        return x1;
    }
    
    public int getY1() {
        return y1;
    }
}
