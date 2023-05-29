package com.millerni456.BlastTheBox.utils;

import java.util.HashMap;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import com.millerni456.BlastTheBox.R;

public class SoundManager {
	 
	private static SoundManager _instance;
	private static SoundPool mSoundPool;
	private static HashMap<Integer, Integer> mSoundPoolMap;
	private static int[] streamID;
	private static boolean[] streamUsed;
	private static AudioManager  mAudioManager;
	private static Context mContext;
	public static boolean MUTED = false;
	public static int boom = 1, nuclear_explosion = 2, shot = 3, heartbeat = 4, reload = 5, shield = 6;
	public static int stopwatch = 7;
	public static int green_up = 8, blue_up = 9;
	public static int click = 10, click2 = 11, pierce = 12;
	public static int[] ahh = {13, 14, 15};
	public static int shield_off = 16;
	public static int triple = 17;
 
	private SoundManager()
	{
	}
 
	/**
	 * Requests the instance of the Sound Manager and creates it
	 * if it does not exist.
	 *
	 * @return Returns the single instance of the SoundManager
	 */
	static synchronized public SoundManager getInstance()
	{
	    if (_instance == null)
	      _instance = new SoundManager();
	    return _instance;
	 }
 
	/**
	 * Initialises the storage for the sounds
	 *
	 * @param theContext The Application context
	 */
	public static  void initSounds(Context theContext)
	{
		mContext = theContext;
	    mSoundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
	    streamID = new int[5];
	    streamUsed = new boolean[5];
	    for(int i = 0; i<streamUsed.length; i++)
	    {
	    	streamUsed[i] = false;
	    }
	    mSoundPoolMap = new HashMap<Integer, Integer>();
	    mAudioManager = (AudioManager)mContext.getSystemService(Context.AUDIO_SERVICE);
	} 
 
	/**
	 * Add a new Sound to the SoundPool
	 *
	 * @param Index - The Sound Index for Retrieval
	 * @param SoundID - The Android ID for the Sound asset.
	 */
	public static void addSound(int Index,int SoundID)
	{
		mSoundPoolMap.put(Index, mSoundPool.load(mContext, SoundID, 1));
	}
 
	/**
	 * Loads the various sound assets
	 * Currently hardcoded but could easily be changed to be flexible.
	 */
	public static void loadSounds()
	{
		mSoundPoolMap.put(1, mSoundPool.load(mContext, R.raw.boom , 1));
		mSoundPoolMap.put(2, mSoundPool.load(mContext, R.raw.nuclear_explosion, 1));
		mSoundPoolMap.put(3, mSoundPool.load(mContext, R.raw.shot, 1));
		mSoundPoolMap.put(4, mSoundPool.load(mContext, R.raw.heartbeat, 1));
		mSoundPoolMap.put(5, mSoundPool.load(mContext, R.raw.reload, 1));
		mSoundPoolMap.put(6, mSoundPool.load(mContext, R.raw.shield, 1));
		mSoundPoolMap.put(7, mSoundPool.load(mContext, R.raw.stopwatch, 1));
		mSoundPoolMap.put(8, mSoundPool.load(mContext, R.raw.green_up, 1));
		mSoundPoolMap.put(9, mSoundPool.load(mContext, R.raw.blue_up, 1));
		mSoundPoolMap.put(10, mSoundPool.load(mContext, R.raw.click, 1));
		mSoundPoolMap.put(11, mSoundPool.load(mContext, R.raw.click2, 1));
		mSoundPoolMap.put(12, mSoundPool.load(mContext, R.raw.pierce, 1));
		mSoundPoolMap.put(ahh[0], mSoundPool.load(mContext, R.raw.ahh1, 1));
		mSoundPoolMap.put(ahh[1], mSoundPool.load(mContext, R.raw.ahh2, 1));
		mSoundPoolMap.put(ahh[2], mSoundPool.load(mContext, R.raw.ahh3, 1));
		mSoundPoolMap.put(16, mSoundPool.load(mContext, R.raw.shield_off, 1));
		mSoundPoolMap.put(17, mSoundPool.load(mContext, R.raw.triple, 1));
	}
 
	/**
	 * Plays a Sound
	 *
	 * @param index - The Index of the Sound to be played
	 * @param speed - The Speed to play not, not currently used but included for compatibility
	 */
	public static void playSound(int index, float speed)
	{
		if(!MUTED)
		{
			float streamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
			streamVolume = streamVolume / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
			mSoundPool.play(mSoundPoolMap.get(index), streamVolume, streamVolume, 1, 0, speed);
		}
		
		
	}
	public static void loopSound(int index, float speed)
	{
		if(!MUTED)
		{
			float streamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
			streamVolume = streamVolume / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
			int newStream = mSoundPool.play(mSoundPoolMap.get(index), streamVolume, streamVolume, 1, -1, speed);
			
			boolean succeeded = false;
			for(int i = 0; i<streamUsed.length; i++)
			{
				if(!streamUsed[i])
				{
					streamUsed[i] = true;
					streamID[i] = newStream;
					succeeded = true;
				}
			}
			if(!succeeded)
			{
				stopSound(newStream);
			}
		}
	}
 
	/**
	 * Stop a Sound
	 * @param index - index of the sound to be stopped
	 */
	private static void stopSound(int index)
	{
		mSoundPool.stop(mSoundPoolMap.get(index));
	}
	
	public static void stopAllSounds()
	{
		for(int i = 0; i<streamID.length; i++)
		{
			if(streamUsed[i])
			{
				stopSound(streamID[i]);
				streamUsed[i] = false;
			}
		}
	}
 
	/**
	 * Deallocates the resources and Instance of SoundManager
	 */
	public static void clean()
	{
		if(mSoundPool==null)
		{
			mSoundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);
		}
		mSoundPool.release();
		mSoundPool = null;
	    mSoundPoolMap.clear();
	    mAudioManager.unloadSoundEffects();
	    _instance = null;
 
	}
 
}
