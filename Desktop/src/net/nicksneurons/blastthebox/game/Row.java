package net.nicksneurons.blastthebox.game;

import miller.opengl.Dimension3d;
import miller.opengl.Point3d;

import net.nicksneurons.blastthebox.graphics.geometry.*;
import net.nicksneurons.blastthebox.utils.*;

public class Row
{
	public Cube[] cubes;
	public Square[] powerups;
	public float distance = 40;
	/**Used for collision detection for powerups.**/
	private double prevDist = 0;
	private GameSettings settings;
	
	public Row()
	{
		
	}
	
	public void init(GameSettings settings)
	{
		this.settings = settings;
		
		distance = 40;
		prevDist = 0;
		
		float c = S.ran.nextFloat() * (float) (CubePopulator.FIELD_WIDTH * CubePopulator.DENSITY * this.settings.cube_density / 100);;
		
		int numCubes = 0;
		if(c>=0 && c < 0.5)
		{
			numCubes = 0;
		}
		else
		{
			numCubes = 1;
		}
		
		boolean[] slotFilled = new boolean[CubePopulator.FIELD_WIDTH];
		int slotsFilled = 0;
		
		while(slotsFilled<numCubes)
		{//Loop until all cubes slots are picked.
			
			int slot = S.ran.nextInt(CubePopulator.FIELD_WIDTH);
			if(!slotFilled[slot])
			{
				slotFilled[slot] = true;
				slotsFilled++;
			}
		}
		
		cubes = new Cube[numCubes];
		powerups = new Square[numCubes];
		int i = 0;
		for(int k = 0; k<slotFilled.length; k++)
		{//instantiate Cube objects.
			int xLoc;
			if(slotFilled[k])	
			{
				xLoc = k - CubePopulator.FIELD_WIDTH/2;
				
				float chance = S.ran.nextFloat();
				if(chance<=.1f)
				{//2% chance of spawning a powerup.
					powerups[i] = SquareStorage.getSquare();
					if(powerups[i]!=null)
					{
//						powerups[i].setScale(new Dimension3d(.5, .5, 1));
//						powerups[i].setLocation(new Point3d(xLoc + .25f, 0, 0));
//						powerups[i].setTexture(pickPowerup());
					}
				}
				else
				{//create a cube
					cubes[i] = CubeStorage.getCube();
//					cubes[i].setScale(new Dimension3d(1, 1, 1));
//					cubes[i].setLocation(new Point3d(xLoc, 0, 0));
					int health = pickHealth();
					boolean ind = pickIndestructible();
					int type = getCubeType(health, ind);
					cubes[i].setType(type);
					cubes[i].setHeath(health);
					cubes[i].setIndestructible(ind);
				}
				i++;
			}
		}
	}
	public void init(int opening_width, int index, boolean first)
	{
		distance = 40;
		prevDist = 0;
		powerups = new Square[0];
		if(CubePopulator.FIELD_WIDTH-opening_width > 0)
		{
			cubes = new Cube[CubePopulator.FIELD_WIDTH];
		}
		else
		{
			opening_width = CubePopulator.FIELD_WIDTH;
			cubes = new Cube[CubePopulator.FIELD_WIDTH];
		}
		
		boolean[] slotFilled = new boolean[CubePopulator.FIELD_WIDTH];
		for(int i = 0; i<slotFilled.length; i++)
		{
			if(i<index || i>index+opening_width)
			{
				if(first)//If this is the first row. Then fill all non-openings.
				{
					slotFilled[i] = true;
				}
				else if(i == index-1 || i == index+opening_width+1)
				{
					slotFilled[i] = true;
				}
				else
				{
					if(S.ran.nextInt(100)>90)
					{//1/10th chance filling an empty space
						slotFilled[i] = true;
					}
					else
					{
						slotFilled[i] = false;
					}
				}
			}
			else if(i>=index && i<=index+opening_width)
			{
				slotFilled[i] = false;
			}
		}
		
		int xLoc = 0;
		for(int i = 0; i<cubes.length; i++)
		{
			if(slotFilled[i])//IF slot filled, place cube
			{
				xLoc = i - CubePopulator.FIELD_WIDTH/2;
				cubes[i] = CubeStorage.getCube();
//				cubes[i].setScale(new Dimension3d(1, 1, 1));
//				cubes[i].setLocation(new Point3d(xLoc, 0, 0));
				int health = pickHealth();
				boolean ind = pickIndestructible();
				int type = getCubeType(health, ind);
				cubes[i].setType(type);
				cubes[i].setHeath(health);
				cubes[i].setIndestructible(ind);
			}
		}
	}
	
	/**
	 * Adds all cubes and powerups in the row to the specified render queue.
	 * Doing so allows them to be sorted when RenderQueue.render(GL10 gl) is called.
	 * @param queue - the queue to add the row's contents onto.
	 */
	public void addToQueue(RenderQueue queue)
	{
		for(int i = 0; i<cubes.length; i++)
		{
			if(cubes[i]!=null)
			{
				queue.addToQueue(cubes[i]);
			}
		}
		if(powerups!=null)
		{
			for(int i = 0; i<powerups.length; i++)
			{
				if(powerups[i]!=null)
				{
//					if(powerups[i].getTexture()!=0)
//					{
//						queue.addToQueue(powerups[i]);
//					}
				}
			}
		}
	}
	
