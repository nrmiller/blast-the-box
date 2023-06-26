package com.millerni456.BlastTheBox;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import miller.opengl.Dimension3d;
import miller.opengl.Point3d;
import miller.opengl.Vector;
import android.os.Handler;

import com.millerni456.BlastTheBox.event.BulletImpactEvent;
import com.millerni456.BlastTheBox.event.BulletListener;
import com.millerni456.BlastTheBox.geometry.Cube;
import com.millerni456.BlastTheBox.geometry.Square;
import com.millerni456.BlastTheBox.utils.CubeStorage;
import com.millerni456.BlastTheBox.utils.Number;
import com.millerni456.BlastTheBox.utils.RenderQueue;
import com.millerni456.BlastTheBox.utils.SoundManager;
import com.millerni456.BlastTheBox.utils.SquareStorage;
import com.millerni456.BlastTheBox.utils.Textures;

public class BulletTracker
{
	public ArrayList<Bullet> bullets = new ArrayList<Bullet>();
	public ArrayList<BulletListener> listeners = new ArrayList<BulletListener>();
	public static final double GRAVITY_RADIUS = 1;
	
	private boolean canFire = true;
	private Handler bulletHandler = new Handler();
	public long waitTime = 300;
	public int ammo = 5;
	public int piercingBullets = 0;
	private boolean hasPierce = false;
	public Square s, s1, s2, s3;
	public Square[] nums = new Square[2];
	public Square[] nums1 = new Square[2];
	
	public BulletTracker()
	{
		
	}
	
	/**
	 * This must be called after squares are created.
	 */
	public void init(int height)
	{
		int size = (int)(height*.125f);
		s = SquareStorage.getSquare();
		s.setScale(new Dimension3d(size, size, 1));//Height * .125
		s.setTexture(Textures.ammo_counter);
		s1 = SquareStorage.getSquare();
		s1.setScale(new Dimension3d(size, size, 1));
		s1.setTexture(Textures.piercing_counter);
		s2 = SquareStorage.getSquare();
		s2.setScale(new Dimension3d(size, size, 1));
		s3 = SquareStorage.getSquare();
		s3.setScale(new Dimension3d(size, size, 1));
		
		int w = (int) (.039*height);
		int h = (int) (.041*height);
		
		for(int i =0; i<nums.length; i++)
		{
			nums[i] = SquareStorage.getSquare();
			nums[i].setScale(new Dimension3d(w, h, 1));
		}
		for(int i = 0; i<nums1.length; i++)
		{
			nums1[i] = SquareStorage.getSquare();
			nums1[i].setScale(new Dimension3d(w, h, 1));
		}
		
	}
	
	public void addListener(BulletListener bl)
	{
		listeners.add(bl);
	}
	public void removeListener(BulletListener bl)
	{
		listeners.remove(bl);
	}
	
	public void addAmmo(int amount)
	{
		ammo += amount;
		if(ammo>99)
		{
			ammo = 99;
		}
	}
	public void addPiercingBullets(int amount)
	{
		piercingBullets += amount;
		if(piercingBullets > 10)
		{
			piercingBullets = 10;
		}
		hasPierce = true;
	}
	
	public void fire(Player player)
	{
		if(canFire && ammo > 0 && bullets.size()<99)
		{
			if(piercingBullets > 0)
			{
				hasPierce = true;
				piercingBullets--;
			}
			else
			{
				//Turn off piercing if there are no piercing bullets.
				hasPierce = false;
			}
			
			SoundManager.playSound(3, 1);
			Bullet b = new Bullet(player);
			b.setPiercing(hasPierce);
			
			bullets.add(b);
			ammo -= 1;
			
			
			
			if(player.hasTripleFire())
			{
				if(ammo>0)
				{
					Bullet b2 = new Bullet(player, new Vector(-.00036397, 0, 0.001), hasPierce);
					b2.setOrientation(-20);
					bullets.add(b2);
					ammo -= 1;
				}
				
				if(ammo>0)
				{
					Bullet b3 = new Bullet(player, new Vector(0.00036397, 0, 0.001), hasPierce);
					b3.setOrientation(20);
					bullets.add(b3);
					ammo -= 1;
				}
				
			}
			
			if(ammo<0)
			{
				ammo = 0;
			}
			
			canFire = false;
			bulletHandler.postDelayed(bulletRunnable, waitTime);
		}
	}
	
	public void update(ArrayList<Row> rows)
	{
		for(int i = bullets.size()-1; i>=0; i--)
		{
			if(bullets.get(i)!=null)
			{
				Bullet b = bullets.get(i);
				b.update();
				for(int k = 0; k<rows.size(); k++)
				{
					Cube[] cubes = rows.get(k).cubes;
					for(int j = 0; j<cubes.length; j++)
					{
						if(b.checkCollision(cubes[j]))
						{
							int strength = b.strength;
							
							if(!cubes[j].isIndestructible())
							{
								cubes[j].doDamage(strength);
							}
							
							BulletImpactEvent be = new BulletImpactEvent(b, cubes[j]);
							for(int l = 0; l<listeners.size(); l++)
							{
								listeners.get(l).onBulletImpact(be);
							}
							
							if(cubes[j].isDead())
							{
								CubeStorage.giveCube(cubes[j]);
								cubes[j] = null;
							}
							
						}
					}
				}
				if(b.canDispose())
				{
					b.clean();
					bullets.remove(i);
				}
			}
		}
	}
	
	public void addToQueue(RenderQueue queue)
	{
		for(int i = 0; i<bullets.size(); i++)
		{
			if(bullets.get(i)!=null)
			{
				Bullet b = bullets.get(i);
				b.addToQueue(queue);
			}
		}
	}
	
