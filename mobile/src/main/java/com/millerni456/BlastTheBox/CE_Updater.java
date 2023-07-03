package com.millerni456.BlastTheBox;

import miller.opengl.Point2d;
import miller.util.TimedThread;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.millerni456.BlastTheBox.event.BackButtonListener;
import com.millerni456.BlastTheBox.io.HighScores;

public class CE_Updater extends TimedThread implements OnTouchListener
{
	public static int STATE = 0;
	public static boolean PERFORM_RENDER = true;
	public Game current_game = null;
	public final BlastTheBoxActivity app;
	public final CE_Renderer renderer;
	public final BackButtonListener keyListener;
	
	public float screen_width, screen_height;
	public boolean screen_selected = false;
	public boolean left = false, right = false, fire = false;
	public Point2d touchXY = new Point2d(0, 0);
	
	public CE_Updater(BlastTheBoxActivity app)
	{
		this.app = app;
		this.renderer = app.renderer;
		
		keyListener = new BackButtonListener(this);
		
		TimedThread touchThread = new TimedThread()
		{
			@Override
			public void update()
			{
				if(screen_selected)
				{
					switch(STATE)
					{
						case OPTIONS:
						{
							CE_Renderer.options.touchScreen(touchXY);
						}
						break;
						case GAME:
						{
							if(current_game!=null && !current_game.gameOver())
							{
								String action = current_game.pauseManager.getAction(renderer.height, touchXY);
								if(action.equalsIgnoreCase("pause_toggle"))
								{
									screen_selected = false;
									return;//return early because game is now paused.
								}
								if(current_game.pauseManager.isPaused())
								{
									if(action.equalsIgnoreCase("exit"))
									{
										current_game.endGame();
									}
									screen_selected = false;
									return;//return early because game is now paused
								}
								
								if(left)
								{//Move left.
									CE_Renderer.glCamera.strafeLeft(.05f * CE_Renderer.options.sensitivity);
									if(CE_Renderer.glCamera.getXPos()<-CubePopulator.FIELD_WIDTH/2)
									{
										CE_Renderer.glCamera.setXPos(-CubePopulator.FIELD_WIDTH/2);
									}
									
								}
								if(right)
								{//Move right.
									CE_Renderer.glCamera.strafeRight(.05f * CE_Renderer.options.sensitivity);
									if(CE_Renderer.glCamera.getXPos()>CubePopulator.FIELD_WIDTH/2)
									{
										CE_Renderer.glCamera.setXPos(CubePopulator.FIELD_WIDTH/2);
									}	
								}
								
								if(fire)
								{
									screen_selected = false;
									current_game.bulletTracker.fire(current_game.player);
								}
							}
						}
						break;
					}
				}
			}
		};
		touchThread.setTargetFPS(120);
		touchThread.start();
	}
	
