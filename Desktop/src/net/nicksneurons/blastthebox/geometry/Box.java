package net.nicksneurons.blastthebox.geometry;

import miller.opengl.Dimension3d;
import miller.opengl.Point3d;
import org.lwjgl.opengl.GL11;

public class Box extends Primitive
{
	private Point3d vloc;
	public Dimension3d dim;
	public final boolean cubeMap;
	
	public Box(Point3d loc, Dimension3d dim, boolean useCubeMap)
	{
		this.vloc = loc;
		this.dim = dim;
		cubeMap = useCubeMap;
	}

	@Override
	public float[] getVertexArray()
	{
		float x = (float) vloc.x;
		float y = (float) vloc.y;
		float z = (float) vloc.z;
		float w = (float) dim.width;
		float h = (float) dim.height;
		float d = (float) dim.depth;
		float[] vertices = 
		{//6 sides times 4 verts times 3 components
				//FRONT
				x, y+h, z+d,
				x, y, z+d,
				x+w, y, z+d,
				x+w, y+h, z+d,
				//RIGHT
				x+w, y+h, z+d,
				x+w, y, z+d,
				x+w, y, z,
				x+w, y+h, z,
				//BACK
				x+w, y+h, z,
				x+w, y, z,
				x, y, z,
				x, y+h, z,
				//LEFT
				x, y+h, z,
				x, y, z,
				x, y, z+d,
				x, y+h, z+d,
				//TOP
				x, y+h, z,
				x, y+h, z+d,
				x+w, y+h, z+d,
				x+w, y+h, z,
				//BOTTOM
				x, y, z+d,
				x, y, z,
				x+w, y, z,
				x+w, y, z+d
				
		};
		return vertices;
	}

	@Override
	public float[] getColorArray()
	{
		return getVertexArray();
	}
	
	@Override
	public short[] getIndexArray()
	{
		short[] indices = 
		{
				0, 1, 2, 0, 2, 3,
				4, 5, 6, 4, 6, 7,
				8, 9, 10, 8, 10, 11,
				12, 13, 14, 12, 14, 15,
				16, 17, 18, 16, 18, 19,
				20, 21, 22, 20, 22, 23
		};
		return indices;
	}

	/**
	 * If Cube-Mapping is enabled in the constructor. This
	 * will adjust coordinates based on a 48x32 image.
	 */
	@Override
	public float[] getTexCoordArray()
	{
		float[] texCoords = 
		{
				0,1,   0,0,   1,0,   1,1,//The same order for each face.
				0,1,   0,0,   1,0,   1,1,
				0,1,   0,0,   1,0,   1,1,
				0,1,   0,0,   1,0,   1,1,
				0,1,   0,0,   1,0,   1,1,
				0,1,   0,0,   1,0,   1,1,
		};
		float[] cubeMapCoords = 
		{
				//FRONT
				0,1,         0,.532f,   .333f,.532f,  .333f,1,//The same order for each face.
				//RIGHT
				.354f,.5f,   .354f,0,   .667f,0,      .667f,.5f,
				//BACK
				0,.5f, 0,0,   .333f,0,   .333f,.5f,
				//LEFT
				.354f,1,   .354f,.532f,   .667f,.532f,   .667f,1,
				//TOP
				.688f,1,   .688f,.532f,   1,.532f,   1,1,
				//BOTTOM
				.688f,.5f,   .688f,0,   1,0,   1,.5f
		};
		
		if(cubeMap)
		{
			return cubeMapCoords;
		}
		else
		{
			return texCoords;
		}
	}
	
	public float[] getNormalArray()
	{
		float[] normals = 
		{
			0, 0, 1, 0, 0, 1,
			1, 0, 0, 1, 0, 0,
			0, 0, -1, 0, 0, -1,
			-1, 0, 0, -1, 0, 0,
			0, 1, 0, 0, 1, 0,
			0, -1, 0, 0, -1, 0
		};
		return normals;
	}
	
	@Override
	public int getRenderingMode()
	{
		return GL11.GL_TRIANGLES;
	}
	

	
	
}
