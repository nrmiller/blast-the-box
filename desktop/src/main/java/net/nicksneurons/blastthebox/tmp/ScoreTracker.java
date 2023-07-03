package net.nicksneurons.blastthebox.tmp;

import javax.microedition.khronos.opengles.GL10;

import miller.opengl.Dimension3d;
import miller.opengl.Point3d;

import com.millerni456.BlastTheBox.geometry.Square;
import com.millerni456.BlastTheBox.utils.Number;
import com.millerni456.BlastTheBox.utils.SquareStorage;

public class ScoreTracker
{
	private int score = 0;
	private Square[] squares = new Square[15];//Up to trillions place.
	private Square[] bSquares = new Square[8];// + sign and up to 7 digits
	private boolean animateBonus = false;
	private int bonusDigits = 0;
	private int bonusHeight = 56; //initial height
	
	/**
	 * This cannot be instantiated before the SquareStorage is created.
	 */
	public ScoreTracker()
	{
		score = 0;
		for(int i = 0; i<squares.length; i++)
		{
			squares[i] = SquareStorage.getSquare();
		}
		for(int i = 0; i<bSquares.length; i++)
		{
			bSquares[i] = SquareStorage.getSquare();
		}
	}
	
	public void addScore(int amount)
	{
		score += amount;
	}
	public void addBonusScore(int amount, int screenH)
	{
		float width = screenH / 10.667f;
		float height = width * 1.1667f;
		bonusHeight = (int)height;
		bonusDigits = 1 + getDigits(amount);
		score += amount;
		pickTexturesYellow(amount);
		animateBonus = true;
	}
	public void setScore(int score)
	{
		this.score = score;
	}
	public int getScore()
	{
		return score;
	}
	
	/**
	 * Draws the score in the current projection mode.
	 * Location computations are based on 2D orthographic mode however.
	 * @param gl - OpenGL context.
	 * @param screenW - the width of the screen.
	 * @param screenH - the height of the screen.
	 */
	public void renderScore(GL10 gl, int screenW, int screenH)
	{
		int digits = getDigits(score);
		pickTextures();
		
		float pause_width = screenH * .125f;
		float width = screenH / 10.667f;
		float height = width * 1.1667f;
		float y_gap = (pause_width - height) /2;
		for(int i = 0; i<digits; i++)
		{
			squares[i].setLocation(new Point3d(pause_width + i * width, screenH - height-y_gap, 0));
			squares[i].setScale(new Dimension3d(width, height, 1));
			squares[i].draw(gl);
		}
		if(animateBonus)
		{
			for(int i = 0; i<bonusDigits; i++)
			{
				bSquares[i].setLocation(new Point3d(pause_width + i * width, screenH - height - bonusHeight-y_gap, 0));
				bSquares[i].setScale(new Dimension3d(width, height, 1));
				bSquares[i].draw(gl);
			}
		}
	}
	
	public void update(int screenH)
	{
		float width = screenH / 10.667f;
		float height = width * 1.1667f;
		if(animateBonus)
		{
			
			bonusHeight++;
			if(bonusHeight>=(height*2))
			{
				animateBonus = false;
				bonusHeight = (int) height;
			}
		}
	}
	
	private void pickTextures()
	{
		String s = String.valueOf(score);
		
		for(int i = 0; i<s.length(); i++)
		{
			switch(s.charAt(i))
			{
				case '0':
					squares[i].setTexture(Number.ZERO);
					break;
				case '1':
					squares[i].setTexture(Number.ONE);
					break;
				case '2':
					squares[i].setTexture(Number.TWO);
					break;
				case '3':
					squares[i].setTexture(Number.THREE);
					break;
				case '4':
					squares[i].setTexture(Number.FOUR);
					break;
				case '5':
					squares[i].setTexture(Number.FIVE);
					break;
				case '6':
					squares[i].setTexture(Number.SIX);
					break;
				case '7':
					squares[i].setTexture(Number.SEVEN);
					break;
				case '8':
					squares[i].setTexture(Number.EIGHT);
					break;
				case '9':
					squares[i].setTexture(Number.NINE);
					break;
				default:
					break;
				
			}
		}
	}
	private void pickTexturesYellow(int bonus)
	{
		String s = "+" + String.valueOf(bonus);
		
		for(int i = 0; i<s.length(); i++)
		{
			switch(s.charAt(i))
			{
				case '+':
					bSquares[i].setTexture(Number.PLUSy);
					break;
				case '0':
					bSquares[i].setTexture(Number.ZEROy);
					break;
				case '1':
					bSquares[i].setTexture(Number.ONEy);
					break;
				case '2':
					bSquares[i].setTexture(Number.TWOy);
					break;
				case '3':
					bSquares[i].setTexture(Number.THREEy);
					break;
				case '4':
					bSquares[i].setTexture(Number.FOURy);
					break;
				case '5':
					bSquares[i].setTexture(Number.FIVEy);
					break;
				case '6':
					bSquares[i].setTexture(Number.SIXy);
					break;
				case '7':
					bSquares[i].setTexture(Number.SEVENy);
					break;
				case '8':
					bSquares[i].setTexture(Number.EIGHTy);
					break;
				case '9':
					bSquares[i].setTexture(Number.NINEy);
					break;
				default:
					break;
				
			}
		}
	}
	
	private int getDigits(int num)
	{
		String s = String.valueOf(num);
		return s.length();
	}
	
	public void clean()
	{
		score = 0;
		for(int i = 0; i<squares.length; i++)
		{
			SquareStorage.giveSquare(squares[i]);
		}
		for(int i = 0; i<bSquares.length; i++)
		{
			SquareStorage.giveSquare(bSquares[i]);
		}
	}
}
