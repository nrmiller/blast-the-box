package net.nicksneurons.blastthebox.tmp;

import javax.microedition.khronos.opengles.GL10;

import miller.opengl.Dimension3d;
import miller.opengl.Point3d;

import com.millerni456.BlastTheBox.geometry.Square;
import com.millerni456.BlastTheBox.utils.SquareStorage;
import com.millerni456.BlastTheBox.utils.Textures;

public class HealthTracker
{
	private Square[] squares;
	private Square shield_cover;
	private int maxhealth;
	private int health;
	private boolean hasShield = false;
	private boolean initialized = false;
	
	/**
	 * This cannot be instantiated before the SquareStorage is created.
	 * HealthTracker should be instantiated after health settings are applied
	 * to the player.
	 * @param p
	 */
	public HealthTracker(Player p)
	{
		health = p.getHealth();
		maxhealth = p.getMaximumHealth();
	}
	
	private void init(int width, int height)
	{
		initialized = true;
		
		int health_W = (int) (0.075*height);
		int padding_W = (int) (0.125*height-health_W)/2;
		int health_H = (int) (health_W/2);
		int x = width - health_W - padding_W;
		int y = height - health_H - padding_W;
		
		squares = new Square[maxhealth];
		for(int i = 0; i<squares.length; i++)
		{
			squares[i] = SquareStorage.getSquare();
			squares[i].setLocation(new Point3d(x, y - i*health_H, 0));
			squares[i].setScale(new Dimension3d(health_W, health_H, 1));
			squares[i].setTexture(Textures.health_on);
		}
		
		shield_cover = SquareStorage.getSquare();
		shield_cover.setLocation(new Point3d(x, y - (maxhealth-1)*health_H, 0));
		shield_cover.setScale(new Dimension3d(health_W, health_H*maxhealth, 0));
		shield_cover.setTexture(Textures.shield_cover);
	}
	
	/**
	 * Performs operations based on orthogonal projection.
	 * Does not change projection mode.
	 * @param gl
	 */
	public void renderHealth(GL10 gl, int width, int height)
	{
		if(!initialized)
		{
			init(width, height);
		}
		
		if(hasShield)
		{
			shield_cover.draw(gl);
		}
		
		for(int i = 0; i<maxhealth; i++)
		{
			//Change texture accordingly.
			if(i<health)
			{
				squares[i].setTexture(Textures.health_on);
			}
			else
			{
				squares[i].setTexture(Textures.health_off);
			}
			
			squares[i].draw(gl);
		}
	}

	public void clean()
	{
		SquareStorage.giveSquare(shield_cover);
		for(int i = 0; i<squares.length; i++)
		{
			SquareStorage.giveSquare(squares[i]);
		}
		
		initialized = false;
	}

	public void setHealth(int health)
	{
		this.health = health;
	}
	public void setHasShield(boolean hasShield)
	{
		this.hasShield = hasShield;
	}
}
