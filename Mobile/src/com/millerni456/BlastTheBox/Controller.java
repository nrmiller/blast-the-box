package com.millerni456.BlastTheBox;

import javax.microedition.khronos.opengles.GL10;

import miller.opengl.Dimension3d;
import miller.opengl.Point3d;

import com.millerni456.BlastTheBox.geometry.Square;
import com.millerni456.BlastTheBox.utils.SquareStorage;
import com.millerni456.BlastTheBox.utils.Textures;

public class Controller
{
	private Square[] squares = new Square[3];
	private Square cursor;
	
	public Controller()
	{
		for(int i = 0; i<squares.length; i++)
		{
			squares[i] = SquareStorage.getSquare();
		}
		cursor = SquareStorage.getSquare();
		
		squares[0].setTexture(Textures.arrow_left);
		squares[1].setTexture(Textures.button_fire);
		squares[2].setTexture(Textures.arrow_right);
		cursor.setTexture(Textures.cursor);
	}
	
	public void render(GL10 gl, int width, int height, boolean show_controller)
	{
		int cursor_Y = 0;
		if(show_controller)
		{
			cursor_Y = (int)width/8;
			squares[0].setScale(new Dimension3d((int)width/4, (int)width/4, 0));
			squares[0].setLocation(new Point3d(0, 0, 0));
		
			squares[1].setScale(new Dimension3d((int)width/2, (int)width/4, 0));
			squares[1].setLocation(new Point3d(width/4, 0, 0));
		
			squares[2].setScale(new Dimension3d((int)width/4, (int)width/4, 0));
			squares[2].setLocation(new Point3d(width/4*3, 0, 0));
		
			for(int i = 0; i<squares.length; i++)
			{
				squares[i].draw(gl);
			}
		}
		
		int cursor_H = (int)height/16;
		int cursor_W = (int)2*cursor_H;
		int cursor_X = (int)(width-cursor_W)/2;
		cursor.setScale(new Dimension3d(cursor_W, cursor_H, 0));
		cursor.setLocation(new Point3d(cursor_X, cursor_Y, 0));
		cursor.draw(gl);
	}
	
	public void clean()
	{
		for(int i = 0; i<squares.length; i++)
		{
			SquareStorage.giveSquare(squares[i]);
		}
	}
}
