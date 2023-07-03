package net.nicksneurons.blastthebox.tmp;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.ViewGroup.LayoutParams;

public class CE_SurfaceView extends GLSurfaceView
{

	public CE_SurfaceView(Context context, CE_Renderer renderer, CE_Updater updater)
	{
		super(context);
		
		this.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
        this.setFocusable(true);
        this.setFocusableInTouchMode(true);
        this.setOnTouchListener(updater);
        this.setOnKeyListener(updater.keyListener);
        this.requestFocus();
        this.setEGLConfigChooser(true);
        this.setRenderer(renderer);
        this.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
	}

}
