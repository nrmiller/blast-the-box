package net.nicksneurons.blastthebox.tmp.geometry;

import javax.microedition.khronos.opengles.GL10;

public class Triangle extends Primitive
{

	@Override
	public float[] getVertexArray()
	{
		float[] vertices =
		{
			-.5f, -.5f, 0,
			0.5f, -.5f, 0,
			0, 0.5f, 0
		};
		return vertices;
	}

	@Override
	public float[] getTexCoordArray()
	{
		float[] texCoords = 
		{
			0,1,
			1,1,
			.5f,0
		};
		
		return texCoords;
	}
	
	@Override
	public short[] getIndexArray()
	{
		short[] indices = 
		{
				0, 1, 2
		};
		return indices;
	}

	@Override
	public int getRenderingMode()
	{
		return GL10.GL_TRIANGLES;
	}

	@Override
	public float[] getNormalArray()
	{
		float[] normals = 
		{
				0, 0, 1
		};
		return normals;
	}

}
