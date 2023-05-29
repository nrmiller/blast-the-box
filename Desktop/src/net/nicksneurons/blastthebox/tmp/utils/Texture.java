package net.nicksneurons.blastthebox.tmp.utils;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.khronos.opengles.GL10;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

public class Texture
{
	public String name;
	public int textureId;
	public static final int REPEAT_S = 0x01;
	public static final int REPEAT_T = 0x02;
	
	public Texture(GL10 gl, InputStream data, String name)
	{
		this.name = name;
		Bitmap bitmap;
		
		try
		{
			bitmap = BitmapFactory.decodeStream(data);
			if(bitmap!=null)
			{
				createTexture(gl, bitmap);
				
			}
		}
		finally
		{
			try
			{
				data.close();
			}
			catch(IOException e)
			{}
		}
	}
	public Texture(GL10 gl, Bitmap bm)
	{
		if(bm!=null)
			createTexture(gl, bm);
	}
	
	private void createTexture(GL10 gl, Bitmap bitmap)
	{
		int[] id = new int[1];
		gl.glGenTextures(1, id, 0);//create a texture ID and put in array.
		textureId = id[0];
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);//bind texture
		gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
		gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
		//By default clamp everything to edge.
		gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
		gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);
		
		
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
		bitmap.recycle();
		bitmap = null;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public String getName()
	{
		return name;
	}
	
	public int getId()
	{
		return textureId;
	}
	
	public void bind(GL10 gl)
	{
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);
	}
	public void setRepeating(GL10 gl, int repeatFlags)
	{
		bind(gl);
		
		if((repeatFlags & REPEAT_S)==REPEAT_S)
		{//If REPEAT_S flag was specified.
			gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_REPEAT);
		}
		if((repeatFlags & REPEAT_T)==REPEAT_T)
		{//If REPEAT_T flag was specified.
			gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_REPEAT);
		}
	}
	
	public static void setTextureParameter(GL10 gl, int pname, int param)
	{
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, pname, param);
	}
	
	public static void setTextureEnvelope(GL10 gl, int pname, int param)
	{
		gl.glTexEnvf(GL10.GL_TEXTURE_ENV, pname, param);
	}
	
	/**
	 * Creates a Bitmap object from a drawable resource.
	 * <b>Make sure to call <code>Bitmap.recycle()</code> when finished with the Bitmap.</b>
	 * @param res - a handle to the applications resources
	 * @param id - the resource ID pointing to the desired image
	 * @return a bitmap of the image pointed by the res ID
	 */
	public static Bitmap getBitmapFromDrawable(Resources res, int id)
	{
		InputStream is = res.openRawResource(id);
		Bitmap b = BitmapFactory.decodeStream(is);
		
		try
		{
			is.close();
		}
		catch(IOException e){}
		
		return b;
	}
}
