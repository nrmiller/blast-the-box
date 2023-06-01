package net.nicksneurons.blastthebox.utils;

import miller.opengl.Dimension2d;
import miller.opengl.Point3d;

import net.nicksneurons.blastthebox.geometry.Square;

public class SquareStorage
{
	private static Square[] squares = new Square[500];
	private static boolean[] used = new boolean[500];
	
	public static void createSquares()
	{
		for(int i = 0; i<squares.length; i++)
		{
			squares[i] = new Square();
			squares[i].init();
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
