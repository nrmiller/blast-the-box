package com.millerni456.BlastTheBox.menus;

import javax.microedition.khronos.opengles.GL10;

import miller.opengl.Dimension3d;
import miller.opengl.Point2d;
import miller.opengl.Point3d;
import android.view.MotionEvent;

import com.millerni456.BlastTheBox.geometry.Square;
import com.millerni456.BlastTheBox.utils.Letter;
import com.millerni456.BlastTheBox.utils.SoundManager;
import com.millerni456.BlastTheBox.utils.SquareStorage;
import com.millerni456.BlastTheBox.utils.Textures;

public class MainMenu
{
	public int width, height;
	private Square bg, button1, button2, button3, tm, exit, options, achievements, howtoplay;
	private char[] title_char = {'b', 'l', 'a', 's', 't', 't', 'h', 'e', 'b', 'o', 'x'};
	private Square[] title_text = new Square[11];
	private boolean initialized = false;
	
	/**
	 * Squares must be initialized before this is instantiated.
	 */
	public MainMenu()
	{
	}
	
	public void init(int widthOfScreen, int heightOfScreen)
	{
		width = widthOfScreen;
		height = heightOfScreen;
		
		initialized = true;
		tm = SquareStorage.getSquare();
		bg = SquareStorage.getSquare();
		button1 = SquareStorage.getSquare();
		button2 = SquareStorage.getSquare();
		button3 = SquareStorage.getSquare();
		exit = SquareStorage.getSquare();
		options = SquareStorage.getSquare();
		achievements = SquareStorage.getSquare();
		
		
		bg.setScale(new Dimension3d(widthOfScreen, heightOfScreen, 0));
		bg.setLocation(new Point3d(0, 0, 0));
		bg.setTexture(Textures.menu_main);
	
		int buttonW = (int)(widthOfScreen * .22);
		int buttonH = (int) (heightOfScreen * .3);
		int buttonX = (int)(widthOfScreen - 3*(buttonW)) / 2;
		int buttonY = (int) (heightOfScreen-buttonH) /2;
		button1.setScale(new Dimension3d(buttonW, buttonH, 0));
		button1.setLocation(new Point3d(buttonX, buttonY, 0));
		button1.setTexture(Textures.button_easymode);
		button2.setScale(new Dimension3d(buttonW, buttonH, 0));
		button2.setLocation(new Point3d(buttonX + buttonW + .15, buttonY, 0));
		button2.setTexture(Textures.button_mediummode);
		button3.setScale(new Dimension3d(buttonW, buttonH, 0));
		button3.setLocation(new Point3d(buttonX + buttonW*2 + .3, buttonY, 0));
		button3.setTexture(Textures.button_hardmode);
		
		float base_letterH = .1f*height; //Letter/Number Dimensions
		float base_letterW = base_letterH/1.167f;
		int padding = (int) (height-(buttonY+buttonH) - 2*base_letterH)/2;
		int top_letterX = (int) (width - 5*base_letterW)/2;
		int top_letterY = (int) (height-padding-base_letterH);
		int bottom_letterX = (int) (width - 7*base_letterW)/2;
		int bottom_letterY = (int) (top_letterY-base_letterH);
		
		for(int i = 0; i<title_text.length; i++)
		{
			title_text[i] = SquareStorage.getSquare();
			title_text[i].setScale(new Dimension3d(base_letterW, base_letterH, 0));
			title_text[i].setTexture(Letter.getLetter(title_char[i]));
		}
		for(int i = 0; i<5; i++)
		{
			title_text[i].setLocation(new Point3d(top_letterX + i*base_letterW, top_letterY, 0));
		}
		for(int i = 0; i<3; i++)
		{
			title_text[5+i].setLocation(new Point3d(bottom_letterX + i*base_letterW, bottom_letterY, 0));
			title_text[8+i].setLocation(new Point3d(bottom_letterX + (i+4)*base_letterW, bottom_letterY, 0));
		}
		
		int TM_length = (int) (heightOfScreen * .0625);
		int TM_x = (int) (title_text[10].loc.x + title_text[10].scale.getWidth());
		int TM_y = (int) (title_text[10].loc.y + title_text[10].scale.getHeight() - TM_length);
		tm.setScale(new Dimension3d(TM_length, TM_length, 0));
		tm.setLocation(new Point3d(TM_x, TM_y, 0));
		tm.setTexture(Textures.text_TM);
		
		int exitH = (int) (heightOfScreen * .125);
		int exitW = exitH;
		exit.setScale(new Dimension3d(exitW, exitH,  0));
		exit.setLocation(new Point3d(widthOfScreen-exitW, heightOfScreen-exitH, 0));
		exit.setTexture(Textures.button_exit);
		
		int tabH = (int) (heightOfScreen * .15);
		int tabW = tabH;
		
		options.setScale(new Dimension3d(tabW, tabH, 0));
		options.setLocation(new Point3d(widthOfScreen-tabW, 0, 0));
		options.setTexture(Textures.button_options);
		
		achievements.setScale(new Dimension3d(tabW, tabH, 0));
		achievements.setLocation(new Point3d(widthOfScreen-tabW, tabH, 0));
		achievements.setTexture(Textures.button_achievements);
		
		float howtoplayW = width*.2f;
		float howtoplayX = (width - howtoplayW)/2;
		float howtoplayY = (buttonY - howtoplayW/2)/2;
		howtoplay = SquareStorage.getSquare();
		howtoplay.setTexture(Textures.button_howtoplay);
		howtoplay.setLocation(new Point3d(howtoplayX, howtoplayY, 0));
		howtoplay.setScale(new Dimension3d(howtoplayW, howtoplayW/2, 0));
	}
	/**
	 * Renders the main screen menu. This function assumes that the projection is orthogonal.
	 * @param gl - OpenGL context handle.
	 * @param widthOfScreen - width of Droid device
	 * @param heightOfScreen - height of Droid devie
	 */
	public void render(GL10 gl, int widthOfScreen, int heightOfScreen)
	{
		if(!initialized)
		{
			init(widthOfScreen, heightOfScreen);
		}
		gl.glDisable(GL10.GL_BLEND);
		bg.draw(gl);
		exit.draw(gl);
		gl.glEnable(GL10.GL_BLEND);
		for(int i = 0; i<title_text.length; i++)
		{
			title_text[i].draw(gl);
		}
		tm.draw(gl);
		button1.draw(gl);
		button2.draw(gl);
		button3.draw(gl);
		options.draw(gl);
		achievements.draw(gl);
		howtoplay.draw(gl);
		
	}
	
