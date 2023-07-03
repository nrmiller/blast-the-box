package miller.opengl;

public class Dimension3d 
{
	public double height = 1;
	public double width = 1;
	public double depth = 1;
	
	public Dimension3d(double width, double height, double depth)
	{
		this.width = width;
		this.height = height;
		this.depth = depth;
	}
	
	public double getWidth()
	{
		return width;
	}
	public double getHeight()
	{
		return height;
	}
	public double getDepth()
	{
		return depth;
	}
	
	public void setWidth(double width)
	{
		this.width = width;
	}
	public void setHeight(double height)
	{
		this.height = height;
	}
	public void setDepth(double depth)
	{
		this.depth = depth;
	}
}
