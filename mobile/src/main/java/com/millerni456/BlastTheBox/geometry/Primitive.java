package com.millerni456.BlastTheBox.geometry;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import miller.opengl.Dimension3d;
import miller.opengl.Point3d;

import com.millerni456.BlastTheBox.utils.Renderable;

public abstract class Primitive implements Renderable
{
	public Point3d loc = new Point3d(0, 0, 0);
	public Dimension3d scale = new Dimension3d(1, 1, 1);
	public float yaw = 0, pitch = 0;
	public int[] id = new int[4];
	public int textureId = 0;
	public float[] vertices;
	public short[] indices;
	public float[] texCoords;
	public float[] normals;
	public int renderMode;
	
	private FloatBuffer vertexBuffer;
	private ShortBuffer indexBuffer;
	private FloatBuffer textureBuffer;
	private FloatBuffer normalBuffer;
	public int texture;
	
	@Override
	public Point3d getLocation()
	{
		return loc;
	}
	
	/**
	 * This method must return the rendering mode for
	 * OpenGL to use for rendering the primitive.
	 * It must be one of the following:
	 * <code><ul>
	 * <li>GL_POINTS</li>
	 * <li>GL_LINES</li>
	 * <li>GL_LINE_STRIP</li>
	 * <li>GL_LINE_LOOP</li>
	 * <li>GL_TRIANGLES</li>
	 * <li>GL_TRIANGLE_STRIP</li>
	 * <li>GL_TRIANGLE_FAN</li>
	 * </ul></code>
	 */
	public abstract int getRenderingMode();
	/**
	 * This method must provide 3-Component vertex data.
	 * The array returned by this method is used to define the
	 * vertices used in creating the primitive. This array
	 * is also sent to the GPU and stored as a VBO.
	 * @return - the array of 3-Component vertices
	 */
	public abstract float[] getVertexArray();
	/**
	 * This method must provide 1-Component index data.
	 * The array returned by this method is used to define the
	 * indices used in creating the primitive. This array
	 * is also sent to the GPU and stored as a VBO.
	 * @return - the array of indices
	 */
	public abstract short[] getIndexArray();
	/**
	 * This method must provide 2-Component texture coordinate data.
	 * The array returned by this method is used to define the
	 * texture coordinates used in creating the primitive. This array
	 * is also sent to the GPU and stored as a VBO.
	 * @return - the array of 2-Component texture coordinates
	 */
	public abstract float[] getTexCoordArray();
	
	public abstract float[] getNormalArray();
	
	public void init(GL10 gl)
	{
		GL11 gl11 = (GL11) gl;
		
		//get array data
		vertices = getVertexArray();
		indices = getIndexArray();
		texCoords = getTexCoordArray();
		normals = getNormalArray();
		renderMode = getRenderingMode();
		
		//create buffers
		ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		vertexBuffer = vbb.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);
		
		ByteBuffer ibb = ByteBuffer.allocateDirect(indices.length * 2);
		ibb.order(ByteOrder.nativeOrder());
		indexBuffer = ibb.asShortBuffer();
		indexBuffer.put(indices);
		indexBuffer.position(0);
		
		ByteBuffer tbb = ByteBuffer.allocateDirect(texCoords.length * 4);
		tbb.order(ByteOrder.nativeOrder());
		textureBuffer = tbb.asFloatBuffer();
		textureBuffer.put(texCoords);
		textureBuffer.position(0);
		
		ByteBuffer nbb = ByteBuffer.allocateDirect(normals.length * 4);
		nbb.order(ByteOrder.nativeOrder());
		normalBuffer = nbb.asFloatBuffer();
		normalBuffer.put(normals);
		normalBuffer.position(0);
		
		//create VBOs
		gl11.glGenBuffers(4, id, 0);
		gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, id[0]);
		gl11.glBufferData(GL11.GL_ARRAY_BUFFER, vertices.length * 4, vertexBuffer, GL11.GL_DYNAMIC_DRAW);
		gl11.glBindBuffer(GL11.GL_ELEMENT_ARRAY_BUFFER, id[1]);
		gl11.glBufferData(GL11.GL_ELEMENT_ARRAY_BUFFER, indices.length * 2, indexBuffer, GL11.GL_DYNAMIC_DRAW);
		gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, id[2]);
		gl11.glBufferData(GL11.GL_ARRAY_BUFFER, texCoords.length * 4, textureBuffer, GL11.GL_DYNAMIC_DRAW);
		gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, id[3]);
		gl11.glBufferData(GL11.GL_ARRAY_BUFFER, normals.length * 4, normalBuffer, GL11.GL_DYNAMIC_DRAW);
		
		gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, 0);
		gl11.glBindBuffer(GL11.GL_ELEMENT_ARRAY_BUFFER, 0);
	}
	
	@Override
	public void draw(GL10 gl)
	{
		GL11 gl11 = (GL11) gl;
		gl.glPushMatrix();
		
		gl.glTranslatef((float)loc.x, (float)loc.y, (float)loc.z);
		gl.glRotatef(yaw, 0 , 1, 0);
		gl.glRotatef(pitch, 1, 0, 0);
		gl.glScalef((float)scale.width, (float)scale.height, (float)scale.depth);
		
		gl11.glEnable(GL10.GL_TEXTURE_2D);
		gl11.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl11.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		
		gl11.glClientActiveTexture(GL10.GL_TEXTURE0);
		
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texture);
		
		
		
		gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, id[0]);
		gl11.glVertexPointer(3, GL10.GL_FLOAT, 0, 0);
		gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, id[2]);
		gl11.glTexCoordPointer(2, GL10.GL_FLOAT, 0, 0);
		gl11.glBindBuffer(GL11.GL_ELEMENT_ARRAY_BUFFER, id[1]);
		gl11.glDrawElements(renderMode, indices.length, GL10.GL_UNSIGNED_SHORT, 0);
		gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, id[3]);
		gl11.glNormalPointer(GL10.GL_FLOAT, 0, 0);
		
		gl11.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl11.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl11.glDisable(GL10.GL_TEXTURE_2D);
		
		gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, 0);
		gl11.glBindBuffer(GL11.GL_ELEMENT_ARRAY_BUFFER, 0);
		
		gl.glPopMatrix();
	}

	public void setTexture(int id)
	{
		this.texture = id;
	}
	
	public void setLocation(Point3d loc)
	{
		this.loc = loc;
	}
	public void setScale(Dimension3d scale)
	{
		this.scale = scale;
	}
	public void setYaw(float yaw)
	{
		this.yaw = yaw;
	}
	public void setPitch(float pitch)
	{
		this.pitch = pitch;
	}
	public int getTexture()
	{
		return texture;
	}
}
