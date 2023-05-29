package net.nicksneurons.blastthebox.tmp.menus;

import javax.microedition.khronos.opengles.GL10;

import miller.opengl.Dimension3d;
import miller.opengl.Point2d;
import miller.opengl.Point3d;
import android.view.MotionEvent;

import com.millerni456.BlastTheBox.geometry.Square;
import com.millerni456.BlastTheBox.utils.Letter;
import com.millerni456.BlastTheBox.utils.Number;
import com.millerni456.BlastTheBox.utils.SoundManager;
import com.millerni456.BlastTheBox.utils.SquareStorage;
import com.millerni456.BlastTheBox.utils.Textures;

public class GameOverScreen
{
	private Square[] initials = new Square[3];
	private Square[] up_arrows = new Square[3];
	private Square[] down_arrows = new Square[3];
	private Square[] score_squares = new Square[15];
	private Square[] letters = new Square[11];
	private Square button_continue;
	
	private int width, height;
	private boolean initialized = false;
	
	private int score = 0;
	
	private boolean pulled_down = false;
	private boolean canUpdate = false;
	private float height_mod;
	
	private int counter = 0;
	
	private char[] name = {'a', 'a', 'a'};
	private char[] title = {'f', 'i', 'n', 'a', 'l', ' ', 's', 'c', 'o', 'r', 'e'};
	
	/**
	 * Must be instantiated after SquareStorage is filled.
	 */
	public GameOverScreen()
	{
		
	}
	
	private void init(int width, int height)
	{
		if(!initialized)
		{
			this.width = width;
			this.height = height;
			
			pulled_down = false;
			counter = 0;
			height_mod = (float)height / 100;
			
			button_continue = SquareStorage.getSquare();
			for(int i = 0; i<3; i++)
			{
				initials[i] = SquareStorage.getSquare();
				initials[i].setTexture(Letter.getLetter(name[i]));
				up_arrows[i] = SquareStorage.getSquare();
				up_arrows[i].setTexture(Textures.arrow_up);
				down_arrows[i] = SquareStorage.getSquare();
				down_arrows[i].setTexture(Textures.arrow_down);
			}
			for(int i = 0; i<score_squares.length; i++)
			{
				score_squares[i] = SquareStorage.getSquare();
			}
			for(int i = 0; i<letters.length; i++)
			{
				letters[i] = SquareStorage.getSquare();
			}
			
			float sf = .125f*height;
			float baseX = (width - sf*3)/2;
			float baseY = (.08f*height);
			for(int i = 0; i<3; i++)
			{
				down_arrows[i].setScale(new Dimension3d(sf, sf, 0));
				down_arrows[i].setLocation(new Point3d(baseX + i*sf, height+baseY, 0));
				initials[i].setScale(new Dimension3d(sf, sf, 0));
				initials[i].setLocation(new Point3d(baseX + i*sf, height+baseY + sf, 0));
				up_arrows[i].setScale(new Dimension3d(sf, sf, 0));
				up_arrows[i].setLocation(new Point3d(baseX + i*sf, height+baseY + sf*2, 0));
			}
			
			float contW = width*.2f;
			float subWidth = (width - (baseX + sf*2));
			float subHeight = (sf*3);
			float contX = (subWidth - contW)/2 + (baseX+sf*2);
			float contY = (subHeight - (contW/2))/2 + baseY;
			button_continue.setLocation(new Point3d(contX, height+contY, 0));
			button_continue.setScale(new Dimension3d(contW, contW/2, 0));
			button_continue.setTexture(Textures.button_continue);
			
			float base_letterH = .1f*height;
			float base_letterW = base_letterH/1.167f;
			float base_scoreY = (0.7f*height);
			for(int i = 0; i<score_squares.length; i++)
			{
				//X cannot be determined until a game over.
				score_squares[i].setLocation(new Point3d(0, height+base_scoreY, 0));
				score_squares[i].setScale(new Dimension3d(base_letterW, base_letterH, 0));
			}
			
			float base_letterX = (width - 11*base_letterW)/2;
			float base_letterY = (0.8f*height);
			for(int i = 0; i<letters.length; i++)
			{
				letters[i].setLocation(new Point3d(base_letterX + i*base_letterW, height + base_letterY, 0));
				letters[i].setScale(new Dimension3d(base_letterW, base_letterH, 0));
				letters[i].setTexture(Letter.getLetter(title[i]));
			}
			initialized = true;
		}
	}
	
	public void pullDown(int score)
	{
		this.score = score;
		canUpdate = true;
		if(!pulled_down)
		{
			pulled_down = true;
		}
	}
	
