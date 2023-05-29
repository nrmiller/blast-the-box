package miller.opengl;

public class Vector
{
	
	public double x = 0, y = 0, z = 0, w = 0;
	
	public Vector(double x)
	{//1 component vector
		this.x = x;
	}
	public Vector(double x, double y)
	{//2 component vector
		this.x = x;
		this.y = y;
	}
	public Vector(double x, double y, double z)
	{//3 component vector
		this.x = x;
		this.y = y;
		this.z = z;
	}
	public Vector(double x, double y, double z, double w)
	{//4 component vector
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	public Vector(Point2d point)
	{
		this.x = point.getX();
		this.y = point.getY();
	}
	public Vector(Point3d point)
	{
		this.x = point.getX();
		this.y = point.getY();
		this.z = point.getZ();
	}
	
	public Vector add(Vector vec)
	{
		x += vec.x;
		y += vec.y;
		z += vec.z;
		return this;
	}
	
	public Vector subtract(Vector vec)
	{
		x -= vec.x;
		y -= vec.y;
		z -= vec.z;
		return this;
	}
	
	public Vector scale(double scalar)
	{
		x *= scalar;
		y *= scalar;
		z *= scalar;
		return this;
	}
	
	public Vector getNormalVector()
	{
		double mag = magnitude();
		Vector normal = new Vector(x, y, z);
		try
		{
			normal.x /= mag;
			normal.y /= mag;
			normal.z /= mag;
		}
		catch(ArithmeticException e)
		{
			System.err.println("Vector must have a non-zero magnitude.\n"
								+ "Vector cannot be at the origin");
		}
		return normal;
	}
	
	public double magnitude()
	{
		double mag = Math.sqrt((x*x + y*y + z*z));
		return mag;
	}
	
	public Point3d toPoint3d()
	{
		return new Point3d(x, y, z);
	}
	
	
}
