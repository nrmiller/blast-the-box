package net.nicksneurons.blastthebox.tmp;

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

public class Darkener
{
	public final static int DARKENING = 0, LIGHTENING = 1;
	private static int[] ids = new int[9];
	private Square s;
	
	private boolean canUpdate;
	private Handler darkenHandler = new Handler();
	
	private int mode = DARKENING;
	private int frame = 0;
	
	public Darkener()
	{
		s = SquareStorage.getSquare();
	}
	
	public void darken()
	{
		frame = 0;
		mode = DARKENING;
		canUpdate = true;
	}
	public void lighten()
	{
		frame = 8;
		mode = LIGHTENING;
		canUpdate = true;
	}
	
	public void upate()
	{
		if(canUpdate)
		{
			canUpdate = false;
			darkenHandler.postDelayed(delay, 40);
		}
	}
	
	public void render(GL10 gl, int width, int height)
	{
		if(s!=null)
		{
			s.setLocation(new Point3d(0, 0, 0));
			s.setScale(new Dimension3d(width, height, 0));
			s.setTexture(ids[frame]);
			s.draw(gl);
		}
	}
	
	public void clean()
	{
		if(s!=null)
		{
			SquareStorage.giveSquare(s);
			s = null;
		}
	}
	
	private Runnable delay = new Runnable()
	{
		public void run()
		{
			switch(mode)
			{
				case Darkener.DARKENING:
				{
					frame++;
					if(frame >= 9)
					{
						frame = 8;//Set to last texture.
						return;//Return early so this doesn't constantly try to update.
					}
					break;
				}
				case Darkener.LIGHTENING:
				{
					frame--;
					if(frame < 0)
					{
						frame = 0;
						return;//Return early so this doesn't constantly try to update.
					}
					break;
				}
				default:
					break;
			}
			canUpdate = true;
		}
	};
	
	public static void loadDarkenTextures(Activity app, GL10 gl)
	{
		Resources res = app.getResources();
		Bitmap pixels = Texture.getBitmapFromDrawable(res, R.drawable.darken);
		
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
