package net.nicksneurons.blastthebox.geometry;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import miller.opengl.Dimension3d;
import miller.opengl.Point3d;

import net.nicksneurons.blastthebox.utils.Renderable;
import org.lwjgl.BufferUtils;

import static org.lwjgl.opengl.GL33.*;

public abstract class Primitive implements Renderable
{
	public static final int VBO_INDEX = 0;
	public static final int COLOR_INDEX = 1;
	public static final int TEXCOORD_INDEX = 2;
	public static final int NORMAL_INDEX = 3;
	public static final int ELEMENT_INDEX = 4;

	public int vao;
	public Point3d loc = new Point3d(0, 0, 0);
	public Dimension3d scale = new Dimension3d(1, 1, 1);
	public float yaw = 0, pitch = 0;
	public int[] bufferIds = new int[5];
	public int textureId = 0;
	public float[] vertices;
	public float[] colors;
	public short[] indices;
	public float[] texCoords;
	public float[] normals;
	public int renderMode;

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
		FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertices.length * 4);
		vertexBuffer.put(vertices);
		vertexBuffer.flip();

		FloatBuffer colorBuffer = BufferUtils.createFloatBuffer(colors.length * 4);
		colorBuffer.put(colors);
		colorBuffer.flip();

		ShortBuffer indexBuffer = BufferUtils.createShortBuffer(indices.length * 2);
		indexBuffer.put(indices);
		indexBuffer.flip();

		FloatBuffer texCoordBuffer = BufferUtils.createFloatBuffer(texCoords.length * 4);
		texCoordBuffer.put(texCoords);
		texCoordBuffer.flip();

		FloatBuffer normalBuffer = BufferUtils.createFloatBuffer(normals.length * 4);
		normalBuffer.put(normals);
		normalBuffer.flip();

		vao = glGenVertexArrays();
		glBindVertexArray(vao);

		glEnableVertexAttribArray(VBO_INDEX);
		glEnableVertexAttribArray(COLOR_INDEX);
		glEnableVertexAttribArray(TEXCOORD_INDEX);
		glEnableVertexAttribArray(NORMAL_INDEX);

		glGenBuffers(bufferIds);

		glBindBuffer(GL_ARRAY_BUFFER, bufferIds[VBO_INDEX]);
		glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);
		glVertexAttribPointer(VBO_INDEX, 3, GL_FLOAT, false, 3 * Float.BYTES, 0);

		glBindBuffer(GL_ARRAY_BUFFER, bufferIds[COLOR_INDEX]);
		glBufferData(GL_ARRAY_BUFFER, colorBuffer, GL_STATIC_DRAW);
		glVertexAttribPointer(COLOR_INDEX, 3, GL_FLOAT, false, 3 * Float.BYTES, 0);

		glBindBuffer(GL_ARRAY_BUFFER, bufferIds[TEXCOORD_INDEX]);
		glBufferData(GL_ARRAY_BUFFER, texCoordBuffer, GL_STATIC_DRAW);
		glVertexAttribPointer(TEXCOORD_INDEX, 2, GL_FLOAT, false, 2 * Float.BYTES, 0);

		glBindBuffer(GL_ARRAY_BUFFER, bufferIds[NORMAL_INDEX]);
		glBufferData(GL_ARRAY_BUFFER, normalBuffer, GL_STATIC_DRAW);
		glVertexAttribPointer(NORMAL_INDEX, 3, GL_FLOAT, false, 3 * Float.BYTES, normalBuffer);

		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, bufferIds[ELEMENT_INDEX]);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexBuffer, GL_STATIC_DRAW);

		glBindVertexArray(0);
	}
	
	@Override
	public void draw()
	{
//		glPushMatrix();
//
//		glTranslatef((float)loc.x, (float)loc.y, (float)loc.z);
//		glRotatef(yaw, 0 , 1, 0);
//		glRotatef(pitch, 1, 0, 0);
//		glScalef((float)scale.width, (float)scale.height, (float)scale.depth);
//
//		glEnable(GL_TEXTURE_2D);
//		glClientActiveTexture(GL_TEXTURE0);
//		glBindTexture(GL_TEXTURE_2D, texture);

		glBindVertexArray(vao);
		glDrawElements(renderMode, indices.length, GL_UNSIGNED_SHORT, 0);
		glBindVertexArray(0);

//		glDisable(GL_TEXTURE_2D);
//
//		glPopMatrix();

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

	public void free() {
		// This could be deleted sooner, see: https://stackoverflow.com/a/27937786/975724
		glDeleteBuffers(bufferIds);
		glDeleteVertexArrays(vao);
	}
}