	/**
	 * Moves the row's distance closer by the amount specified.
	 * @param amount - the distance (in 3D world coordinates) the row should be brought
	 * closer in the Z direction.
	 */
	public void moveCloser(float amount)
	{
		distance -= amount;
		for(int i = 0; i<cubes.length; i++)
		{
			if(cubes[i]!=null)
			{
//				cubes[i].setLocation(new Point3d(cubes[i].loc.x, 0, -distance));
			}
		}
		if(powerups!=null)
		{
			for(int i = 0; i<powerups.length; i++)
			{
				if(powerups[i]!=null)
				{
//					powerups[i].setLocation(new Point3d(powerups[i].loc.x, 0, -distance));
				}
			}
		}
	}
	
	/**
	 * Object[] size = 2. First element is boolean, second is Cube.
	 * @param loc
	 * @return
	 */
	public boolean checkCollision(Point3d loc)
	{
		for(int i = 0; i<cubes.length; i++)
		{
			if(cubes[i]!=null)
			{
//				if(loc.x>=cubes[i].loc.x && loc.x<=cubes[i].loc.x + cubes[i].dim.x)
//				{//if x is between start X and end X of cube.
//					if(loc.y>=cubes[i].loc.y && loc.y<=cubes[i].loc.y + cubes[i].dim.y)
//					{//if y is inbetween.
//						if(loc.z>=distance-1 && loc.z<=distance)
//						{//if z is inbetween.
//							//Collided! Do something.
//							CubeStorage.giveCube(cubes[i]);
//							cubes[i] = null;
//							return true;
//						}
//					}
//				}
			}
		}
		
		return false;
	}
	
	/**
	 * Collision Detection method.
	 * Returns 0 if the player did not collide with the powerup.
	 * @return
	 */
	public int collectedPowerup(Point3d loc)
	{
		double d = 0;
		for(int i = 0; i<powerups.length; i++)
		{
			if(powerups[i]!=null)
			{
//				if(loc.x>=powerups[i].loc.x-.5 && loc.x<=powerups[i].loc.x + powerups[i].dim.width+.5)
//				{//if x is between start X and end X of cube.
//					if(loc.y>=powerups[i].loc.y && loc.y<=powerups[i].loc.y + powerups[i].dim.height+.5)
//					{//if y is inbetween.
//						if(loc.z>=distance-.02 && loc.z<=distance)
//						{//if z is inbetween.
//							//Collided! Do something.
//							int tex = powerups[i].getTexture();
//							powerups[i].setTexture(0);
//							SquareStorage.giveSquare(powerups[i]);
//							powerups[i]=null;
//
//							return tex;
//						}
//						else
//						{
//							d = Math.abs(distance - loc.z);
//
//							double difference = Math.abs(d - prevDist);
//							if(Math.round(difference * 10000)/10000 <= .5f && d < 0.5f)
//							{//If one iteration has passed and it is by the player.
//								int tex = powerups[i].getTexture();
//								powerups[i].setTexture(0);
//								SquareStorage.giveSquare(powerups[i]);
//								powerups[i]=null;
//
//								return tex;
//							}
//
//							prevDist = d;
//						}
//					}
//				}
			}
		}
		
		return 0;
	}
	
	/**
	 * This method does all the rarity assignments to powerups, then returns
	 * the powerup's texture id.
	 * @return
	 */
	public int pickPowerup()
	{
		Integer powerup = (Integer) Game.INSTANCE.getPowerups().spin(S.ran);
		if(powerup!=null)
		{
			return powerup;
		}
		else
		{
			return 0;
		}
	}
	public int pickHealth()
	{
		Integer health = (Integer) Game.INSTANCE.getCube_health().spin(S.ran);
		if(health!=null)
		{
			return health;
		}
		else
		{
			return 0;
		}
	}
	public boolean pickIndestructible()
	{
		Integer i = (Integer) Game.INSTANCE.getIndestructible().spin(S.ran);
		boolean b = false;
		if(i!=null && i.equals(Cube.INDESTRUCTIBLE))
		{
			b = true;
		}
		return b;
	}
	
	public int getCubeType(int health, boolean indestructible)
	{
		if(indestructible)
		{
			return Cube.INDESTRUCTIBLE;
		}
		else
		{
			if(health==3)
			{
				return Cube.BLUE;
			}
			else if(health==2)
			{
				return Cube.GREEN;
			}
			else
			{
				return Cube.NORMAL;
			}
		}
		
	}
	
	public void clean()
	{
		for(int i = 0; i<cubes.length; i++)
		{
			CubeStorage.giveCube(cubes[i]);
		}
		for(int i = 0; i<powerups.length; i++)
		{
			if(powerups[i]!=null)
			{
				SquareStorage.giveSquare(powerups[i]);
			}
		}
	}
}
