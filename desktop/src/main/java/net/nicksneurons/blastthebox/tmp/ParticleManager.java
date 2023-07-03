package net.nicksneurons.blastthebox.tmp;

import javax.microedition.khronos.opengles.GL10;

import miller.opengl.Dimension3d;
import miller.opengl.Point3d;

import com.millerni456.BlastTheBox.geometry.Cube;
import com.millerni456.BlastTheBox.geometry.Square;
import com.millerni456.BlastTheBox.utils.RenderQueue;
import com.millerni456.BlastTheBox.utils.RouletteWheel;
import com.millerni456.BlastTheBox.utils.S;
import com.millerni456.BlastTheBox.utils.SquareStorage;
import com.millerni456.BlastTheBox.utils.Textures;

public class ParticleManager
{
	private RouletteWheel indexSelect;
	
	public int MAX_PARTICLES = 200;
	public int PARTICLES_PER_CRATE = 7;
	public float LIFE = 1.15f;
	public int usedParticles = 0;
	public float GRAVITY = 0.01f;
	public Square[] particles = new Square[MAX_PARTICLES];
	public float[] velX = new float[MAX_PARTICLES];
	public float[] velY = new float[MAX_PARTICLES];
	public float[] velZ = new float[MAX_PARTICLES];
	public Point3d[] loc = new Point3d[MAX_PARTICLES];
	public float[] age = new float[MAX_PARTICLES];
	public boolean[] inUse = new boolean[MAX_PARTICLES];
	
	public ParticleManager()
	{
		indexSelect = new RouletteWheel();
		Integer[] indices = {0, 1, 2, 3};
		double[] rarities = {.25, .25, .25, .25};
		indexSelect.setItems(indices, rarities);
		
		for(int i = 0; i<MAX_PARTICLES; i++)
		{
			inUse[i] = false;
		}
	}
	
	public void initParticles(int amount, Point3d loc, int type)
	{
		int num = S.ran.nextInt(4) + PARTICLES_PER_CRATE;//7-10 particles per cube.
		for(int k = 0; k<num; k++)
		{
		for(int i = 0; i<MAX_PARTICLES; i++)
		{
			if(!inUse[i])
			{
				velX[i] = S.ran.nextFloat() * .04f - .02f;
				velY[i] = S.ran.nextFloat() * 2 + .2f;
				velZ[i] = S.ran.nextFloat() * .04f - .02f;
				age[i] = 0;
				this.loc[i] = new Point3d(loc.x+.5-.125, loc.y, loc.z+.5-.125);
				particles[i] = SquareStorage.getSquare();
				if(particles[i]!=null)
				{
					particles[i].setScale(new Dimension3d(.25, .25, 0));
					particles[i].setTexture(pickTexture(type));
					particles[i].setLocation(this.loc[i]);
				}
				
				inUse[i] = true;
				break;
			}
		}
		}
	}
	
	public void update()
	{
		for(int i = 0; i<MAX_PARTICLES; i++)
		{
			if(inUse[i] && particles[i]!=null)
			{
				age[i] += 0.01f;
				
				if(age[i]>LIFE)
				{
					particles[i].setTexture(0);
					clean(i);
					inUse[i] = false;
				}
				else
				{

					loc[i].x += velX[i];
					loc[i].z += velZ[i];
					loc[i].y = -1 * velY[i]*Math.pow((age[i]*2-1), 2) + 2;
					
					particles[i].setLocation(loc[i]);
				}
			}
		}
	}
	
	/**
	 * @deprecated Use ParticleManager.addToQueue(RenderQueue queue).
	 * @param gl
	 */
	public void render(GL10 gl)
	{
		for(int i = 0; i<MAX_PARTICLES; i++)
		{
			if(inUse[i])
			{
				particles[i].draw(gl);
			}
			
		}
	}
	
	public void addToQueue(RenderQueue queue)
	{
		for(int i = 0; i<MAX_PARTICLES; i++)
		{
			if(inUse[i] && particles[i]!=null)
			{
				queue.addToQueue(particles[i]);
			}
			
		}
	}
	
	public float genAngle()
	{
		return (float) (S.ran.nextFloat() * 2 * Math.PI);
	}
	
	public int pickTexture(int type)
	{
		int index = (Integer) indexSelect.spin(S.ran);
		int tex = 0;
		
		switch(type)
		{
			case Cube.NORMAL:
			{
				tex = Explosion.gray_P[index];
				break;
			}
			case Cube.GREEN:
			{
				tex = Explosion.green_P[index];
				break;
			}
			case Cube.BLUE:
			{
				tex = Explosion.blue_P[index];
				break;
			}
			case Cube.INDESTRUCTIBLE:
			{
				tex = Explosion.red_P[index];
				break;
			}
			default:
				break;
		}
		
		return tex;
	}
	
	public synchronized void clean(int index)
	{
		SquareStorage.giveSquare(particles[index]);
		particles[index].setTexture(Textures.trans);
		particles[index] = null;
	}
	
	/**
	 * Force clean particles. Used for when the gameover menu is displayed.
	 */
	public void cleanAll()
	{
		for(int i = particles.length-1; i>=0; i--)
		{
			SquareStorage.giveSquare(particles[i]);
		}
	}
}
