package net.nicksneurons.blastthebox.tmp.utils;

import javax.microedition.khronos.opengles.GL10;

import miller.opengl.Dimension2d;
import miller.opengl.Point3d;

import com.millerni456.BlastTheBox.geometry.Square;

public class SquareStorage
{
	private static Square[] squares = new Square[500];
	private static boolean[] used = new boolean[500];
	
	public static void createSquares(GL10 gl)
	{
		for(int i = 0; i<squares.length; i++)
		{
			squares[i] = new Square(new Point3d(0, 0, 0), new Dimension2d(1, 1));
			squares[i].init(gl);
		}
	}
	
	public synchronized static Square getSquare()
	{
		for(int i = 0; i<squares.length; i++)
		{
			if(!used[i])
			{
				used[i] = true;
				return squares[i];
			}
		}
		return null;
	}
	public synchronized static void giveSquare(Square s)
	{
		for(int i = 0; i<squares.length; i++)
		{
			if(s==squares[i])
			{
				used[i] = false;
				break;
			}
		}
	}
}
