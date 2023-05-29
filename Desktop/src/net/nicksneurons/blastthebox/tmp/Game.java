package net.nicksneurons.blastthebox.tmp;

import java.util.ArrayList;
import java.util.Random;

import javax.microedition.khronos.opengles.GL10;

import miller.opengl.Point3d;

import com.millerni456.BlastTheBox.event.BulletImpactEvent;
import com.millerni456.BlastTheBox.event.BulletListener;
import com.millerni456.BlastTheBox.event.PausePlayEvent;
import com.millerni456.BlastTheBox.event.PausePlayListener;
import com.millerni456.BlastTheBox.event.PlayerDamageEvent;
import com.millerni456.BlastTheBox.event.PlayerListener;
import com.millerni456.BlastTheBox.geometry.Cube;
import com.millerni456.BlastTheBox.utils.RenderQueue;
import com.millerni456.BlastTheBox.utils.RouletteWheel;
import com.millerni456.BlastTheBox.utils.S;
import com.millerni456.BlastTheBox.utils.SoundManager;
import com.millerni456.BlastTheBox.utils.SoundPlayer;
import com.millerni456.BlastTheBox.utils.Textures;

public class Game implements BulletListener, PlayerListener, PausePlayListener
{
	private final CE_Updater _updater;
	private boolean started = false;
	private boolean isPaused = false;
	private boolean game_over = false;
	public boolean game_played = false;
	
	private int counter = 0;
	public static ArrayList<Row> rows = new ArrayList<Row>();
	public static RouletteWheel powerups, cube_health, indestructible;
	
	public GameSettings settings;
	public Player player;
	public ScoreTracker scoreTracker;
	public HealthTracker healthTracker;
	public BulletTracker bulletTracker;
	public ExplosionTracker explosionTracker;
	public FlashTracker flashTracker;
	public final Darkener darkener;
	public StopwatchTracker stopwatchTracker;
	public Controller controller;
	public PauseManager pauseManager;
	
	public Scene scene;
	
	public RenderQueue queue;
	
	public Game(CE_Updater updater)
	{
		this(updater, null);
	}
	public Game(CE_Updater updater, GameSettings settings)
	{
		if(settings==null)
		{
			settings = new GameSettings();
			this.settings = settings;
		}
		else
		{
			this.settings = settings;
		}
		queue = new RenderQueue();
		
		powerups = new RouletteWheel();
		powerups.setItems(settings.getPowerups(), settings.getPowerupRarities());
		cube_health = new RouletteWheel();
		cube_health.setItems(settings.getCubeHealths(), settings.getCubeHealthRarities());
		indestructible = new RouletteWheel();
		indestructible.setItems(settings.getIndestructible(), settings.getIndestructibleRarity());

		_updater = updater;
		player = new Player();
		player.setMaximumHealth(10);
		player.setHealth(10);
		player.addListener(this);
		
		scoreTracker = new ScoreTracker();
		healthTracker = new HealthTracker(player);
		bulletTracker = new BulletTracker();
		bulletTracker.init((int)_updater.screen_height);
		bulletTracker.addListener(this);
		explosionTracker = new ExplosionTracker();
		flashTracker = new FlashTracker();
		darkener = new Darkener();
		stopwatchTracker = new StopwatchTracker();
		controller = new Controller();
		scene = new Scene();
		pauseManager = new PauseManager(this);
		pauseManager.setPausePlayListener(this);
	}
	
