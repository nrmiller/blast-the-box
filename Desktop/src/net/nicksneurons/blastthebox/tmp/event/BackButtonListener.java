package net.nicksneurons.blastthebox.tmp.event;

import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;

import com.millerni456.BlastTheBox.CE_SurfaceView;
import com.millerni456.BlastTheBox.CE_Updater;
import com.millerni456.BlastTheBox.utils.SoundManager;

public class BackButtonListener implements OnKeyListener
{
	public final CE_Updater updater;
	
	public BackButtonListener(CE_Updater updater)
	{
		this.updater = updater;
	}
	
	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event)
	{
		if(v instanceof CE_SurfaceView)
		{
			if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN 
					&& event.getRepeatCount() == 0)
			{
				backPressed(keyCode, event);
				return true;
			}
		}
		return false;
	}
	
	public void backPressed(int keyCode, KeyEvent event)
	{
		switch(CE_Updater.STATE)
		{
			case CE_Updater.GUIDE:
			case CE_Updater.HIGHSCORES:
			case CE_Updater.OPTIONS:
			{
				SoundManager.playSound(SoundManager.click, 1);
				CE_Updater.STATE = CE_Updater.MAIN_MENU;
			}
			break;
			case CE_Updater.MAIN_MENU:
			{
				updater.app.onPause();
			}
			break;
			case CE_Updater.GAME:
			{
				if(!updater.current_game.gameOver())
				{
					updater.current_game.pauseManager.tapPauseButton();
				}
			}
			break;
		}
	}

}