	@Override
	public void update()
	{
		switch(STATE)
		{
			case MAIN_MENU:
				break;
			case HIGHSCORES:
				break;
			case OPTIONS:
				break;
			case GUIDE:
				break;
			case GAME:
				if(current_game!=null)
				{
					current_game.update();
				}
				if(current_game.gameOver())
				{
					CE_Renderer.gameover_screen.pullDown(current_game.scoreTracker.getScore());
					CE_Renderer.gameover_screen.update();
				}
				break;
			default:
				break;
		}
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent e)
	{
		try
		{
			if(v instanceof GLSurfaceView)
			{
				screen_width = v.getWidth();
				screen_height = v.getHeight();
				
				left = false;
				right = false;
				fire = false;
				
				int a = e.getAction();
				a = a & MotionEvent.ACTION_MASK;
				if(a == 5 || a == 6)
					a = a - 5;
				
				int ptrIdx = 0;
				while(ptrIdx < e.getPointerCount())
				{
					int ptrID = e.getPointerId(ptrIdx);
					float x = e.getX(ptrID);
					
					if(x >= (float).75*screen_width && (a == MotionEvent.ACTION_DOWN || a == MotionEvent.ACTION_MOVE))
					{
						right = true;
					}
					
					if(x <= (float) .25*screen_width && (a == MotionEvent.ACTION_DOWN || a == MotionEvent.ACTION_MOVE))
					{
						left = true;
					}
					
					if(x > (float) .25*screen_width && x < (float) .75*screen_width)
					{
						fire = true;
					}
					
					touchXY.x = e.getX(ptrIdx);
					touchXY.y = e.getY(ptrIdx);
					ptrIdx++;
				}
				
				
				if(a == MotionEvent.ACTION_DOWN)
				{
					screen_selected =true;
					
					switch(STATE)
					{
						case MAIN_MENU:
							if(CE_Renderer.main!=null)
							{
								String action = CE_Renderer.main.getAction(e);
								
								if(action.equalsIgnoreCase("hard"))
								{
									screen_selected = false;
									
									GameSettings settings = new GameSettings();
									settings.game_mode = GameSettings.HARD;
									settings.cube_speed = 1;
									settings.cube_density = .9;
									settings.score_speed = 1;
									settings.indestructible_chance = .06;
									settings.nuke_chance = 0.015;
									settings.strength1_chance = 0.15;
									settings.strength2_chance = 0.10;
									settings.show_controller = CE_Renderer.options.controller_enabled;
									
									current_game = new Game(this, settings);
									current_game.start();
									
									CE_Updater.STATE = CE_Updater.GAME;
								}
								else if(action.equalsIgnoreCase("medium"))
								{
									screen_selected = false;
									
									GameSettings settings = new GameSettings();
									settings.game_mode = GameSettings.MEDIUM;
									settings.cube_speed = .75;
									settings.cube_density = .75;
									settings.score_speed = .75;
									settings.indestructible_chance = .04;
									settings.nuke_chance = 0.0125;
									settings.strength1_chance = 0.12;
									settings.strength2_chance = 0.07;
									settings.show_controller = CE_Renderer.options.controller_enabled;
									
									current_game = new Game(this, settings);
									current_game.start();
									
									CE_Updater.STATE = CE_Updater.GAME;
								}
								else if(action.equalsIgnoreCase("easy"))
								{
									screen_selected = false;
									
									GameSettings settings = new GameSettings();
									settings.show_controller = CE_Renderer.options.controller_enabled;
									
									current_game = new Game(this, settings);
									current_game.start();
									
									CE_Updater.STATE = CE_Updater.GAME;
								}
								else if(action.equalsIgnoreCase("highscores"))
								{
									screen_selected = false;
									CE_Updater.STATE = CE_Updater.HIGHSCORES;
								}
								else if(action.equalsIgnoreCase("options"))
								{
									screen_selected = false;
									CE_Updater.STATE = CE_Updater.OPTIONS;
								}
								else if(action.equalsIgnoreCase("guide"))
								{
									screen_selected = false;
									CE_Updater.STATE = CE_Updater.GUIDE;
								}
								else if(action.equalsIgnoreCase("exit"))
								{
									screen_selected = false;
									app.onPause();
								}
							}
							break;
						case HIGHSCORES:
							if(CE_Renderer.highscores!=null)
							{
								String action = CE_Renderer.highscores.getAction(e);
								if(action.equalsIgnoreCase("back"))
								{
									CE_Updater.STATE = CE_Updater.MAIN_MENU;
									CE_Renderer.highscores.clean();
								}
							}
							break;
						case OPTIONS:
							if(CE_Renderer.options!=null)
							{
								String action = CE_Renderer.options.getAction(e);
								if(action.equalsIgnoreCase("back"))
								{
									CE_Updater.STATE = CE_Updater.MAIN_MENU;
									CE_Renderer.options.clean();
								}
							}
							break;
						case GUIDE:
							if(CE_Renderer.guide!=null)
							{
								String action = CE_Renderer.guide.getAction(e);
								if(action.equalsIgnoreCase("back"))
								{
									CE_Updater.STATE = CE_Updater.MAIN_MENU;
									CE_Renderer.guide.clean();
								}
							}
							break;
						case GAME:
							if(current_game!=null)
							{
								if(current_game.gameOver())
								{
									String action = CE_Renderer.gameover_screen.getAction(e);
									if(action.equalsIgnoreCase("menu"))
									{
										char[] inits_ref = CE_Renderer.gameover_screen.getInitials();
										char[] inits = {inits_ref[0], inits_ref[1], inits_ref[2]};
										int score = current_game.scoreTracker.getScore();
										int mode = current_game.settings.game_mode;
										HighScores.addScore(new Score(inits, score, mode));
										HighScores.writeScores(app);
										current_game.clean();
										CE_Renderer.gameover_screen.clean();
										CE_Renderer.resetClearColor();
										CE_Renderer.resetFogVariables();
										
										CE_Renderer.reset(CE_Renderer.glCamera);
										CE_Updater.STATE = CE_Updater.MAIN_MENU;
									}
								}
							}
							break;
						default:
							break;
					}
				}
			}
		}
		catch(Exception ex)
		{}
		return true;
	}
	
	public static final int GAME = 10;
	public static final int MAIN_MENU = 11;
	public static final int OPTIONS = 12;
	public static final int HIGHSCORES = 13;
	public static final int GUIDE = 14;
}
