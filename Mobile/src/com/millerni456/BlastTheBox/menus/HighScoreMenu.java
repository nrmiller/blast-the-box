package com.millerni456.BlastTheBox.menus;

import javax.microedition.khronos.opengles.GL10;

import miller.opengl.Dimension3d;
import miller.opengl.Point2d;
import miller.opengl.Point3d;
import android.view.MotionEvent;

import com.millerni456.BlastTheBox.GameSettings;
import com.millerni456.BlastTheBox.Score;
import com.millerni456.BlastTheBox.geometry.Rectangle;
import com.millerni456.BlastTheBox.geometry.Square;
import com.millerni456.BlastTheBox.io.HighScores;
import com.millerni456.BlastTheBox.utils.Letter;
import com.millerni456.BlastTheBox.utils.Number;
import com.millerni456.BlastTheBox.utils.SoundManager;
import com.millerni456.BlastTheBox.utils.SquareStorage;
import com.millerni456.BlastTheBox.utils.Textures;

public class HighScoreMenu
{
	public int width, height;
	public static int DISPLAY_MODE = GameSettings.EASY;
	public Rectangle scorebox = new Rectangle(0, 0, 0, 0);
	private Square bg, back_button, easy_button, medium_button, hard_button;
	private char[] title_char = {'h', 'i', 'g', 'h', 's', 'c', 'o', 'r', 'e', 's'};
	private Square[] title = new Square[10]; //10 letters in highscores
	private Square[] points; //Size of this array is determined by the individual scores' digit lengths.
	private Square[] place = new Square[9]; //Places 1-9
	private Square[] initials = new Square[3*9]; //3 Chars per initial, 9 initals.
	private boolean initialized = false, scores_initialized = false;
	
	private Score[] scores = new Score[9];
	
	/**
	 * Squares must be initialized before this is instantiated.
	 */
	public HighScoreMenu()
	{
		
	}
	
	public void init(int widthOfScreen, int heightOfScreen)
	{
		width = widthOfScreen;
		height = heightOfScreen;
		float base_letterH = .1f*height; //Letter/Number Dimensions
		float base_letterW = base_letterH/1.167f;
		
		initialized = true; //Prevent continuous initializations
		
		bg = SquareStorage.getSquare();
		back_button = SquareStorage.getSquare();
		easy_button = SquareStorage.getSquare();
		medium_button = SquareStorage.getSquare();
		hard_button = SquareStorage.getSquare();
		
		bg.setScale(new Dimension3d(width, height, 0));
		bg.setLocation(new Point3d(0, 0, 0));
		bg.setTexture(Textures.menu_highscores);
		
		int backH = (int) (heightOfScreen * .125);
		int backW = backH;
		back_button.setScale(new Dimension3d(backW, backH, 0));
		back_button.setLocation(new Point3d(0, height-backH, 0));
		back_button.setTexture(Textures.button_back);
		
		//SCORE BOX 
		int scoreboxW = (int) (.8 * width);
		int scoreboxH = (int) (.7 * height);
		int scoreboxX = (int) (.1 * width);
		int scoreboxY = (int) (.05 * height);
		scorebox.setLocation(scoreboxX, scoreboxY);
		scorebox.setSize(scoreboxW, scoreboxH);
		
		int button_H = (int) (scoreboxH/3);
		easy_button.setLocation(new Point3d(0, scoreboxY + button_H*2, 0));
		easy_button.setScale(new Dimension3d(button_H, button_H, 0));
		
		medium_button.setLocation(new Point3d(0, scoreboxY + button_H, 0));
		medium_button.setScale(new Dimension3d(button_H, button_H, 0));
		
		hard_button.setLocation(new Point3d(0, scoreboxY, 0));
		hard_button.setScale(new Dimension3d(button_H, button_H, 0));
		
		if(!scores_initialized)
		{
			initializeScores(scoreboxX, scoreboxY, scoreboxW, scoreboxH);
		}
		
		int base_titleX = (int) (width - title.length*base_letterW) / 2;
		int base_titleH = (height - (scoreboxY+scoreboxH));
		int base_titleY = (int) ((base_titleH - base_letterH) /2) + scoreboxY + scoreboxH;
		for(int i = 0; i<title.length; i++)
		{
			title[i] = SquareStorage.getSquare();
			title[i].setLocation(new Point3d(base_titleX + i*base_letterW, base_titleY, 0));
			title[i].setScale(new Dimension3d(base_letterW, base_letterH, 0));
			title[i].setTexture(Letter.getLetter(title_char[i]));
		}
	}
	
