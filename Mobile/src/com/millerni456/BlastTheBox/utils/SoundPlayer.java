package com.millerni456.BlastTheBox.utils;

import android.content.Context;
import android.media.MediaPlayer;

public class SoundPlayer
{
	private static SoundPlayer _instance;
	private static Context _context;
	private static MediaPlayer mp;
	public static boolean MUTED = false;
	
	private SoundPlayer()
	{
		
	}
	
	public static SoundPlayer getInstance(Context c)
	{
		_context = c;
		if(_instance==null)
		{
			_instance = new SoundPlayer();
		}
		return _instance;
	}
	
	public static void loopSound(int id)
	{
		mp = MediaPlayer.create(_context, id);
		
		if(!MUTED && mp!=null)
		{
			mp.setLooping(true);
			mp.start();
		}
	}
	public static void stopSound()
	{
		if(mp!=null)
		{
			mp.stop();
			clean();
			mp = null;
		}
	}
	public static MediaPlayer getMediaPlayer()
	{
		return mp;
	}
	
	public static void clean()
	{
		if(mp!=null)
		{
			try
			{
				mp.release();
			}
			catch(Exception e)
			{}
		}
	}
}