	/**
	 * Starts the game mode.
	 */
	public void start()
	{
		if(!started)
		{
			started = true;
			SoundPlayer.stopSound();
			SoundPlayer.loopSound(R.raw.loop_one);
			SoundPlayer.getMediaPlayer().setVolume(.4f, .4f);
		}
	}
	/**
	 * First updates the introductory animation, then starts updating cubes.
	 */
	public void update()
	{
		darkener.upate();//Always update the darkener
		flashTracker.update();
		
		if(!started || game_over || pauseManager.isPaused())
		{
			return; //Return early and do not update. Game has not been started or game is over.
		}
		else
		{
			player.location = new Point3d(CE_Renderer.glCamera.getXPos(), CE_Renderer.glCamera.getYPos(), 0);
			
			counter++;
			if(counter >= (1 / ((float)settings.cube_speed/4 * (float) stopwatchTracker.getSpeedModifier())))
			{
				counter = 0;
				scene.moveCloser((float)settings.cube_speed/4 * (float) stopwatchTracker.getSpeedModifier());
				rows.add(CubePopulator.initializeRow(settings));
			}
			for(int i = rows.size()-1; i>=0; i--)
			{//Update the row.
				Row row = rows.get(i);
				row.moveCloser((float)settings.cube_speed/4 * (float) stopwatchTracker.getSpeedModifier());
				if(row.checkCollision(player.location))
				{//If any cube collides with the player, do some damage.
					player.doDamage(1);
					healthTracker.setHealth(player.getHealth());//Update the health tracker.
					healthTracker.setHasShield(player.hasShield());
					
					if(player.isDead())
					{//If the player has no health remaining; display score, clean up, and exit application.
						darkener.darken();
						endGame();
						break;
					}
				}
				int powerup = row.collectedPowerup(player.location);
				if(powerup!=0)
				{//If there was a collected powerup, find out which one.
					if(powerup==Textures.powerup_heart)
					{//Add health then update health tracker.
						SoundManager.playSound(SoundManager.heartbeat, 1);
						player.addHealth(1);
						healthTracker.setHealth(player.getHealth());
					}
					else if(powerup==Textures.powerup_shield)
					{//Turn on the shield of the player.
						SoundManager.playSound(SoundManager.shield, 1);
						player.setShield(true);
						healthTracker.setHasShield(player.hasShield());
					}
					else if(powerup==Textures.powerup_ammo)
					{
						SoundManager.playSound(SoundManager.reload, 1);
						Random r = new Random();
						int amount = r.nextInt(4) + 4;
						bulletTracker.addAmmo(amount);
					}
					else if(powerup==Textures.powerup_stopwatch)
					{
						SoundManager.playSound(SoundManager.stopwatch, 1);
						stopwatchTracker.slowTime();
					}
					else if(powerup==Textures.powerup_triplefire)
					{
						SoundManager.playSound(SoundManager.triple, 1);
						player.setTripleFire(true);
					}
					else if(powerup==Textures.powerup_nuke)
					{
						int numNormals = 0;
						int numGreens = 0;
						int numBlues = 0;
						int numReds = 0;
						SoundManager.playSound(SoundManager.nuclear_explosion, 1);
						for(int k = rows.size()-1; k>0; k--)
						{
							for(int j = 0; j<rows.get(k).cubes.length; j++)
							{
								if(rows.get(k).cubes[j]!=null)
								{
									int t = rows.get(k).cubes[j].type;
									
									if(t==Cube.NORMAL) {numNormals++;}
									else if(t==Cube.GREEN) {numGreens++;}
									else if(t==Cube.BLUE) {numBlues++;}
									else if(t==Cube.INDESTRUCTIBLE) {numReds++;}
								
									Point3d loc = rows.get(k).cubes[j].loc;
									explosionTracker.createExplosion(loc, rows.get(k).cubes[j]);
								}
							}
							rows.get(k).clean();
							CubePopulator.giveRow(rows.get(k));
							rows.remove(k);
						}
						scoreTracker.addBonusScore(numNormals*1000 + numGreens*2500 + numBlues*5000 + numReds*10000, (int)_updater.screen_height);
						break;
					}
					else if(powerup==Textures.powerup_strength1)
					{
						SoundManager.playSound(SoundManager.green_up, 1);
						player.setStrength(2);
					}
					else if(powerup==Textures.powerup_strength2)
					{
						SoundManager.playSound(SoundManager.blue_up, 1);
						player.setStrength(3);
					}
					else if(powerup==Textures.powerup_pierce)
					{
						SoundManager.playSound(SoundManager.pierce, 1);
						Random r = new Random();
						int a = r.nextInt(2) + 2;//either 2 or 3
						bulletTracker.addPiercingBullets(a);
					}
				}
			
				if(row.distance<=-1)
				{//clean and remove the row of cubes once it has left the map.
					row.clean();
					CubePopulator.giveRow(row);
					rows.remove(row);
				}	
			}//End of for-loop. (Update Rows)
			
			explosionTracker.update();
			stopwatchTracker.update();
			bulletTracker.update(rows);
			flashTracker.update();
			scoreTracker.addScore((int)(4 * settings.score_speed));
			scoreTracker.update((int)_updater.screen_height);
		}
	}
	public void renderPerspective(GL10 gl)
	{
		//Always draw the grid in-game.
		scene.render(gl);
		
		for(int i = rows.size()-1; i>=0; i--)
		{
			Row row = rows.get(i);
			row.addToQueue(queue);
		}
	
		gl.glColor4f(1, 1, 1, 1);
		explosionTracker.addToQueue(queue);
		bulletTracker.addToQueue(queue);
		
		queue.render(gl);
		
	}
	