	public void initializeScores(int scoreboxX, int scoreboxY, int scoreboxW, int scoreboxH)
	{
		scores_initialized = true;
		
		switch(DISPLAY_MODE)//Retrieve the selected scores for displaying.
		{
			case GameSettings.EASY:
			{
				scores = HighScores.scoresE;
			}
			break;
			case GameSettings.MEDIUM:
			{
				scores = HighScores.scoresM;
			}
			break;
			case GameSettings.HARD:
			{
				scores = HighScores.scoresH;
			}
			break;
			default:
				break;
		}
		
		scores = HighScores.sort(scores);//Sort the scores by point-order.
		
		int txtH = scoreboxH / 9;
		int txtW = (int)(txtH /1.167);
		
		int total_digits = getDigits(scores);
		points = new Square[total_digits];
		int passed = 0; //Keeps track of the indices in the points array.
		for(int i = 0; i<scores.length; i++)//9 Scores
		{
			int score_y = (int) (scoreboxY+scoreboxH - txtH - i*txtH);
			String s = String.valueOf(scores[i].score);
			char[] inits = scores[i].initials;
			int digits = s.length();
			
			place[i] = SquareStorage.getSquare();
			place[i].setLocation(new Point3d(scoreboxX + txtW, score_y, 0));
			place[i].setScale(new Dimension3d(txtW, txtH, 0));
			place[i].setTexture(Number.getYellowNumberTexture(i+1));
			
			for(int h = 0; h<inits.length; h++)
			{//Initials
				initials[i*3 + h] = SquareStorage.getSquare();
				initials[i*3 + h].setLocation(new Point3d(scoreboxX + txtW*(3+h), score_y, 0));
				initials[i*3 + h].setScale(new Dimension3d(txtW, txtH, 0));
				initials[i*3 + h].setTexture(Letter.getLetter(inits[h]));
			}
			
			for(int k = 0; k<digits; k++)
			{
				int score_x = (int) (scoreboxX+scoreboxW + txtW - (digits - k)*txtW);
				points[passed + k] = SquareStorage.getSquare();
				points[passed + k].setLocation(new Point3d(score_x, score_y, 0));
				points[passed + k].setScale(new Dimension3d(txtW, txtH, 0));
				points[passed + k].setTexture(Number.getNumberTextureFromChar(s.charAt(k)));
			}
			passed += digits;
		}
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
		back_button.draw(gl);
		gl.glEnable(GL10.GL_BLEND);

		switch(DISPLAY_MODE)//Retrieve the selected scores for displaying.
		{
			case GameSettings.EASY:
			{
				easy_button.setTexture(Textures.button_easy_on);
				medium_button.setTexture(Textures.button_medium_off);
				hard_button.setTexture(Textures.button_hard_off);
			}
			break;
			case GameSettings.MEDIUM:
			{
				easy_button.setTexture(Textures.button_easy_off);
				medium_button.setTexture(Textures.button_medium_on);
				hard_button.setTexture(Textures.button_hard_off);
			}
			break;
			case GameSettings.HARD:
			{
				easy_button.setTexture(Textures.button_easy_off);
				medium_button.setTexture(Textures.button_medium_off);
				hard_button.setTexture(Textures.button_hard_on);
			}
			break;
			default:
				break;
		}
		
		easy_button.draw(gl);
		medium_button.draw(gl);
		hard_button.draw(gl);
		
		for(int i = 0; i<points.length; i++)
		{
			points[i].draw(gl);
		}
		for(int i = 0; i<place.length; i++)
		{
			place[i].draw(gl);
		}
		for(int i = 0; i<initials.length; i++)
		{
			initials[i].draw(gl);
		}
		for(int i = 0; i<title.length; i++)
		{
			title[i].draw(gl);
		}
	}
	
	public String getAction(MotionEvent e)
	{
		String action = "";
		Point2d p = new Point2d(e.getX(), height-e.getY());
		
		if(!initialized) return action;
		
		if(back_button.isTouched(p))
		{//BACK
			action = "back";
			SoundManager.playSound(SoundManager.click, 1);
		}
		else if(easy_button.isTouched(p))
		{
			if(DISPLAY_MODE != GameSettings.EASY)
			{
				DISPLAY_MODE = GameSettings.EASY;
				cleanScores();
				initializeScores(scorebox.x, scorebox.y, scorebox.width, scorebox.height);
				SoundManager.playSound(SoundManager.click2, 1);
			}
		}
		else if(medium_button.isTouched(p))
		{
			if(DISPLAY_MODE != GameSettings.MEDIUM)
			{
				DISPLAY_MODE = GameSettings.MEDIUM;
				cleanScores();
				initializeScores(scorebox.x, scorebox.y, scorebox.width, scorebox.height);
				SoundManager.playSound(SoundManager.click2, 1);
			}
		}
		else if(hard_button.isTouched(p))
		{
			if(DISPLAY_MODE != GameSettings.HARD)
			{
				DISPLAY_MODE = GameSettings.HARD;
				cleanScores();
				initializeScores(scorebox.x, scorebox.y, scorebox.width, scorebox.height);
				SoundManager.playSound(SoundManager.click2, 1);
			}
		}
		
		
		return action;
	}
	
	public void clean()
	{
		SquareStorage.giveSquare(bg);
		SquareStorage.giveSquare(back_button);
		SquareStorage.giveSquare(easy_button);
		SquareStorage.giveSquare(medium_button);
		SquareStorage.giveSquare(hard_button);
		
		for(int i = 0; i<title.length; i++)
		{
			SquareStorage.giveSquare(title[i]);
			title[i] = null;
		}
		
		bg = null;
		back_button = null;
		
		cleanScores();
		
		initialized = false;
	}
	public void cleanScores()
	{
		for(int i = 0; i<points.length; i++)
		{
			SquareStorage.giveSquare(points[i]);
			points[i] = null;
		}
		points = null;
		
		for(int i = 0; i<place.length; i++)
		{
			SquareStorage.giveSquare(place[i]);
			place[i] = null;
		}
		for(int i = 0; i<initials.length; i++)
		{
			SquareStorage.giveSquare(initials[i]);
			initials[i] = null;
		}
		
		scores_initialized = false;
	}
	
	/**
	 * Given the scores parameter, this method looks at all individual score values and adds up the
	 * total number of digits.
	 * @param scores - scores array.
	 * @return the number of total digits inside the array
	 */
	private int getDigits(Score[] scores)
	{
		int digits = 0;
		for(int i = 0; i<scores.length; i++)
		{
			String s = String.valueOf(scores[i].score);
			digits += s.length();
		}
		
		return digits;
	}
}
