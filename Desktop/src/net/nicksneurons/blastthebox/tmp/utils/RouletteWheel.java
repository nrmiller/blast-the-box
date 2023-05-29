package net.nicksneurons.blastthebox.tmp.utils;

import java.util.Random;

public class RouletteWheel
{
	public Object[] items;
	public double[] rarities;
	public double[] mark_L;
	public double[] mark_R;
	
	public RouletteWheel()
	{
		
	}
	
	public void setItems(Object[] items, double[] rarities)
	{
		this.items = items;
		this.rarities = rarities;
		
		mark_L = new double[items.length];
		mark_R = new double[items.length];
		
		float cursor = 1;
		for(int i = 0; i<items.length; i++)
		{
			mark_R[i] = cursor;
			cursor -= rarities[i];
			mark_L[i] = cursor;
		}
	}
	
	public Object spin(Random r)
	{
		float s = r.nextFloat();
		
		for(int i = 0; i<items.length; i++)
		{
			if(s>=mark_L[i] && s<mark_R[i])
			{
				return items[i];
			}
		}
		
		return null;
	}
}
