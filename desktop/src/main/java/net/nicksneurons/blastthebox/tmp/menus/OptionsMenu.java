package net.nicksneurons.blastthebox.tmp.menus;

import javax.microedition.khronos.opengles.GL10;

import miller.opengl.Dimension3d;
import miller.opengl.Point2d;
import miller.opengl.Point3d;
import android.view.MotionEvent;

import com.millerni456.BlastTheBox.R;
import com.millerni456.BlastTheBox.geometry.Square;
import com.millerni456.BlastTheBox.utils.Letter;
import com.millerni456.BlastTheBox.utils.Number;
import com.millerni456.BlastTheBox.utils.SoundManager;
import com.millerni456.BlastTheBox.utils.SoundPlayer;
import com.millerni456.BlastTheBox.utils.SquareStorage;
import com.millerni456.BlastTheBox.utils.Textures;


public class OptionsMenu
{
	public int width, height, cell_H, cell_W, optionsboxX, optionsboxW, options_top_Y;
	public float slider_X, slider_Y;
	private Square bg, back_button;
	private char[] title_char = {'o', 'p', 't', 'i', 'o', 'n', 's'};
	private Square[] title = new Square[7];//7 letters in options
	private Square slider_base, slider_thumb, music_toggle, soundfx_toggle, controller_toggle;
	private Square slider_text, music_text, soundfx_text, controller_text;
	private Square[] slider_output = new Square[4];
	private Square reset_button;
	private boolean initialized = false;
	public float sensitivity = 1f; //100%
	public boolean soundfx_enabled = true;
	public boolean music_enabled = true;
	public boolean controller_enabled = true;
	public Point3d thumbLoc = new Point3d(0, 0, 0);
	public float thumb_W = 0, slider_W = 0;
	public boolean soundLooping = true;
	
	/**
	 * Squares must be initialized before this is instantiated.
	 */
	public OptionsMenu()
	{
		
	}
	
