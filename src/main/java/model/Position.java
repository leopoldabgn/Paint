package model;

import java.io.Serializable;

public class Position implements Serializable
{
	private static final long serialVersionUID = 6888272755517546218L;
	private int x, y;
	
	public Position(int x, int y)
	{
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
}
