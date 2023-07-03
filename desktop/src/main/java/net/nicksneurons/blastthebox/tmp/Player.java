package net.nicksneurons.blastthebox.tmp;

import java.util.ArrayList;

import com.millerni456.BlastTheBox.event.PlayerDamageEvent;
import com.millerni456.BlastTheBox.event.PlayerListener;
import com.millerni456.BlastTheBox.utils.SoundManager;

import android.os.Handler;
import miller.opengl.Point3d;

public class Player
{
	public ArrayList<PlayerListener> listeners = new ArrayList<PlayerListener>();
	public Point3d location = new Point3d(0, 0, 0);
	public int health = 1;
	public int maximumHealth = 1;
	public int strength = 1;
	public boolean isDead = false;
	public boolean hasShield = false;
	public boolean hasTripleFire = false;
	public boolean homingBullets = false;
	private boolean invinceable = false;
	private Handler handler = new Handler();
	
	public Player()
	{
		isDead = false;
		hasShield = false;
		homingBullets = false;
		strength = 1;
	}
	
	public int getHealth()
	{
		return health;
	}
	public void setHealth(int health)
	{
		this.health = health;
		if(health<=0)
		{
			health = 0;
			isDead = true;
		}
	}
	public void setMaximumHealth(int max)
	{
		maximumHealth = max;
	}
	public int getMaximumHealth()
	{
		return maximumHealth;
	}
	public void doDamage(int amount)
	{
		if(!invinceable)
		{
			if(hasShield)
			{
				setShield(false);
				SoundManager.playSound(SoundManager.shield_off, 1);
			}
			else
			{
				health -= amount;
				strength = 1;
				PlayerDamageEvent pde = new PlayerDamageEvent(this);
				for(int i = 0; i<listeners.size(); i++)
				{
					listeners.get(i).onPlayerDamage(pde);
				}
				
				if(hasTripleFire)
				{
					setTripleFire(false);
				}
				
				if(health<=0)
				{
					health = 0;
					isDead = true;
				}
				else
				{//need to recover
					invinceable = true;
					handler.postDelayed(recover, 3000);
				}
			}
		}
	}

	public void addHealth(int amount)
	{
		health+=amount;
		if(health>=maximumHealth)
		{
			health = maximumHealth;
		}
	}
	
	public void setStrength(int strength)
	{
		this.strength = strength;
	}
	public int getStrength()
	{
		return strength;
	}
	
	public boolean isDead()
	{
		return isDead;
	}
	
	public void setShield(boolean onOff)
	{
		hasShield = onOff;
	}
	public boolean hasShield()
	{
		return hasShield;
	}
	
	public void startHomingBullets()
	{
		homingBullets = true;
		handler.postDelayed(homingOff, 20000);//20 Seconds homing bulletes
	}
	
	public boolean hasTripleFire()
	{
		return hasTripleFire;
	}
	public void setTripleFire(boolean onOff)
	{
		hasTripleFire = onOff;
	}
	
	public void addListener(PlayerListener pl)
	{
		listeners.add(pl);
	}
	public void removeListener(PlayerListener pl)
	{
		listeners.remove(pl);
	}
	
	private Runnable recover = new Runnable()
	{
		public void run()
		{
			invinceable = false;
		}
	};
	private Runnable homingOff = new Runnable()
	{
		public void run()
		{
			homingBullets = false;
		}
	};
}
