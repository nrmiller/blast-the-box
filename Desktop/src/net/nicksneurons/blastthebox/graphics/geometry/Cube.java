package net.nicksneurons.blastthebox.graphics.geometry;

import org.joml.Vector3d;

public class Cube extends Cuboid
{
	public int type = 0;
	public int health = 0;
	public boolean indestructible = false;

	public Cube()
	{
		this(new Vector3d(0, 0, 0), 1.0f, false);
	}

	public Cube(Vector3d loc, float size, boolean cubemap)
	{
		super(loc, new Vector3d(size), cubemap);
		health = 1;
	}
	
	public void setHeath(int health)
	{
		this.health = health;
//		chooseTexture();
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
//			chooseTexture();
		}
		
	}
	public void setIndestructible(boolean onOff)
	{
		indestructible = onOff;
//		chooseTexture();
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
	
//	private void chooseTexture()
//	{
//		if(indestructible)
//		{
//			setTexture(Textures.block_four);
//		}
//		else
//		{
//			switch(health)
//			{
//				case 1:
//					setTexture(Textures.block_one);
//					break;
//				case 2:
//					setTexture(Textures.block_two);
//					break;
//				case 3:
//					setTexture(Textures.block_three);
//					break;
//				default:
//					break;
//			}
//		}
//	}
	
	public final static int NORMAL = 0, GREEN = 1, BLUE = 2, INDESTRUCTIBLE = 3;
}
