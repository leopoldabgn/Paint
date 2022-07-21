package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;

import model.Position;

public class DeleteZone extends Rectangle {
    
    private Color color;

    public DeleteZone(Color color, Position pos, Dimension dim) {
        setLocation(pos.getX(), pos.getY());
        setSize((int)dim.getWidth(), (int)dim.getHeight());
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

}