	public synchronized void update()
	{
		if(canUpdate && initialized)
		{
			if(pulled_down && counter < 100)
			{
				for(int i = 0; i<3; i++)
				{
					//Decrement the height of all squares.
					Point3d loc = initials[i].getLocation();
					initials[i].setLocation(new Point3d(loc.x, loc.y - height_mod, 0));
					Point3d loc2 = up_arrows[i].getLocation();
					up_arrows[i].setLocation(new Point3d(loc2.x, loc2.y - height_mod, 0));
					Point3d loc3 = down_arrows[i].getLocation();
					down_arrows[i].setLocation(new Point3d(loc3.x, loc3.y - height_mod, 0));
				}
				Point3d loc5 = button_continue.getLocation();
				button_continue.setLocation(new Point3d(loc5.x, loc5.y - height_mod, 0));
				
				float base_letterH = .1f*height;
				float base_letterW = base_letterH/1.167f;
				for(int i = 0; i<score_squares.length; i++)
				{
					Point3d loc6 = score_squares[i].getLocation();
					int digits = String.valueOf(score).length();
					float base_scoreX = (width - 48*digits)/2;
					score_squares[i].setLocation(new Point3d(base_scoreX + i*base_letterW, loc6.y - height_mod, 0));
				}
				
				for(int i = 0; i<letters.length; i++)
				{
					Point3d loc7 = letters[i].getLocation();
					letters[i].setLocation(new Point3d(loc7.x, loc7.y - height_mod, 0));
				}
				
				counter++;
			}
			//Do whether menu is being pulled down or not.
			for(int i = 0; i<3; i++)
			{
				//Update the texture of the initials.
				initials[i].setTexture(Letter.getLetter(name[i]));
			}
			pickTextures();
		}
	}
	
	public void render(GL10 gl, int width, int height)
	{	
		init(width, height);
		//Draw background panel.
		
		for(int i = 0; i<3; i++)
		{//Draw initials and arrows.
			initials[i].draw(gl);
			up_arrows[i].draw(gl);
			down_arrows[i].draw(gl);
		}
		int digits = String.valueOf(score).length();
		if(digits <= score_squares.length)
		{//Draw the score.
			for(int i = 0; i<digits; i++)
			{
				score_squares[i].draw(gl);
			}
		}
		for(int i = 0; i<letters.length; i++)
		{//Draw "FINAL SCORE"
			letters[i].draw(gl);
		}
		
		button_continue.draw(gl);
	}
	
	public char incrementCharacter(char start)
	{
		if(start == 'z')
		{
			start = 'a';
		}
		else
		{
			start++;
		}
		return start;
	}
	
	public char decrementCharacter(char start)
	{
		if(start == 'a')
		{
			start = 'z';
		}
		else
		{
			start--;
		}
		return start;
	}
	
	public String getAction(MotionEvent e)
	{
		String action = "";
		Point2d p = new Point2d(e.getX(), height-e.getY());
		if(!initialized) return action;
		
		for(int i = 0; i<3; i++)
		{
			if(up_arrows[i].isTouched(p))
			{
				name[i] = incrementCharacter(name[i]);
				SoundManager.playSound(SoundManager.click2, 1);
			}
			else if(down_arrows[i].isTouched(p))
			{
				name[i] = decrementCharacter(name[i]);
				SoundManager.playSound(SoundManager.click2, 1);
			}
		}
		
		if(button_continue.isTouched(p))
		{
			SoundManager.playSound(SoundManager.click, 1);
			return "menu";
		}
		
		return action;
	}
	
	public char[] getInitials()
	{
		return name;
	}
	
	private void pickTextures()
	{
		String s = String.valueOf(score);
		
		for(int i = 0; i<s.length(); i++)
		{
			switch(s.charAt(i))
			{
				case '0':
					score_squares[i].setTexture(Number.ZERO);
					break;
				case '1':
					score_squares[i].setTexture(Number.ONE);
					break;
				case '2':
					score_squares[i].setTexture(Number.TWO);
					break;
				case '3':
					score_squares[i].setTexture(Number.THREE);
					break;
				case '4':
					score_squares[i].setTexture(Number.FOUR);
					break;
				case '5':
					score_squares[i].setTexture(Number.FIVE);
					break;
				case '6':
					score_squares[i].setTexture(Number.SIX);
					break;
				case '7':
					score_squares[i].setTexture(Number.SEVEN);
					break;
				case '8':
					score_squares[i].setTexture(Number.EIGHT);
					break;
				case '9':
					score_squares[i].setTexture(Number.NINE);
					break;
				default:
					break;
			}
		}
	}
	
	/**
	 * Resets the menu so that the score can be updated. And so the menus pulls down again.
	 */
	public synchronized void clean()
	{
		SquareStorage.giveSquare(button_continue);
		for(int i = 0; i<3; i++)
		{
			SquareStorage.giveSquare(initials[i]);
			SquareStorage.giveSquare(up_arrows[i]);
			SquareStorage.giveSquare(down_arrows[i]);
			initials[i] = null;
			up_arrows[i] = null;
			down_arrows[i] = null;
		}
		for(int i = 0; i<score_squares.length; i++)
		{
			SquareStorage.giveSquare(score_squares[i]);
			score_squares[i] = null;
		}
		for(int i =0; i<letters.length; i++)
		{
			SquareStorage.giveSquare(letters[i]);
			letters[i] = null;
		}
		
		button_continue = null;
		
		initialized = false;
	}
}
