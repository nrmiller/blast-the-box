package net.nicksneurons.blastthebox.tmp;

import android.os.Handler;

import com.millerni456.BlastTheBox.utils.AbstractRenderer;

public class StopwatchTracker
{
	public boolean slow_motion = false;
	private boolean canUpdate = true;
	public double cubeSpeedPercent = 1;
	private Handler handler = new Handler();
	public float density = 0;
	public float densityMod = 0;
	public float[] fogColor = new float[4];
	private float[] fogMod = new float[4];
	public float[] clearColor = new float[4];
	private float[] clearMod = new float[4];
	
	public StopwatchTracker()
	{
		
	}
	
	public void slowTime()
	{
		CE_Renderer.resetFogVariables();
		AbstractRenderer.resetClearColor();
		slow_motion = true;
		cubeSpeedPercent = .5;
		//Divide by 100 because there are 100 iterations in the stop watch delay.
		fogMod[0] = .234f / 100;
		fogMod[1] = .125f / 100;
		fogMod[2] = .555f / 100;
		fogMod[3] = 0;
		fogColor[0] = 0;
		fogColor[1] = 0;
		fogColor[2] = 0;	
		fogColor[3] = .8f;
		
		densityMod = (.05f - CE_Renderer.density) / 100;
		density = .05f;
		
		clearMod[0] = (1 - AbstractRenderer.clearColor[0]) / 100;
		clearMod[1] = (1 - AbstractRenderer.clearColor[1]) / 100;
		clearMod[2] = (1 - AbstractRenderer.clearColor[2]) / 100;
		clearMod[3] = (1 - AbstractRenderer.clearColor[3]) / 100;
		clearColor[0] = 1;
		clearColor[1] = 1;
		clearColor[2] = 1;
		clearColor[3] = 1;
		
	}
	
	public double getSpeedModifier()
	{
		return cubeSpeedPercent;	
	}
	
	public void update()
	{
		if(slow_motion && canUpdate)
		{
			canUpdate = false;
			handler.postDelayed(delay, 100);
			CE_Renderer.fogColor = getFogColor();
			CE_Renderer.density = getDensity();
			CE_Renderer.clearColor = getClearColor();
		}
	}
	
	private Runnable delay = new Runnable()
	{
		public void run()
		{
			cubeSpeedPercent += 0.005;
			for(int i = 0; i<fogColor.length; i++)
			{
				fogColor[i] += fogMod[i];
				if(fogColor[i]>1)
				{
					fogColor[i] = 1;
				}
				clearColor[i] -= clearMod[i];
				if(clearColor[i] < 0)
				{
					clearColor[i] = 0;
				}
			}
			density -= densityMod;
			if(density < 0)
			{
				density = 0;
			}
			if(cubeSpeedPercent > 1)
			{
				cubeSpeedPercent = 1;
				slow_motion = false;
			}
			canUpdate = true;
		}
	};

	public float[] getFogColor()
	{
		return fogColor;
	}
	public float[] getClearColor()
	{
		return clearColor;
	}
	public float getDensity()
	{
		return density;
	}
	public boolean clearColorUpdating()
	{
		return slow_motion;
	}
}
