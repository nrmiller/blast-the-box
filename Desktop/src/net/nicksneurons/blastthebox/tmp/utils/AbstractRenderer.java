package net.nicksneurons.blastthebox.tmp.utils;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLSurfaceView.Renderer;

public abstract class AbstractRenderer implements Renderer
{
	public int width, height;
	public abstract void render(GL10 gl);
	public abstract void init(GL10 gl);
	public void resize(GL10 gl, int width, int height){}
	public static float[] clearColor = new float[4];
	
	public AbstractRenderer()
	{
		clearColor[0] = 0.086f;
		clearColor[1] = 0.173f;
		clearColor[2] = 0.380f;
		clearColor[3] = 1;
	}

	@Override
	public void onDrawFrame(GL10 gl)
	{
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT|GL10.GL_DEPTH_BUFFER_BIT);
		render(gl);
		gl.glFlush();
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height)
	{
		resize(gl, width, height);
		gl.glViewport(0, 0, width, height);
		this.width = width;
		this.height = height;
		float ratio = (float) width/height;
		
		
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glFrustumf(-ratio, ratio, -1, 1, 0.1f, 50);
		
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config)
	{
		gl.glClearDepthf(1);
		gl.glClearColor(0.086f, 0.173f, 0.380f, 1);
		gl.glDisable(GL10.GL_DITHER);
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);
		gl.glEnable(GL10.GL_DEPTH_TEST);
		gl.glDepthFunc(GL10.GL_LEQUAL);
		gl.glEnable(GL10.GL_CULL_FACE);
		gl.glFrontFace(GL10.GL_CCW);
		gl.glCullFace(GL10.GL_BACK);
		init(gl);
	}
	
	public static void resetClearColor()
	{
		clearColor[0] = 0.086f;
		clearColor[1] = 0.173f;
		clearColor[2] = 0.380f;
		clearColor[3] = 1;
	}
}
