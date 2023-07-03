package com.millerni456.BlastTheBox;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import miller.opengl.Dimension3d;
import miller.opengl.Point3d;
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Handler;

import com.millerni456.BlastTheBox.geometry.Cube;
import com.millerni456.BlastTheBox.geometry.Square;
import com.millerni456.BlastTheBox.utils.RenderQueue;
import com.millerni456.BlastTheBox.utils.SquareStorage;
import com.millerni456.BlastTheBox.utils.Texture;

public class ExplosionTracker
{
	private Handler explosionHandler = new Handler();
	public ArrayList<Explosion> explosions = new ArrayList<Explosion>();
	private boolean canUpdate = true;
	public ParticleManager pm;
	
	public ExplosionTracker()
	{
		pm = new ParticleManager();
	}
	
	public void createExplosion(Point3d location, Cube c)
	{
		Explosion e = new Explosion(location);
		explosions.add(e);
		
		pm.initParticles(10, c.loc, c.type);
	}
	
	public void addToQueue(RenderQueue queue)
	{
		for(int i = 0; i<explosions.size(); i++)
		{
			explosions.get(i).addToQueue(queue);
		}
		pm.addToQueue(queue);
	}
	
	public void update()
	{		
		if(canUpdate)
		{
			canUpdate = false;
			explosionHandler.postDelayed(explosionUpdater, 40);
		}
		pm.update();
	}
	public void moveCloser(double amount)
	{
		for(int i = 0; i<explosions.size(); i++)
		{
			explosions.get(i).moveCloser(amount);
		}
	}
	
	private Runnable explosionUpdater = new Runnable()
	{
		public void run()
		{
			for(int i = explosions.size()-1; i>=0; i--)
			{
				explosions.get(i).nextFrame();
				
				if(explosions.get(i).canDispose())
				{
					explosions.get(i).clean();
					explosions.remove(i);
				}
			}
			canUpdate = true;
		}
	};
	
	/**
	 * Force clean explosions. Used for when the gameover menu is displayed.
	 */
	public void clean()
	{
		pm.cleanAll();
		for(int i = explosions.size()-1; i>=0; i--)
		{
			explosions.get(i).clean();
			explosions.remove(i);
		}
	}
}

class Explosion
{
	public static int[] gray_P = new int[4];
	public static int[] green_P = new int[4];
	public static int[] blue_P = new int[4];
	public static int[] red_P = new int[4];
	private static int[] ids = new int[10]; //static texture ids
	public boolean canDispose = false;
	public Point3d loc;
	public int frame = 0; //Current frame
	private Square s;
	public double distance;
	
	public Explosion(Point3d location)
	{	
		loc = new Point3d(location.x, location.y, location.z);
		try
		{	
			s = SquareStorage.getSquare();
			distance = -loc.z;
			s.setLocation(loc);
			s.setScale(new Dimension3d(1, 1, 1));
			s.setTexture(ids[0]);
		}
		catch(Exception e)
		{}
	}
	
