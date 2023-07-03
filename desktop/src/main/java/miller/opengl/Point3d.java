package miller.opengl;

public class Point3d 
{
	public double x = 0, y = 0, z = 0;

	public Point3d(double x, double y, double z)
	{
		this.x  = x;
		this.y = y;
		this.z = z;
	}
	
	public double getX()
	{
		return x;
	}
	public double getY()
	{
		return y;
	}
	public double getZ()
	{
		return z;
	}
	
	public void setX(double x)
	{
		this.x = x;
	}
	public void setY(double y)
	{
		this.y = y;
	}
	public void setZ(double z)
	{
		this.z = z;
	}
	
	public Vector toVector()
	{
		return new Vector(x, y, z);
	}
	
	public Point2d asPoint2d()
	{
		return new Point2d(x, y);
	}
	
	public String toString()
	{
		return "(" + x + ", " + y + ", " + z + ")";
	}
}
