package net.nicksneurons.blastthebox.utils;

import miller.opengl.Point3d;
import net.nicksneurons.blastthebox.geometry.Cube;

public class CubeStorage
{
	private static Cube[] cubes = new Cube[150];//1100
	private static boolean[] used = new boolean[150];
	
	public static void createCubes()
	{
		for(int i = 0; i<cubes.length; i++)
		{
			cubes[i] = new Cube(new Point3d(0, 0, 0), 1, false);
			cubes[i].init();
		}
	}
	
	public synchronized static Cube getCube()
	{
		for(int i = 0; i<cubes.length; i++)
		{
			if(!used[i])
			{
				used[i] = true;
				return cubes[i];
			}
		}
		return null;
	}
	public synchronized static void giveCube(Cube c)
	{
		for(int i = 0; i<cubes.length; i++)
		{
			if(c==cubes[i])
			{
				used[i] = false;
				break;
			}
		}
	}
}
