package net.nicksneurons.blastthebox.geometry;

import miller.opengl.Dimension2d;
import miller.opengl.Point2d;

public class Rectangle
{
	public int x, y, width, height;
	public Rectangle(int x, int y, int width, int height)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public void setLocation(Point2d loc)
	{
		x = (int) loc.x;
		y = (int) loc.y;
	}
	public void setLocation(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	public void setSize(Dimension2d dim)
	{
		width = (int) dim.width;
		height = (int) dim.height;
	}
	public void setSize(int width, int height)
	{
		this.width = width;
		this.height = height;
	}
}
