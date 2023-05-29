package miller.opengl;

public class Point2d
{
	public double x, y;
	
	public Point2d(double x, double y)
	{
		this.x = x;
		this.y = y;
	}
	
	public void setX(double x){this.x = x;}
	public void setY(double y){this.y = y;}
	public double getX(){return x;}
	public double getY(){return y;}
	
	public String toString()
	{
		double x = (double) Math.round(100*getX())/100;
		double y = (double) Math.round(100*getY())/100;
		return "(" + x + ", " + y + ")";
	}
}
