package net.nicksneurons.blastthebox.tmp;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import android.opengl.GLU;

import com.millerni456.BlastTheBox.menus.GameOverScreen;
import com.millerni456.BlastTheBox.menus.GuideMenu;
import com.millerni456.BlastTheBox.menus.HighScoreMenu;
import com.millerni456.BlastTheBox.menus.MainMenu;
import com.millerni456.BlastTheBox.menus.OptionsMenu;
import com.millerni456.BlastTheBox.utils.AbstractRenderer;
import com.millerni456.BlastTheBox.utils.AndroidGLCamera;
import com.millerni456.BlastTheBox.utils.CubeStorage;
import com.millerni456.BlastTheBox.utils.Letter;
import com.millerni456.BlastTheBox.utils.Number;
import com.millerni456.BlastTheBox.utils.SoundPlayer;
import com.millerni456.BlastTheBox.utils.SquareStorage;
import com.millerni456.BlastTheBox.utils.Texture;
import com.millerni456.BlastTheBox.utils.Textures;


public class CE_Renderer extends AbstractRenderer
{
	public final BlastTheBoxActivity app;
	public static AndroidGLCamera glCamera;
	public static MainMenu main;
	public static HighScoreMenu highscores;
	public static OptionsMenu options;
	public static GuideMenu guide;
	public static GameOverScreen gameover_screen;
	
	public CE_Renderer(BlastTheBoxActivity app)
	{
		this.app = app;

		CubePopulator.FIELD_WIDTH = 20;
		CubePopulator.DENSITY = 10;
		
		glCamera = new AndroidGLCamera();
		reset(glCamera);
	}
	
	public float[] light0 = 
	{
			0.1f, 0.1f, 0.1f,  //Ambient
			1, 1, 1,  //Diffuse
			1, 1, 1,  //Specular
			1, 0, 1, 0 //Position
	};
	public static float[] fogColor = {.234f, .125f, .555f, 0.8f};
	public static float density = 0.03f;//0.03f;//0.025f;
	
	@Override
	public void init(GL10 gl)
	{   
		Textures.loadTextures(app, gl);//create TextureID constants
		Number.loadNumbers(app, gl);//create more TextureID constants
		Letter.loadLetters(app, gl);
		BulletParticles.loadTextures(app, gl);
		Flash.loadFlashTextures(app, gl);
		Darkener.loadDarkenTextures(app, gl);
		Explosion.loadExplosion(app, gl);
		CubeStorage.createCubes(gl);
		SquareStorage.createSquares(gl);
		CubePopulator.createRows();
		
		main = new MainMenu();
		gameover_screen = new GameOverScreen();
		highscores = new HighScoreMenu();
		options = new OptionsMenu();
		guide = new GuideMenu();
		
		//set default parameters.
		Texture.setTextureEnvelope(gl, GL10.GL_TEXTURE_ENV_MODE, GL10.GL_REPLACE);
		
		//Light settings
		gl.glEnable(GL10.GL_LIGHT0);
		gl.glEnable(GL10.GL_LIGHTING);
		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_AMBIENT, light0, 0);
		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_DIFFUSE, light0, 3);
		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_SPECULAR, light0, 6);
		//Fog settings
		gl.glEnable(GL10.GL_FOG);
		gl.glFogf(GL10.GL_FOG_MODE, GL10.GL_EXP2);
		gl.glHint(GL10.GL_FOG_HINT, GL10.GL_NICEST);
		
		SoundPlayer.loopSound(R.raw.loop_two);
		SoundPlayer.getMediaPlayer().setVolume(.4f, .4f);
	}
	
	@Override
	public void resize(GL10 gl, int width, int height)
	{
		
	}

	@Override
	public void render(GL10 gl)
	{	
		try{
		switch(CE_Updater.STATE)
		{
			case CE_Updater.MAIN_MENU:
				setup2DGraphics(gl);
				main.render(gl, width, height);
				break;
			case CE_Updater.HIGHSCORES:
				setup2DGraphics(gl);
				highscores.render(gl, width, height);
				break;
			case CE_Updater.OPTIONS:
				setup2DGraphics(gl);
				options.render(gl, width, height);
				break;
			case CE_Updater.GUIDE:
				setup2DGraphics(gl);
				guide.render(gl, width, height);
				break;
			case CE_Updater.GAME:
				gl.glEnable(GL10.GL_DEPTH_TEST);
				
				gl.glClearColor(clearColor[0], clearColor[1], clearColor[2], clearColor[3]);
				//Fog settings
				gl.glFogfv(GL10.GL_FOG_COLOR, fogColor, 0);
				gl.glFogf(GL10.GL_FOG_DENSITY, density);
				
				//Set up projection matrix
				gl.glMatrixMode(GL10.GL_PROJECTION);
				gl.glLoadIdentity();
				GLU.gluPerspective(gl, 45, width/height, 0.01f, 50);
				
				//Switch to modelview and render geometry.
				gl.glMatrixMode(GL10.GL_MODELVIEW);
				gl.glLoadIdentity();
				gl.glColor4f(1, 1, 1, 1);
				glCamera.attachCamera((GL11)gl);
				
				gl.glEnable(GL10.GL_BLEND);
				gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
				app.updater.current_game.renderPerspective(gl);
				
				/**END OF PERSPECTIVE AND ONTO ORTHOGRAPHIC**/
				
				//Set up an orthographic projection.
				gl.glViewport(0, 0, width, height);
				gl.glMatrixMode(GL10.GL_PROJECTION);
				gl.glLoadIdentity();
				GLU.gluOrtho2D(gl,0, width, 0, height);
				
				//Switch to modelview then render the score, health, and cursor.
				gl.glMatrixMode(GL10.GL_MODELVIEW);
				gl.glLoadIdentity();
				gl.glColor4f(0, 0, 0, 0);
				gl.glDisable(GL10.GL_DEPTH_TEST);

				app.updater.current_game.renderOrthographic(gl, width, height);
				
				if(app.updater.current_game.gameOver())
				{
					gameover_screen.render(gl, width, height);
				}
				gl.glDisable(GL10.GL_BLEND);
				
				
				break;
			default:
				break;
		}	
		}catch(Exception e){}
	}//End Of render(GL10 gl)
	
	public static void reset(AndroidGLCamera glCamera)
	{
		glCamera.setPosition(0, 1, 0);
		glCamera.setFocusPoint(0, 1, -100);
		glCamera.strafeRight(.5f);
	}
	
	public static void resetFogVariables()
	{
		fogColor[0] = .234f;
		fogColor[1] = .125f;
		fogColor[2] = .555f;
		fogColor[3] = .8f;
		density = 0.03f;//0.025f;
	}
	
	private void setup2DGraphics(GL10 gl)
	{
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		gl.glDisable(GL10.GL_DEPTH_TEST);
		
		//Set up an orthographic projection.
		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		GLU.gluOrtho2D(gl,0, width, 0, height);
		
		//Switch to modelview then render the score, health, and cursor.
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		gl.glColor4f(0, 0, 0, 0);
	}
}
