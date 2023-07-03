package net.nicksneurons.blastthebox.tmp;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.millerni456.BlastTheBox.io.HighScores;
import com.millerni456.BlastTheBox.utils.SoundManager;
import com.millerni456.BlastTheBox.utils.SoundPlayer;

public class BlastTheBoxActivity extends Activity {
   
	public static Handler uiHandler;
    public CE_SurfaceView surfaceView;
    public CE_Renderer renderer;
    public CE_Updater updater;
    public HighScores highscores = new HighScores();
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        uiHandler = new Handler();
        
        //Change application settings.
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        setContentView(R.layout.main);
        LinearLayout layout = (LinearLayout) this.findViewById(R.id.main_layout);
        
        HighScores.loadScores(this);
        SoundManager.getInstance();
		SoundManager.initSounds(this);
		SoundManager.loadSounds();
		SoundPlayer.getInstance(this);
        
        renderer = new CE_Renderer(this);
        updater = new CE_Updater(this);
        updater.setTargetFPS(60);
        updater.start();
        CE_Updater.STATE = CE_Updater.MAIN_MENU;
        surfaceView = new CE_SurfaceView(this, renderer, updater);
        
        layout.addView(surfaceView);
    }
    
    public void onStart()
    {
    	super.onStart();
    }
    
    public void onRestart()
    {
    	super.onRestart();
    }
    
    public void onResume()
    {
    	super.onResume();
    	surfaceView.onResume();
    }
    
    public void onPause()
    {
    	super.onPause();
    	surfaceView.onPause();

    	HighScores.writeScores(this);
    	SoundManager.clean();
    	SoundPlayer.clean();
    	
    	System.runFinalizersOnExit(true);
    	System.exit(0);
    	
    }
    
    public void onStop()
    {
    	super.onStop();
    }
    
    public void onDestroy()
    {
    	super.onDestroy();
    }
}