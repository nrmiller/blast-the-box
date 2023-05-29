package net.nicksneurons.blastthebox.tmp.geometry;

import miller.opengl.Dimension3d;
import miller.opengl.Point3d;

import com.millerni456.BlastTheBox.utils.Textures;

public class Cube extends Box
{
	public int type = 0;
	public int health = 0;
	public boolean indestructible = false;
	
	public Cube(Point3d loc, float size, boolean cubemap)
	{
		super(loc, new Dimension3d(size, size, size), cubemap);
		health = 1;
	}
	
	public void setHeath(int health)
	{
		this.health = health;
		chooseTexture();
	}
	public int getHealth()
	{
		return health;
	}
	public void doDamage(int damage)
	{
		if(!indestructible)
		{
			health -= damage;
			if(health < 0)
			{
				health = 0;
			}
			chooseTexture();
		}
		
	}
	public void setIndestructible(boolean onOff)
	{
		indestructible = onOff;
		chooseTexture();
	}
	public boolean isIndestructible()
	{
		return indestructible;
	}
	public void setType(int type)
	{
		this.type = type;
	}
	public int getType()
	{
		return type;
	}
	public boolean isDead()
	{
		if(health <= 0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	private void chooseTexture()
	{
		if(indestructible)
		{
			setTexture(Textures.block_four);
		}
		else
		{
			switch(health)
			{
				case 1:
					setTexture(Textures.block_one);
					break;
				case 2:
					setTexture(Textures.block_two);
					break;
				case 3:
					setTexture(Textures.block_three);
					break;
				default:
					break;
			}
		}
	}
	
	public final static int NORMAL = 0, GREEN = 1, BLUE = 2, INDESTRUCTIBLE = 3;
}