	public void renderOrthographic(GL10 gl, int width, int height)
	{
		flashTracker.render(gl);
		scoreTracker.renderScore(gl, width, height);
		healthTracker.renderHealth(gl, width, height);
		bulletTracker.renderAmmoCounter(gl, width, height);
		bulletTracker.renderPiercingCounter(gl, width, height);
		bulletTracker.renderPowerups(gl, player, width, height);
		controller.render(gl, width, height, settings.show_controller);
		pauseManager.renderPauser(gl, width, height);
		if(!game_over)
		{
			darkener.render(gl, width, height);
			pauseManager.renderExit(gl, width, height);
		}
		else
		{
			pauseManager.renderExit(gl, width, height);
			darkener.render(gl, width, height);
		}
		
		game_played = true;
	}

	public void setPaused(boolean pause)
	{
		isPaused = pause;
	}
	
	public boolean isPaused()
	{
		return isPaused;
	}
	
	public void endGame()
	{
		game_over = true;
		SoundPlayer.stopSound();
		SoundPlayer.loopSound(R.raw.loop_two);
		SoundPlayer.getMediaPlayer().setVolume(.4f, .4f);
	}
	public boolean gameOver()
	{
		return game_over;
	}
	
	public void clean()
	{
		for(int k = rows.size()-1; k>=0; k--)
		{
			rows.get(k).clean();
			CubePopulator.giveRow(rows.get(k));
			rows.remove(k);
		}
		queue.renderables.clear();
		scoreTracker.clean();
		healthTracker.clean();
		bulletTracker.clean();
		flashTracker.clean();
		darkener.clean();
		explosionTracker.clean();
		controller.clean();
		pauseManager.clean();
	}
	
	@Override
	public void onBulletImpact(BulletImpactEvent e)
	{
		SoundManager.playSound(SoundManager.boom, .75f);
		if(e.isDead)
		{
			switch(e.cube.type)
			{
				case Cube.NORMAL:
					scoreTracker.addBonusScore(1000, (int)_updater.screen_height);
					break;
				case Cube.GREEN:
					scoreTracker.addBonusScore(2500, (int)_updater.screen_height);
					break;
				case Cube.BLUE:
					scoreTracker.addBonusScore(5000, (int)_updater.screen_height);
					break;
				default:
					break;
			}
		}
		explosionTracker.createExplosion(e.cubeLocation, e.cube);
	}

	@Override
	public void onPlayerDamage(PlayerDamageEvent e)
	{
		flashTracker.doFlash((int)_updater.screen_width, (int)_updater.screen_height);
		int index = S.ran.nextInt(3);
		SoundManager.playSound(SoundManager.ahh[index], 1);
	}
	
	@Override
	public void onPausePlay(PausePlayEvent e)
	{
		switch(e.getType())
		{
			case PausePlayEvent.PAUSE:
			{
				darkener.darken();
				break;
			}
			case PausePlayEvent.PLAY:
			{
				darkener.lighten();
				break;
			}
			default:
				break;
		}
	}
}
