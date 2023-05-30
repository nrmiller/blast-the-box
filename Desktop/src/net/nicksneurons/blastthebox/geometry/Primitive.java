package net.nicksneurons.blastthebox.geometry;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import miller.opengl.Dimension3d;
import miller.opengl.Point3d;

import net.nicksneurons.blastthebox.utils.Renderable;

import org.lwjgl.opengl.*;

public abstract class Primitive implements Renderable
{
	public Point3d loc = new Point3d(0, 0, 0);
	public Dimension3d scale = new Dimension3d(1, 1, 1);
	public float yaw = 0, pitch = 0;
	public int[] id = new int[5];
	public int textureId = 0;
	public float[] vertices;
	public float[] colors;
	public short[] indices;
	public float[] texCoords;
	public float[] normals;
	public int renderMode;
	
	private FloatBuffer vertexBuffer;
	private FloatBuffer colorBuffer;
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

	public abstract float[] getColorArray();

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

	@Override
	public void init()
	{
		
		//get array data
		vertices = getVertexArray();
		colors = getColorArray();
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

		ByteBuffer cbb = ByteBuffer.allocateDirect(colors.length * 4);
		cbb.order(ByteOrder.nativeOrder());
		colorBuffer = cbb.asFloatBuffer();
		colorBuffer.put(colors);
		colorBuffer.position(0);
		
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
		GL15.glGenBuffers(id);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, id[0]);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertexBuffer, GL15.GL_DYNAMIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, id[1]);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, colorBuffer, GL15.GL_DYNAMIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, id[2]);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indexBuffer, GL15.GL_DYNAMIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, id[3]);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, textureBuffer, GL15.GL_DYNAMIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, id[4]);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, normalBuffer, GL15.GL_DYNAMIC_DRAW);
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
	}
	
	@Override
	public void draw()
	{
		GL21.glPushMatrix();
		
		GL11.glTranslatef((float)loc.x, (float)loc.y, (float)loc.z);
		GL11.glRotatef(yaw, 0 , 1, 0);
		GL11.glRotatef(pitch, 1, 0, 0);
		GL11.glScalef((float)scale.width, (float)scale.height, (float)scale.depth);
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
		GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
		
		GL13.glClientActiveTexture(GL13.GL_TEXTURE0);
		
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
		
		
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, id[0]);
		GL11.glVertexPointer(3, GL15.GL_FLOAT, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, id[2]);
		GL11.glTexCoordPointer(2, GL15.GL_FLOAT, 0, 0);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, id[1]);
		GL11.glDrawElements(renderMode, indices.length, GL15.GL_UNSIGNED_SHORT, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, id[3]);
		GL11.glNormalPointer(GL15.GL_FLOAT, 0, 0);
		
		GL11.glDisableClientState(GL15.GL_VERTEX_ARRAY);
		GL11.glDisableClientState(GL15.GL_TEXTURE_COORD_ARRAY);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		
		GL11.glPopMatrix();
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
