package com.millerni456.BlastTheBox;

import javax.microedition.khronos.opengles.GL10;

import miller.opengl.Dimension3d;
import miller.opengl.Point3d;
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Handler;

import com.millerni456.BlastTheBox.geometry.Square;
import com.millerni456.BlastTheBox.utils.SquareStorage;
import com.millerni456.BlastTheBox.utils.Texture;

public class FlashTracker
{
	public Flash flash;
	public boolean canUpdate = true;
	public Handler flashHandler = new Handler();
	
	public FlashTracker()
	{
		
	}
	
	public void doFlash(int width, int height)
	{
		flash = new Flash(width, height);
	}
	
	public void update()
	{		
		if(canUpdate)
		{
			canUpdate = false;
			flashHandler.postDelayed(flashUpdater, 40);
		}
	}
	
	public void render(GL10 gl)
	{
		if(flash!=null)
		{
			flash.render(gl);
		}
	}
	private Runnable flashUpdater = new Runnable()
	{
		public void run()
		{
			if(flash!=null)
			{
				flash.nextFrame();
				if(flash.canDispose)
				{
					flash.clean();
					flash = null;
				}
			}
			canUpdate = true;
		}
	};

	/**
	 * Called when there is a gameover.
	 */
	public void clean()
	{
		if(flash!=null)
		{
			flash.clean();
		}
		
	}
}

class Flash
{
	private static int[] ids = new int[9];
	public int frame;
	private Square s;
	public boolean canDispose = false;
	public boolean cleaned = false;
	
	public Flash(int width, int height)
	{
		s = SquareStorage.getSquare();
		if(s!=null)
		{
			s.setLocation(new Point3d(0, 0, 0));
			s.setScale(new Dimension3d(width, height, 1));
			s.setTexture(ids[0]);
		}
	}
	
	public void render(GL10 gl)
	{
		if(s!=null)
		{
			s.setTexture(ids[frame]);
			s.draw(gl);
		}
	}
	
	public void nextFrame()
	{
		frame++;
		if(frame>=9)
		{
			frame = 0;
			canDispose = true;
		}
	}
	
	public void clean()
	{
		if(!cleaned)
		{
			SquareStorage.giveSquare(s);
			s = null;
			cleaned = true;
		}
	}
	
	public static void loadFlashTextures(Activity app, GL10 gl)
	{
		Resources res = app.getResources();
		//Load first set of numbers.
		Bitmap pixels = Texture.getBitmapFromDrawable(res, R.drawable.flash);
		
		Bitmap[] a = new Bitmap[9];
		a[0] = Bitmap.createBitmap(pixels, 0, 0, 1, 1);
		a[1] = Bitmap.createBitmap(pixels, 1, 0, 1, 1);
		a[2] = Bitmap.createBitmap(pixels, 2, 0, 1, 1);
		a[3] = Bitmap.createBitmap(pixels, 0, 1, 1, 1);
		a[4] = Bitmap.createBitmap(pixels, 1, 1, 1, 1);
		a[5] = Bitmap.createBitmap(pixels, 2, 1, 1, 1);
		a[6] = Bitmap.createBitmap(pixels, 0, 2, 1, 1);
		a[7] = Bitmap.createBitmap(pixels, 1, 2, 1, 1);
		a[8] = Bitmap.createBitmap(pixels, 2, 2, 1, 1);
		
		
		for(int i = 0; i<a.length; i++)
		{
			Texture t = new Texture(gl, a[i]);
			ids[i] = t.textureId;
		}
		
	}
}