	public void init(int widthOfScreen, int heightOfScreen)
	{
		width = widthOfScreen;
		height = heightOfScreen;
		float base_letterH = .1f*height; //Letter/Number Dimensions
		float base_letterW = base_letterH/1.167f;
		
		initialized = true; //Prevent continuous initializations
		
		/*BACKGROUND AND BACK BUTTON CALCULATIONS*/
		bg = SquareStorage.getSquare();
		back_button = SquareStorage.getSquare();
		
		bg.setScale(new Dimension3d(width, height, 0));
		bg.setLocation(new Point3d(0, 0, 0));
		bg.setTexture(Textures.menu_highscores);
		
		int backH = (int) (heightOfScreen * .125);
		int backW = backH;
		back_button.setScale(new Dimension3d(backW, backH, 0));
		back_button.setLocation(new Point3d(0, height-backH, 0));
		back_button.setTexture(Textures.button_back);
		
		//OPTIONS BOX
		optionsboxW = (int) (.8 * width);
		int optionsboxH = (int) (.7 * height);
		optionsboxX = (int) (.1 * width);
		int optionsboxY = (int) (.05 * height);
		options_top_Y = (optionsboxY + optionsboxH);
		cell_W = optionsboxW /2;
		cell_H = optionsboxH /5;
		
		/*OPTIONS TITLE CALCULATIONS*/
		int base_titleX = (int) (width - title.length*base_letterW) / 2;
		int base_titleH = (height - (optionsboxY+optionsboxH));
		int base_titleY = (int) ((base_titleH - base_letterH) /2) + optionsboxY + optionsboxH;
		for(int i = 0; i<title.length; i++)
		{
			title[i] = SquareStorage.getSquare();
			title[i].setLocation(new Point3d(base_titleX + i*base_letterW, base_titleY, 0));
			title[i].setScale(new Dimension3d(base_letterW, base_letterH, 0));
			title[i].setTexture(Letter.getLetter(title_char[i]));
		}
		
		/*TEXT CALCULATIONS*/
		float text_W = optionsboxW / 4;
		float text_H = optionsboxH / 10;
		float text_X = optionsboxX + (text_W / 2);
		float text_padding_Y = (cell_H - text_H)/2;
		
		slider_text = SquareStorage.getSquare();
		controller_text = SquareStorage.getSquare();
		music_text = SquareStorage.getSquare();
		soundfx_text = SquareStorage.getSquare();
		
		slider_text.setLocation(new Point3d(text_X, options_top_Y - cell_H + text_padding_Y, 0));
		controller_text.setLocation(new Point3d(text_X, options_top_Y - (cell_H*2) + text_padding_Y, 0));
		music_text.setLocation(new Point3d(text_X, options_top_Y - (cell_H*3) + text_padding_Y, 0));
		soundfx_text.setLocation(new Point3d(text_X, options_top_Y - (cell_H*4) + text_padding_Y, 0));
		
		slider_text.setScale(new Dimension3d(text_W, text_H, 0));
		controller_text.setScale(new Dimension3d(text_W, text_H, 0));
		music_text.setScale(new Dimension3d(text_W, text_H, 0));
		soundfx_text.setScale(new Dimension3d(text_W, text_H, 0));
		
		slider_text.setTexture(Textures.text_tolerance);
		controller_text.setTexture(Textures.text_controller);
		music_text.setTexture(Textures.text_music);
		soundfx_text.setTexture(Textures.text_soundfx);
		
		
		/*SLIDER CALCULATIONS*/
		slider_W = cell_W * .75f;
		float slider_H = cell_H * .75f;
		slider_X = optionsboxX + optionsboxW / 2 +(cell_W-slider_W)/2;
		slider_Y = options_top_Y - cell_H + (cell_H-slider_H)/2;
		thumb_W = slider_W/4;
		thumbLoc = new Point3d(slider_X + (sensitivity-.5f)*slider_W - thumb_W/2, slider_Y, 0);
		
		slider_base = SquareStorage.getSquare();
		slider_thumb = SquareStorage.getSquare();
		slider_base.setLocation(new Point3d(slider_X, slider_Y, 0));
		slider_thumb.setLocation(thumbLoc);
		slider_base.setScale(new Dimension3d(slider_W, slider_H, 0));
		slider_thumb.setScale(new Dimension3d(thumb_W, slider_H, 0));
		slider_base.setTexture(Textures.slider_base);
		slider_thumb.setTexture(Textures.slider_thumb);
		
		for(int i = 0; i<slider_output.length; i++)
		{
			slider_output[i] = SquareStorage.getSquare();
		}
		setupSliderOutput(sensitivity, (int)(cell_H*.5));
		
		/*TOGGLEABLE BUTTON CALCULATIONS*/
		controller_toggle = SquareStorage.getSquare();
		music_toggle = SquareStorage.getSquare();
		soundfx_toggle = SquareStorage.getSquare();
		
		float button_X = slider_X + (slider_W - cell_H)/2;
		
		controller_toggle.setLocation(new Point3d(button_X, options_top_Y - (cell_H*2), 0));
		controller_toggle.setScale(new Dimension3d(cell_H, cell_H, 0));
		
		music_toggle.setLocation(new Point3d(button_X, options_top_Y - (cell_H*3), 0));
		music_toggle.setScale(new Dimension3d(cell_H, cell_H, 0));
		
		soundfx_toggle.setLocation(new Point3d(button_X, options_top_Y - (cell_H*4), 0));
		soundfx_toggle.setScale(new Dimension3d(cell_H, cell_H, 0));
		
		//RESET BUTTON CALCULATIONS
		reset_button = SquareStorage.getSquare();
		reset_button.setLocation(new Point3d(0, 0, 0));
		reset_button.setScale(new Dimension3d(backW, backH, 0));
		reset_button.setTexture(Textures.button_reset);
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
		slider_base.draw(gl);
		slider_thumb.draw(gl);
		
		gl.glEnable(GL10.GL_BLEND);
		for(int i = 0; i<title.length; i++)
		{
			title[i].draw(gl);
		}
		
		String amount = String.valueOf(Math.round(sensitivity*100));
		for(int i = 0; i<amount.length()+1; i++)
		{
			slider_output[i].draw(gl);
		}
		
		reset_button.draw(gl);
		slider_text.draw(gl);
		controller_text.draw(gl);
		music_text.draw(gl);
		soundfx_text.draw(gl);
		
		if(music_enabled)
		{
			music_toggle.setTexture(Textures.music_on);
		}
		else
		{
			music_toggle.setTexture(Textures.music_off);
		}
		music_toggle.draw(gl);
		
		if(soundfx_enabled)
		{
			soundfx_toggle.setTexture(Textures.soundfx_on);
		}
		else
		{
			soundfx_toggle.setTexture(Textures.soundfx_off);
		}
		soundfx_toggle.draw(gl);
		
		if(controller_enabled)
		{
			controller_toggle.setTexture(Textures.controller_on);
		}
		else
		{
			controller_toggle.setTexture(Textures.controller_off);
		}
		controller_toggle.draw(gl);
		
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
		else if(music_toggle.isTouched(p))
		{
			music_enabled = !music_enabled;
			if(music_enabled)
			{
				SoundPlayer.MUTED = false;
				soundLooping = true;
				SoundPlayer.loopSound(R.raw.loop_two);
				SoundPlayer.getMediaPlayer().setVolume(.4f, .4f);
			}
			else
			{
				SoundPlayer.MUTED = true;
				soundLooping = false;
				SoundPlayer.stopSound();
			}
			SoundManager.playSound(SoundManager.click2, 1);
		}
		else if(soundfx_toggle.isTouched(p))
		{
			soundfx_enabled = !soundfx_enabled;
			if(soundfx_enabled)
			{
				SoundManager.MUTED = false;
			}
			else
			{
				SoundManager.MUTED = true;
				SoundManager.stopAllSounds();
			}
			SoundManager.playSound(SoundManager.click2, 1);
		}
		else if(controller_toggle.isTouched(p))
		{
			controller_enabled = !controller_enabled;
			SoundManager.playSound(SoundManager.click2, 1);
		}
		else if(reset_button.isTouched(p))
		{
			resetToDefaults();
		}
		
		return action;
	}
	
