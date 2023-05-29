package com.millerni456.BlastTheBox.event;

public class PausePlayEvent
{
	public final static int PAUSE = 0, PLAY = 1;
	private int type = -1;
	
	public PausePlayEvent(int type)
	{
		this.type = type;
	}
	
	public int getType()
	{
		return type;
	}
}