	public void renderAmmoCounter(GL10 gl, int width, int height)
	{
		int digits = pickTextures();
		
		int size = (int) (height*.125f);
		int size2 = (int) (height*.1406f);
		int size3 = (int) (.039*height);
		int size4 = (int) (height*.03125);
		int size5 = (int) (height*.0039);
		
		if(s!=null)
		{
			s.setLocation(new Point3d(width-(size*2), height-size2, 0));
			s.draw(gl);
		}
		for(int i = 0; i<digits; i++)
		{
			if(nums[i]!=null)
			{
				nums[i].setLocation(new Point3d(width-(size*2) + size3 + i * size4, height-size2+size5, 0));
				nums[i].draw(gl);
			}
		}
	}
	public void renderPiercingCounter(GL10 gl, int width, int height)
	{
		int digits = pickPierceTextures();
		
		int size = (int) (height*.125f);
		int size2 = (int) (height*.1406f);
		int size3 = (int) (.039*height);
		int size4 = (int) (height*.03125);
		int size5 = (int) (height*.0039);
		
		if(s1!=null)
		{
			s1.setLocation(new Point3d(width-(size*3), height-size2, 0));
			s1.draw(gl);
		}
		for(int i = 0; i<digits; i++)
		{
			if(nums1[i]!=null)
			{
				nums1[i].setLocation(new Point3d(width-(size*3) + size3 + i * size4, height-size2+size5, 0));
				nums1[i].draw(gl);
			}
		}
	}
	
	public void renderPowerups(GL10 gl, Player player, int width, int height)
	{
		int str = player.getStrength();
		
		int size = (int) (height*.125f);
		int size2 = (int) (height*.1406f);
		
		int num_squares = 0;
		if(str > 1)
		{
			num_squares++;
		}
		if(player.hasTripleFire())
		{
			num_squares++;
		}
		
		int[] texes = new int[num_squares];
		int index = 0;
		
		if(str == 2)
		{
			texes[index] = Textures.powerup_strength1;
			index++;
		}
		else if(str == 3)
 		{
			texes[index] = Textures.powerup_strength2;
			index++;
		}
		if(player.hasTripleFire())
		{
			texes[index] = Textures.powerup_triplefire;
			index++;
		}
		
		switch(num_squares)
		{
			case 1:
				s2.setLocation(new Point3d(width-(size*4), height-size2, 0));
				s2.setTexture(texes[0]);
				s2.draw(gl);
				break;
			case 2:
				s2.setLocation(new Point3d(width-(size*4), height-size2, 0));
				s2.setTexture(texes[0]);
				s2.draw(gl);
				
				s3.setTexture(texes[1]);
				s3.setLocation(new Point3d(width-(size*5), height-size2, 0));
				s3.draw(gl);
				break;
			default:
				break;
		}
	}
	
	/**
	 * 
	 * @return returns the number of digits used in the ammo (eight 1 or 2)
	 */
	private int pickTextures()
	{
		String s = String.valueOf(ammo);
		
		for(int i = 0; i<s.length(); i++)
		{
			switch(s.charAt(i))
			{
				case '0':
					nums[i].setTexture(Number.ZERO);
					break;
				case '1':
					nums[i].setTexture(Number.ONE);
					break;
				case '2':
					nums[i].setTexture(Number.TWO);
					break;
				case '3':
					nums[i].setTexture(Number.THREE);
					break;
				case '4':
					nums[i].setTexture(Number.FOUR);
					break;
				case '5':
					nums[i].setTexture(Number.FIVE);
					break;
				case '6':
					nums[i].setTexture(Number.SIX);
					break;
				case '7':
					nums[i].setTexture(Number.SEVEN);
					break;
				case '8':
					nums[i].setTexture(Number.EIGHT);
					break;
				case '9':
					nums[i].setTexture(Number.NINE);
					break;
				default:
					break;
				
			}
		}
		
		return s.length();
	}
	private int pickPierceTextures()
	{
		String s = String.valueOf(piercingBullets);
		
		for(int i = 0; i<s.length(); i++)
		{
			switch(s.charAt(i))
			{
				case '0':
					nums1[i].setTexture(Number.ZERO);
					break;
				case '1':
					nums1[i].setTexture(Number.ONE);
					break;
				case '2':
					nums1[i].setTexture(Number.TWO);
					break;
				case '3':
					nums1[i].setTexture(Number.THREE);
					break;
				case '4':
					nums1[i].setTexture(Number.FOUR);
					break;
				case '5':
					nums1[i].setTexture(Number.FIVE);
					break;
				case '6':
					nums1[i].setTexture(Number.SIX);
					break;
				case '7':
					nums1[i].setTexture(Number.SEVEN);
					break;
				case '8':
					nums1[i].setTexture(Number.EIGHT);
					break;
				case '9':
					nums1[i].setTexture(Number.NINE);
					break;
				default:
					break;
				
			}
		}
		
		return s.length();
	}
	
	public void clean()
	{
		SquareStorage.giveSquare(s);
		s = null;
		SquareStorage.giveSquare(s1);
		s1 = null;
		SquareStorage.giveSquare(s2);
		s2 = null;
		SquareStorage.giveSquare(s3);
		s3 = null;
		
		for(int i = 0; i<nums.length; i++)
		{
			SquareStorage.giveSquare(nums[i]);
			nums[i] = null;
		}
		for(int i = 0; i<nums1.length; i++)
		{
			SquareStorage.giveSquare(nums1[i]);
			nums1[i] = null;
		}
		for(int i = 0; i<bullets.size(); i++)
		{
			bullets.get(i).clean();
		}
		bullets.clear();
	}
	
	public Runnable bulletRunnable = new Runnable()
	{
		public void run()
		{
			canFire = true;
		}
	};

}