	public String getAction(MotionEvent e)
	{
		String action = "";
		Point2d p = new Point2d(e.getX(), height-e.getY());
		
		if(!initialized) return action;
		if(button1.isTouched(p))
		{//EASY MODE
			action = "easy";
			SoundManager.playSound(SoundManager.click, 1);
		}
		else if(button2.isTouched(p))
		{//MEDIUM MODE
			action = "medium";
			SoundManager.playSound(SoundManager.click, 1);
		}
		else if(button3.isTouched(p))
		{//HARD MODE
			action = "hard";
			SoundManager.playSound(SoundManager.click, 1);
		}
		else if(exit.isTouched(p))
		{//EXIT
			action = "exit";
		}
		else if(achievements.isTouched(p))
		{//HIGHSCORES
			action = "highscores";
			SoundManager.playSound(SoundManager.click, 1);
		}
		else if(options.isTouched(p))
		{//OPTIONS
			action = "options";
			SoundManager.playSound(SoundManager.click, 1);
		}
		else if(howtoplay.isTouched(p))
		{//GUIDE
			action = "guide";
			SoundManager.playSound(SoundManager.click, 1);
		}
		return action;
	}
	
	public void clean()
	{
		SquareStorage.giveSquare(tm);
		SquareStorage.giveSquare(bg);
		SquareStorage.giveSquare(button1);
		SquareStorage.giveSquare(button2);
		SquareStorage.giveSquare(button3);
		SquareStorage.giveSquare(exit);
		SquareStorage.giveSquare(options);
		SquareStorage.giveSquare(achievements);
		SquareStorage.giveSquare(howtoplay);
		
		for(int i = 0; i<title_text.length; i++)
		{
			SquareStorage.giveSquare(title_text[i]);
			title_text[i] = null;
		}
		
		tm = null;
		bg = null;
		button1 = null;
		button2 = null;
		button3 = null;
		exit = null;
		options = null;
		achievements = null;
		howtoplay = null;
		initialized = false;
	}
}
