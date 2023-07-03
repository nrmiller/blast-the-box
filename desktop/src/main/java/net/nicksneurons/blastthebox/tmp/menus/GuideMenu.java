package net.nicksneurons.blastthebox.tmp.menus;

import javax.microedition.khronos.opengles.GL10;

import miller.opengl.Dimension3d;
import miller.opengl.Point2d;
import miller.opengl.Point3d;
import android.view.MotionEvent;

import com.millerni456.BlastTheBox.geometry.Square;
import com.millerni456.BlastTheBox.utils.SoundManager;
import com.millerni456.BlastTheBox.utils.SquareStorage;
import com.millerni456.BlastTheBox.utils.Textures;

public class GuideMenu 
{
	public int width, height;
	private Square bg, back, prev, next, display;
	private boolean initialized = false;
	public final int TOTAL_PAGES;
	public int page = 0;
	
	public GuideMenu()
	{
		TOTAL_PAGES = Textures.guide.length;
	}
	
	public void init(int width, int height)
	{
		this.width = width;
		this.height = height;
		
		initialized = true;
		bg = SquareStorage.getSquare();
		back = SquareStorage.getSquare();
		prev = SquareStorage.getSquare();
		next = SquareStorage.getSquare();
		display = SquareStorage.getSquare();
		
		bg.setScale(new Dimension3d(width, height, 0));
		bg.setLocation(new Point3d(0, 0, 0));
		bg.setTexture(Textures.menu_highscores);
		
		int backH = (int) (height * .125);
		int backW = backH;
		back.setScale(new Dimension3d(backW, backH, 0));
		back.setLocation(new Point3d(0, height-backH, 0));
		back.setTexture(Textures.button_back);
		
		int baseX = (width-2*backW)/2;
		prev.setScale(new Dimension3d(backW, backH, 0));
		prev.setLocation(new Point3d(baseX, 0, 0));
		
		next.setScale(new Dimension3d(backW, backH, 0));
		next.setLocation(new Point3d(baseX+backW, 0, 0));
		
		pickArrowTextures();
		
		int dispW = (int) (.85*width);
		int dispH = (int) (.85*height);
		int dispX = (width-dispW)/2;
		int dispY = (height-dispH)/2;
		display.setScale(new Dimension3d(dispW, dispH, 0));
		display.setLocation(new Point3d(dispX, dispY, 0));
		display.setTexture(Textures.guide[page]);
		
	}
	
	public void render(GL10 gl, int width, int height)
	{
		if(!initialized)
		{
			init(width, height);
		}
		gl.glDisable(GL10.GL_BLEND);
		bg.draw(gl);
		back.draw(gl);
		gl.glEnable(GL10.GL_BLEND);
		display.draw(gl);
		prev.draw(gl);
		next.draw(gl);
		
	}
	
	public String getAction(MotionEvent e)
	{
		String a = "";
		Point2d p = new Point2d(e.getX(), height-e.getY());
		
		if(!initialized) return a;
		if(back.isTouched(p))
		{
			a = "back";
			SoundManager.playSound(SoundManager.click, 1);
		}
		else if(prev.isTouched(p))
		{
			page--;
			if(page<0)
			{
				page = 0;
			}
			else
			{
				SoundManager.playSound(SoundManager.click2, 1);
			}
			
			display.setTexture(Textures.guide[page]);
			pickArrowTextures();
		}
		else if(next.isTouched(p))
		{
			page++;
			if(page>=TOTAL_PAGES)
			{
				page = TOTAL_PAGES-1;
			}
			else
			{
				SoundManager.playSound(SoundManager.click2, 1);
			}
			display.setTexture(Textures.guide[page]);
			pickArrowTextures();
		}
		return a;
	}
	
	private void pickArrowTextures()
	{
		if(page>0)
		{
			prev.setTexture(Textures.button_prev_on);
		}
		else
		{
			prev.setTexture(Textures.button_prev_off);
		}
		
		if(page<TOTAL_PAGES-1)
		{
			next.setTexture(Textures.button_next_on);
		}
		else
		{
			next.setTexture(Textures.button_next_off);
		}
	}
	public void clean()
	{
		SquareStorage.giveSquare(bg);
		SquareStorage.giveSquare(back);
		SquareStorage.giveSquare(prev);
		SquareStorage.giveSquare(next);
		SquareStorage.giveSquare(display);
		initialized = false;
	}
}
