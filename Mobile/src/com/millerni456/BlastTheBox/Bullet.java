package com.millerni456.BlastTheBox;

import miller.opengl.Dimension3d;
import miller.opengl.Vector;

import com.millerni456.BlastTheBox.geometry.Cube;
import com.millerni456.BlastTheBox.utils.CubeStorage;
import com.millerni456.BlastTheBox.utils.RenderQueue;
import com.millerni456.BlastTheBox.utils.Textures;

public class Bullet
{
	public Player player;
	public Vector location = new Vector(-.02, -.55, 0);
	public Vector velocity = new Vector(0, 0, .001);
	public Vector acceleration = new Vector(-.1, -.1, -.1);//Halfed acceleration since FPS is double.
	public float yaw = 0;
	private Cube c;
	private boolean canDispose;
	public boolean hasPierce = false;
	private int pierceHits = 0;
	public int strength = 1;

	public BulletParticles bp;
	
	public Bullet(Player player)
	{
		this(player, new Vector(0, 0, .0015), false);
	}
	public Bullet(Player player, Vector velocity, boolean piercing)
	{
		bp = new BulletParticles(this);
		this.player = player;
		strength = player.strength;
		hasPierce = piercing;
		canDispose = false;
		this.velocity = velocity;
		location.add(new Vector(player.location));
		c = CubeStorage.getCube();
		if(c!=null)
		{
			c.setScale(new Dimension3d(.04, .1, .1));
			c.setTexture(pickMissleTexture(player.strength));
			c.setLocation(location.toPoint3d());
		}
	}
	
	public void addToQueue(RenderQueue queue)
	{
		queue.addToQueue(c);
		bp.addToQueue(queue);
	}
	
	public void applyAccelerationVector(Vector a)
	{
		velocity.add(a);
	}
	
	public void update()
	{
		if(!bp.collided)
		{
			Vector direction = velocity.getNormalVector();
			velocity.add(new Vector(direction.x * acceleration.x, direction.y * acceleration.y, direction.z * acceleration.z));
			location.add(new Vector(velocity.x, 0, velocity.z));
			c.setLocation(location.toPoint3d());
		
			if(c.loc.x<-CubePopulator.FIELD_WIDTH/2-5||c.loc.x>CubePopulator.FIELD_WIDTH/2+5
					||c.loc.z<-40||c.loc.z>2)
			{//Bullet out of range.
				bp.collided = true;
				canDispose = true;
			}
		}
		bp.updateParticles();
		
	}
	public boolean checkCollision(Cube cube)
	{
		if(cube!=null)
		{
			if(c.loc.x+.02>=cube.loc.x&&c.loc.x+.02<=cube.loc.x + cube.dim.width)
			{
				if(c.loc.y>=cube.loc.y&&c.loc.y<=cube.loc.y + cube.dim.height)
				{
					if(c.loc.z-.05>=cube.loc.z&&c.loc.z-.05<=cube.loc.z + cube.dim.depth)
					{
						if(!hasPierce)
						{
							canDispose = true;
							bp.collided = true;
						}
						else
						{
							pierceHits++;
							if(pierceHits > 10)
							{//Maximum 10 hits for a piercing bullet.
								canDispose = true;
								bp.collided = true;
							}
						}
						return true;
					}
				}
			}
		}
		return false;
	}
	
	private int pickMissleTexture(int strength)
	{
		switch(strength)
		{
			case 1:
				return Textures.missile;
			case 2:
				return Textures.missile2;
			case 3:
				return Textures.missile3;
		}
		return Textures.missile;
	}
	
	public void clean()
	{
		bp.cleanAll();
		bp = null;
		c.setYaw(0);
		CubeStorage.giveCube(c);
		c = null;
	}
	
	public boolean canDispose()
	{
		return canDispose;
	}
	
	public void setOrientation(float yaw)
	{
		this.yaw = yaw;
		c.setYaw(yaw);
	}
	
	public Player getSender()
	{
		return player;
	}
	public int getStrength()
	{
		return strength;
	}
	
	public void setPiercing(boolean onOff)
	{
		hasPierce = onOff;
	}
}
