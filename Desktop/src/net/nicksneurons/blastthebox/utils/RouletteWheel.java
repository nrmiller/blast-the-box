package net.nicksneurons.blastthebox.utils;

import java.util.Random;

public class RouletteWheel<T>
{
	public T[] items;
	public double[] rarities;
	public double[] mark_L;
	public double[] mark_R;

	public RouletteWheel(T[] items, double[] rarities)
	{
		setItems(items, rarities);
	}
	
	private void setItems(T[] items, double[] rarities)
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
	
	public T spin(Random r)
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
