package model;

import java.awt.Color;
import java.awt.Graphics;

import view.Window;

public class Point 
{
	private Color color;
	private int size, form;
	private Position position;
	
	public Point(Color color, int size, int form, Position position)
	{
		this.color = color;
		this.size = size;
		this.position = position;
		this.form = form;
	}

	public void paint(Graphics g)
	{
		g.setColor(color);
		if(form == 0)
			g.fillOval(position.getX()-size/2, position.getY()-size/2, size, size);
		else if(form == 1)
			g.fillRect(position.getX()-size/2, position.getY()-size/2, size, size);
		else
			g.drawImage(Window.getImage("selection.jpg"),position.getX()-size/2, position.getY()-size/2,null);
	}
	
	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(int x, int y) {
		this.position = new Position(x, y);
	}

	public int getForm() {
		return form;
	}

	public void setForm(int form) {
		this.form = form;
	}
	
}
