package net.nicksneurons.blastthebox.tmp;

import javax.microedition.khronos.opengles.GL10;

import miller.opengl.Dimension2d;
import miller.opengl.Point3d;

import com.millerni456.BlastTheBox.geometry.Square;
import com.millerni456.BlastTheBox.utils.Textures;

public class Scene
{
	private boolean initialized;
	private Square floor_tile1, floor_tile2, floor_tile3, floor_tile4;
	private Square left_side1, left_side2, left_side3, left_side4;
	private Square right_side1, right_side2, right_side3, right_side4;
	public float z, z2, z3, z4;
	
	public Scene()
	{
		initialized = false;
		z = 0;
		z2 = 20;
		z3 = 40;
		z4 = 60;
	}
	
	public void init(GL10 gl)
	{
		if(!initialized)
		{
			floor_tile1 = new Square(new Point3d(0, 0, 0), new Dimension2d(20, 20), 20);
			floor_tile1.init(gl);
			floor_tile1.setPitch(-90);
			floor_tile1.setTexture(Textures.floor);
			left_side1 = new Square(new Point3d(0, 0, 0), new Dimension2d(1, 20), 1, 20);
			left_side1.init(gl);
			left_side1.setPitch(-90);
			left_side1.setTexture(Textures.floor_transr);
			right_side1 = new Square(new Point3d(0, 0, 0), new Dimension2d(1, 20), 1, 20);
			right_side1.init(gl);
			right_side1.setPitch(-90);
			right_side1.setTexture(Textures.floor_transl);
			
			floor_tile2 = new Square(new Point3d(0, 0, 0), new Dimension2d(20, 20), 20);
			floor_tile2.init(gl);
			floor_tile2.setPitch(-90);
			floor_tile2.setTexture(Textures.floor);
			left_side2 = new Square(new Point3d(0, 0, 0), new Dimension2d(1, 20), 1, 20);
			left_side2.init(gl);
			left_side2.setPitch(-90);
			left_side2.setTexture(Textures.floor_transr);
			right_side2 = new Square(new Point3d(0, 0, 0), new Dimension2d(1, 20), 1, 20);
			right_side2.init(gl);
			right_side2.setPitch(-90);
			right_side2.setTexture(Textures.floor_transl);
		
			floor_tile3 = new Square(new Point3d(0, 0, 0), new Dimension2d(20, 20), 20);
			floor_tile3.init(gl);
			floor_tile3.setPitch(-90);
			floor_tile3.setTexture(Textures.floor);
			left_side3 = new Square(new Point3d(0, 0, 0), new Dimension2d(1, 20), 1, 20);
			left_side3.init(gl);
			left_side3.setPitch(-90);
			left_side3.setTexture(Textures.floor_transr);
			right_side3 = new Square(new Point3d(0, 0, 0), new Dimension2d(1, 20), 1, 20);
			right_side3.init(gl);
			right_side3.setPitch(-90);
			right_side3.setTexture(Textures.floor_transl);
			
			floor_tile4 = new Square(new Point3d(0, 0, 0), new Dimension2d(20, 20), 20);
			floor_tile4.init(gl);
			floor_tile4.setPitch(-90);
			floor_tile4.setTexture(Textures.floor);
			left_side4 = new Square(new Point3d(0, 0, 0), new Dimension2d(1, 20), 1, 20);
			left_side4.init(gl);
			left_side4.setPitch(-90);
			left_side4.setTexture(Textures.floor_transr);
			right_side4 = new Square(new Point3d(0, 0, 0), new Dimension2d(1, 20), 1, 20);
			right_side4.init(gl);
			right_side4.setPitch(-90);
			right_side4.setTexture(Textures.floor_transl);
			
			floor_tile1.setLocation(new Point3d(-10, 0, -z));
			floor_tile2.setLocation(new Point3d(-10, 0, -z2));
			floor_tile3.setLocation(new Point3d(-10, 0, -z3));
			floor_tile4.setLocation(new Point3d(-10, 0, -z4));
			left_side1.setLocation(new Point3d(-11, 0, -z));
			left_side2.setLocation(new Point3d(-11, 0, -z2));
			left_side3.setLocation(new Point3d(-11, 0, -z3));
			left_side4.setLocation(new Point3d(-11, 0, -z4));
			right_side1.setLocation(new Point3d(10, 0, -z));
			right_side2.setLocation(new Point3d(10, 0, -z2));
			right_side3.setLocation(new Point3d(10, 0, -z3));
			right_side4.setLocation(new Point3d(10, 0, -z4));
			initialized = true;
		}
	}

	public void moveCloser(float amount)
	{
		z -= amount;
		z2 -= amount;
		z3 -= amount;
		z4 -= amount;
		
		if(z <= -20)
		{
			z += 60;
		}
		if(z2 <= -20)
		{
			z2 += 60;
		}
		if(z3 <= -20)
		{
			z3 += 60;
		}
		if(z4 <= -20)
		{
			z4 += 60;
		}
		
		if(initialized)
		{
			floor_tile1.setLocation(new Point3d(-10, 0, -z));
			floor_tile2.setLocation(new Point3d(-10, 0, -z2));
			floor_tile3.setLocation(new Point3d(-10, 0, -z3));
			floor_tile4.setLocation(new Point3d(-10, 0, -z4));
			left_side1.setLocation(new Point3d(-11, 0, -z));
			left_side2.setLocation(new Point3d(-11, 0, -z2));
			left_side3.setLocation(new Point3d(-11, 0, -z3));
			left_side4.setLocation(new Point3d(-11, 0, -z4));
			right_side1.setLocation(new Point3d(10, 0, -z));
			right_side2.setLocation(new Point3d(10, 0, -z2));
			right_side3.setLocation(new Point3d(10, 0, -z3));
			right_side4.setLocation(new Point3d(10, 0, -z4));
		}
	}
	
	public void render(GL10 gl)
	{
		init(gl);
		
		floor_tile1.draw(gl);
		floor_tile2.draw(gl);
		floor_tile3.draw(gl);
		floor_tile4.draw(gl);
		left_side1.draw(gl);
		left_side2.draw(gl);
		left_side3.draw(gl);
		left_side4.draw(gl);
		right_side1.draw(gl);
		right_side2.draw(gl);
		right_side3.draw(gl);
		right_side4.draw(gl);
	}
}
