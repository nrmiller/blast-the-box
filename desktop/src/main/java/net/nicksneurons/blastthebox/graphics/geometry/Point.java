package net.nicksneurons.blastthebox.graphics.geometry;

import miller.opengl.Point3d;

import static org.lwjgl.opengl.GL11.GL_POINTS;

public class Point extends Primitive
{
	private Point3d vloc;

	public Point()
	{
		this(new Point3d(0, 0, 0));
	}
	public Point(Point3d loc)
	{
		vloc = loc;

		init();
	}
	
	@Override
	public int getRenderingMode()
	{
		return GL_POINTS;
	}
	
	@Override
	public float[] getVertexArray()
	{
		//Creates a centered Square
		float[] vertices =
		{
			(float)(vloc.x), (float)(vloc.y), (float)vloc.z,
		};
		return vertices;
	}

	@Override
	public float[] getColorArray()
	{
		return getVertexArray();
	}

	@Override
	public float[] getTexCoordArray()
	{
		float[] texCoords = 
		{
			0, 0,
		};
		return texCoords;
	}
	
	@Override
	public short[] getIndexArray()
	{
		short[] indices = 
		{
				0
		};
		return indices;
	}
	@Override
	public float[] getNormalArray()
	{
		float[] normals = 
		{
			0, 0, 0,
		};
		return normals;
	}
}
