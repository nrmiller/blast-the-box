package miller.opengl;

public class Dimension2d
{
	public double width, height;
	
	public Dimension2d(double w, double h)
	{
		width = w;
		height = h;
	}
	
	public void setWidth(double w){width = w;}
	public void setHeight(double h){height = h;}
	public double getWidth(){return width;}
	public double getHeight(){return height;}
}
