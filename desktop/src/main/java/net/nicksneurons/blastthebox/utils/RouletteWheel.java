package net.nicksneurons.blastthebox.utils;

import java.util.*;

public class RouletteWheel<T>
{
	public List<T> items;
	public List<Double> rarities;
	public double[] mark_L;
	public double[] mark_R;

	public RouletteWheel(Map<T, Double> map)
	{
		this.items = new ArrayList<>();
		this.rarities = new ArrayList<>();

		for (Map.Entry<T, Double> entry : map.entrySet()) {
			items.add(entry.getKey());
			rarities.add(entry.getValue());
		}

		mark_L = new double[items.size()];
		mark_R = new double[items.size()];

		float cursor = 1;
		for(int i = 0; i<items.size(); i++)
		{
			mark_R[i] = cursor;
			cursor -= rarities.get(i);
			mark_L[i] = cursor;
		}
	}

	public RouletteWheel(T[] items, double[] rarities)
	{
		setItems(items, rarities);
	}
	
	private void setItems(T[] items, double[] rarities)
	{
		this.items = new ArrayList<>();
		this.items.addAll(Arrays.asList(items));

		this.rarities = new ArrayList<>();
		this.rarities.addAll(Arrays.stream(rarities).boxed().toList());
		
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
		
		for(int i = 0; i<items.size(); i++)
		{
			if(s>=mark_L[i] && s<mark_R[i])
			{
				return items.get(i);
			}
		}
		
		return null;
	}
}
