package net.nicksneurons.blastthebox.tmp;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import miller.opengl.Point3d;
import miller.opengl.Dimension3d;
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;

import com.millerni456.BlastTheBox.geometry.Square;
import com.millerni456.BlastTheBox.utils.RenderQueue;
import com.millerni456.BlastTheBox.utils.S;
import com.millerni456.BlastTheBox.utils.SquareStorage;
import com.millerni456.BlastTheBox.utils.Texture;

public class BulletParticles
{
	
	public int MAX_PARTICLES = 5;
	public int MAX_FRAMES = 5;
	public ArrayList<Integer> frames = new ArrayList<Integer>();
	public ArrayList<Square> particles = new ArrayList<Square>();
	public static int[] orange = new int[5];
	public static int[] green = new int[5];
	public static int[] blue = new int[5];
	private boolean canUpdate = true;
	public boolean collided = false;

	private Bullet bullet;
	
	public BulletParticles(Bullet b)
	{
		bullet = b;
	}
	
	public void addParticle()
	{
		frames.add(0);
		particles.add(SquareStorage.getSquare());
		int i = particles.size() - 1;

		if(particles.size()>i)
		{
			if(particles.get(i)!=null)
			{
				particles.get(i).setScale(new Dimension3d(.25, .25, 0));
				particles.get(i).setTexture(pickTexture(0));
				Point3d p = bullet.location.toPoint3d();
				float xOffset = S.ran.nextFloat() * .02f - .01f;
				float yOffset = S.ran.nextFloat() * .02f - .01f;
				p.setX(p.x-.125+.02 + xOffset);
				p.setY(p.y-.125+.05 + yOffset);
				particles.get(i).setLocation(p);
			}
		}
	}
	
	public void updateParticles()
	{
		if(canUpdate)
		{
			canUpdate = false;
			BlastTheBoxActivity.uiHandler.postDelayed(delay, 30);
			
			for(int i = 0; i<frames.size(); i++)
			{
				if(particles.get(i)!=null)
				{
					particles.get(i).setTexture(pickTexture(frames.get(i)));
				}
				frames.set(i, frames.get(i)+1);
				if(frames.get(i)>=MAX_FRAMES)
				{
					frames.remove(i);
					cleanParticle(i);
				}
			}
			if(!collided)
			{//Keep adding particles unless the bullet collides.
				addParticle();
			}
		}
	}
	
	public synchronized void addToQueue(RenderQueue queue)
	{
		for(int i = 0; i<particles.size(); i++)
		{
			queue.addToQueue(particles.get(i));
		}
	}
	
	public int pickTexture(int index)
	{
		int str = bullet.getStrength();
		if(index<5)
		{
			switch(str)
			{
				case 1:
					return orange[index];
				case 2:
					return green[index];
				case 3:
					return blue[index];
				default:
					return orange[index];
			}
		}
		return orange[0];
	}
	
	private Runnable delay = new Runnable()
	{
		public void run()
		{
			canUpdate = true;
		}
	};
	
	public synchronized void cleanParticle(int index)
	{
		SquareStorage.giveSquare(particles.get(index));
		particles.remove(index);
	}
	
	public void cleanAll()
	{
		for(int i = particles.size()-1; i>=0; i--)
		{
			cleanParticle(i);	
		}
	}
	
	public static void loadTextures(Activity app, GL10 gl)
	{
		Resources res = app.getResources();
		
		Bitmap particles = Texture.getBitmapFromDrawable(res, R.drawable.bullet_particles);
		
		Bitmap[] a = new Bitmap[15];
		a[0] = Bitmap.createBitmap(particles, 0, 0, 64, 64);
		a[1] = Bitmap.createBitmap(particles, 64, 0, 64, 64);
		a[2] = Bitmap.createBitmap(particles, 128, 0, 64, 64);
		a[3] = Bitmap.createBitmap(particles, 192, 0, 64, 64);
		a[4] = Bitmap.createBitmap(particles, 256, 0, 64, 64);

		a[5] = Bitmap.createBitmap(particles, 0, 64, 64, 64);
		a[6] = Bitmap.createBitmap(particles, 64, 64, 64, 64);
		a[7] = Bitmap.createBitmap(particles, 128, 64, 64, 64);
		a[8] = Bitmap.createBitmap(particles, 192, 64, 64, 64);
		a[9] = Bitmap.createBitmap(particles, 256, 64, 64, 64);

		a[10] = Bitmap.createBitmap(particles, 0, 128, 64, 64);
		a[11] = Bitmap.createBitmap(particles, 64, 128, 64, 64);
		a[12] = Bitmap.createBitmap(particles, 128, 128, 64, 64);
		a[13] = Bitmap.createBitmap(particles, 192, 128, 64, 64);
		a[14] = Bitmap.createBitmap(particles, 256, 128, 64, 64);
		
		
		Texture[] tex = new Texture[15];
		for(int i = 0; i<tex.length; i++)
		{
			tex[i] = new Texture(gl, a[i]);
		}
		orange[0] = tex[0].textureId;
		orange[1] = tex[1].textureId;
		orange[2] = tex[2].textureId;
		orange[3] = tex[3].textureId;
		orange[4] = tex[4].textureId;
		
		green[0] = tex[5].textureId;
		green[1] = tex[6].textureId;
		green[2] = tex[7].textureId;
		green[3] = tex[8].textureId;
		green[4] = tex[9].textureId;
		
		blue[0] = tex[10].textureId;
		blue[1] = tex[11].textureId;
		blue[2] = tex[12].textureId;
		blue[3] = tex[13].textureId;
		blue[4] = tex[14].textureId;
		
		for(int i = 0; i<a.length; i++)
		{
			a[i].recycle();
		}
		particles.recycle();
	}
}