	public static void loadExplosion(Activity app, GL10 gl)
	{
		Resources res = app.getResources();
		
		Bitmap explosion = Texture.getBitmapFromDrawable(res, R.drawable.explosion);
		
		int w = (int) (explosion.getWidth() / 5);
		Bitmap[] a = new Bitmap[10];
		a[0] = Bitmap.createBitmap(explosion, 0, 0, w, w);
		a[1] = Bitmap.createBitmap(explosion, w, 0, w, w);
		a[2] = Bitmap.createBitmap(explosion, w*2, 0, w, w);
		a[3] = Bitmap.createBitmap(explosion, w*3, 0, w, w);
		a[4] = Bitmap.createBitmap(explosion, w*4, 0, w, w);
		a[5] = Bitmap.createBitmap(explosion, 0, w, w, w);
		a[6] = Bitmap.createBitmap(explosion, w, w, w, w);
		a[7] = Bitmap.createBitmap(explosion, w*2, w, w, w);
		a[8] = Bitmap.createBitmap(explosion, w*3, w, w, w);
		a[9] = Bitmap.createBitmap(explosion, w*4, w, w, w);
		
		for(int i = 0; i<a.length; i++)
		{
			Texture t = new Texture(gl, a[i]);
			ids[i] = t.textureId;
		}
		
		for(int i = 0; i<a.length; i++)
		{
			a[i].recycle();
		}
		explosion.recycle();
		
		Bitmap pieces = Texture.getBitmapFromDrawable(res, R.drawable.block_particles);
		
		Bitmap[] b = new Bitmap[16];
		b[0] = Bitmap.createBitmap(pieces, 0, 0, 32, 32);
		b[1] = Bitmap.createBitmap(pieces, 32, 0, 32, 32);
		b[2] = Bitmap.createBitmap(pieces, 64, 0, 32, 32);
		b[3] = Bitmap.createBitmap(pieces, 96, 0, 32, 32);
		
		b[4] = Bitmap.createBitmap(pieces, 0, 32, 32, 32);
		b[5] = Bitmap.createBitmap(pieces, 32, 32, 32, 32);
		b[6] = Bitmap.createBitmap(pieces, 64, 32, 32, 32);
		b[7] = Bitmap.createBitmap(pieces, 96, 32, 32, 32);
		
		b[8] = Bitmap.createBitmap(pieces, 0, 64, 32, 32);
		b[9] = Bitmap.createBitmap(pieces, 32, 64, 32, 32);
		b[10] = Bitmap.createBitmap(pieces, 64, 64, 32, 32);
		b[11] = Bitmap.createBitmap(pieces, 96, 64, 32, 32);
		
		b[12] = Bitmap.createBitmap(pieces, 0, 96, 32, 32);
		b[13] = Bitmap.createBitmap(pieces, 32, 96, 32, 32);
		b[14] = Bitmap.createBitmap(pieces, 64, 96, 32, 32);
		b[15] = Bitmap.createBitmap(pieces, 96, 96, 32, 32);
		
		Texture[] texes = new Texture[b.length];
		for(int i = 0; i<b.length; i++)
		{
			texes[i] = new Texture(gl, b[i]);
		}
		
		gray_P[0] = texes[0].textureId;
		gray_P[1] = texes[1].textureId;
		gray_P[2] = texes[2].textureId;
		gray_P[3] = texes[3].textureId;
		
		green_P[0] = texes[4].textureId;
		green_P[1] = texes[5].textureId;
		green_P[2] = texes[6].textureId;
		green_P[3] = texes[7].textureId;
		
		blue_P[0] = texes[8].textureId;
		blue_P[1] = texes[9].textureId;
		blue_P[2] = texes[10].textureId;
		blue_P[3] = texes[11].textureId;
		
		red_P[0] = texes[12].textureId;
		red_P[1] = texes[13].textureId;
		red_P[2] = texes[14].textureId;
		red_P[3] = texes[15].textureId;
		
		pieces.recycle();
		for(int i = 0; i<b.length; i++)
		{
			b[i].recycle();
		}
	}
	
	/**
	 * @deprecated Use Explosion.addToQueue(RenderQueue queue).
	 * @param gl
	 */
	public synchronized void render(GL10 gl)
	{
		if(s!=null)
		{
			s.setTexture(ids[frame]);
			s.draw(gl);
		}
	}
	public synchronized void addToQueue(RenderQueue queue)
	{
		if(s!=null)
		{
			s.setTexture(ids[frame]);
			queue.addToQueue(s);
		}
	}
	
	public void moveCloser(double amount)
	{
		distance -= amount;

		s.setLocation(new Point3d(s.loc.x, 0, -distance));
	}
	
	public void nextFrame()
	{
		frame++;
		if(frame>=10)
		{
			frame = 0;
			canDispose = true;
		}
	}
	
	public boolean canDispose()
	{
		return canDispose;
	}
	
	public synchronized void clean()
	{
		SquareStorage.giveSquare(s);
		s = null;
	}
}