	public void touchScreen(Point2d loc)
	{
		Point2d p = new Point2d(loc.x, height - loc.y);
		if(slider_base.isTouched(p))//Do not update the slider if the powerup info is displayed.
		{
			thumbLoc.setX(p.getX() - thumb_W/2);
			repositionThumb();
			calculateSensitivity();
		}
	}
	private void repositionThumb()
	{
		if(thumbLoc.getX() < slider_base.getLocation().getX())
		{
			thumbLoc.setX(slider_base.getLocation().getX());
		}
		else if(thumbLoc.getX() >= slider_base.getLocation().getX() + slider_W - thumb_W)
		{
			thumbLoc.setX(slider_base.getLocation().getX() + slider_W - thumb_W);
		}
		slider_thumb.setLocation(thumbLoc);
	}
	private void calculateSensitivity()
	{
		float percent = (float) (thumbLoc.getX()-slider_base.getLocation().getX()) / (slider_W - thumb_W);//between 0 and 1
		sensitivity = percent + .5f; //New values range from 50% and 150%.
		if(sensitivity%.1f == 0) 
		{
			SoundManager.playSound(SoundManager.click2, 1);
		}
		setupSliderOutput(sensitivity, (int)(cell_H*.5));
	}
	
	public void clean()
	{
		SquareStorage.giveSquare(bg);
		SquareStorage.giveSquare(back_button);
		SquareStorage.giveSquare(reset_button);
		
		for(int i = 0; i<title.length; i++)
		{
			SquareStorage.giveSquare(title[i]);
			title[i] = null;
		}
		
		for(int i = 0; i<slider_output.length; i++)
		{
			SquareStorage.giveSquare(slider_output[i]);
			slider_output[i] = null;
		}
		
		SquareStorage.giveSquare(slider_base);
		SquareStorage.giveSquare(slider_thumb);
		SquareStorage.giveSquare(controller_toggle);
		SquareStorage.giveSquare(music_toggle);
		SquareStorage.giveSquare(soundfx_toggle);
		
		SquareStorage.giveSquare(slider_text);
		SquareStorage.giveSquare(controller_text);
		SquareStorage.giveSquare(music_text);
		SquareStorage.giveSquare(soundfx_text);
		
		bg = null;
		back_button = null;
		reset_button = null;
		slider_base = null;
		slider_thumb = null;
		controller_toggle = null;
		music_toggle = null;
		soundfx_toggle = null;
		slider_text = null;
		controller_text = null;
		music_text = null;
		soundfx_text = null;
		
		initialized = false;
	}
	
	private void setupSliderOutput(float sensitivity, int height)
	{
		float letter_width = (height/1.167f);
		String amount = String.valueOf(Math.round(sensitivity*100));
		int length = amount.length();
		
		int padding_Y = (int) ((cell_H - height)/2);
		int padding_X = (int) ((cell_W - (letter_width*length))/2);
		int base_X = optionsboxX + optionsboxW/2;
		
		for(int i =0; i<length; i++)
		{
			slider_output[i].setLocation(new Point3d(base_X + padding_X + i*letter_width, options_top_Y - cell_H + padding_Y, 0));
			slider_output[i].setScale(new Dimension3d(letter_width, height, 0));
			slider_output[i].setTexture(Number.getNumberTextureFromChar(amount.charAt(i)));
		}
		slider_output[length].setLocation(new Point3d(base_X + padding_X + length*letter_width, options_top_Y - cell_H + padding_Y, 0));
		slider_output[length].setScale(new Dimension3d(letter_width, height, 0));
		slider_output[length].setTexture(Number.PERCENT);
	}
	
	public void resetToDefaults()
	{
		sensitivity = 1;
		thumbLoc = new Point3d(slider_X + (sensitivity-.5f)*slider_W - thumb_W/2, slider_Y, 0);
		slider_thumb.setLocation(thumbLoc);
		setupSliderOutput(sensitivity, (int)(cell_H*.5));
		
		controller_enabled = true;
		music_enabled = true;
		soundfx_enabled = true;
		SoundPlayer.MUTED = false;
		SoundManager.MUTED = false;
		
		SoundManager.playSound(SoundManager.click2, 1);
		if(!soundLooping)
		{
			soundLooping = true;
			SoundPlayer.loopSound(R.raw.loop_two);
			SoundPlayer.getMediaPlayer().setVolume(.4f, .4f);
		}
	}
}
