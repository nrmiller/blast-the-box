package net.nicksneurons.blastthebox.tmp;

import javax.microedition.khronos.opengles.GL10;

import miller.opengl.Dimension3d;
import miller.opengl.Point2d;
import miller.opengl.Point3d;

import com.millerni456.BlastTheBox.event.PausePlayEvent;
import com.millerni456.BlastTheBox.event.PausePlayListener;
import com.millerni456.BlastTheBox.geometry.Square;
import com.millerni456.BlastTheBox.utils.SoundManager;
import com.millerni456.BlastTheBox.utils.SquareStorage;
import com.millerni456.BlastTheBox.utils.Textures;


public class PauseManager
{
	private boolean initialized = false;
	private boolean isPaused = false;
	private Square s, exit;
	public PausePlayListener listener;
	public Game current_game;
	
	public PauseManager(Game current_game)
	{
		this.current_game = current_game;
		s = SquareStorage.getSquare();
		exit = SquareStorage.getSquare();
		resume();
	}
	
	public void setPausePlayListener(PausePlayListener listener)
	{
		this.listener = listener;
	}
	
	private void init(GL10 gl, int width, int height)
	{
		initialized = true;
		s.setScale(new Dimension3d(height * .125, height * .125, 0));
		s.setLocation(new Point3d(0, height * .875, 0));
		
		float exitW = width*.2f;
		float exitX = (width - exitW)/2;
		float exitY = (((height-(height * .3f))/2) - exitW/2)/2;
		exit.setTexture(Textures.button_pause_exit);
		exit.setScale(new Dimension3d(exitW, exitW/2, 0));
		exit.setLocation(new Point3d(exitX, exitY, 0));
	}
	
	public void pause()
	{
		isPaused = true;
	}
	public void resume()
	{
		isPaused = false;
	}
	
	/**
	 * Draws the pause/play button
	 * This method assumes the OpenGL context is in orthographic rendering mode.
	 * @param gl - context handle
	 * @param width - width of screen
	 * @param height - height of screen
	 */
	public void renderPauser(GL10 gl, int width, int height)
	{
		if(!initialized)
		{
			init(gl, width, height);
		}
		if(isPaused)
		{
			s.setTexture(Textures.button_play);
		}
		else
		{
			s.setTexture(Textures.button_pause);
		}
		s.draw(gl);
	}
	public void renderExit(GL10 gl, int width, int height)
	{
		if(!initialized)
		{
			init(gl, width, height);
		}
		if(isPaused)
		{
			exit.draw(gl);
		}
	}
	
	public String getAction(int height, Point2d loc)
	{
		if(!current_game.game_played)
		{
			return "";
		}
		String a = "";
		Point2d p = new Point2d(loc.getX(), height-loc.getY());
		
		if(s.isTouched(p))
		{
			if(isPaused)
			{
				resume();
				if(listener!=null)
				{
					listener.onPausePlay(new PausePlayEvent(PausePlayEvent.PLAY));
				}
			}
			else
			{
				pause();
				if(listener!=null)
				{
					listener.onPausePlay(new PausePlayEvent(PausePlayEvent.PAUSE));
				}
			}
			a = "pause_toggle";
		}
		else if(exit.isTouched(p))
		{
			a = "exit";
			if(isPaused)
			{
				SoundManager.playSound(SoundManager.click, 1);
			}
		}
		return a;
	}
	
	/**
	 * Programmatically pauses the game. Messages are still sent to the game object, so that
	 * the screen can fade in/out.  This function should be used when the back button is pressed, or
	 * when the game is interrupted by other applications.
	 */
	public void tapPauseButton()
	{
		if(!current_game.game_played)
		{
			return;
		}
		
		if(isPaused)
		{
			resume();
			if(listener!=null)
			{
				listener.onPausePlay(new PausePlayEvent(PausePlayEvent.PLAY));
			}
		}
		else
		{
			pause();
			if(listener!=null)
			{
				listener.onPausePlay(new PausePlayEvent(PausePlayEvent.PAUSE));
			}
		}
	}
	public boolean isPaused()
	{
		return isPaused;
	}
	
	public void clean()
	{
		SquareStorage.giveSquare(s);
		SquareStorage.giveSquare(exit);
		s = null;
		exit = null;
	}
}
