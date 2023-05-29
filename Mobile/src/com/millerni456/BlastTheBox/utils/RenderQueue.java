package com.millerni456.BlastTheBox.utils;

import java.util.ArrayList;
import java.util.HashMap;

import javax.microedition.khronos.opengles.GL10;


public class RenderQueue
{
	
	public ArrayList<Renderable> renderables = new ArrayList<Renderable>();
	/**
	 * Format for the HashMap.
	 * First Integer is the index of the renderable object in its array.
	 * Second Integer is Z-Value of a renderable object.
	 */
	public HashMap<Integer, Double> map = new HashMap<Integer, Double>();
	public int[] indexArray; //Used for sort algorithm. (Values map to a Z value)
	
	public RenderQueue()
	{
		
	}
	
	/**
	 * Add a Renderable object to the queue to be rendered.
	 * @param obj
	 */
	public void addToQueue(Renderable obj)
	{
		renderables.add(obj);
		map.put(renderables.size()-1, renderables.get(renderables.size()-1).getLocation().getZ());
	}
	
	/**
	 * Sorts then renders all currently added Renderable objects on the queue. Renderable objects are sorted
	 * based on their Z-values, from smallest to largest. After rendering,
	 * all the objects are removed from the queue.
	 * @param gl - OpenGL context.
	 */
	public void render(GL10 gl)
	{
		indexArray = new int[renderables.size()];
		for(int i = 0; i<indexArray.length; i++)
		{
			indexArray[i] = i;
		}
		sort(indexArray, 0, indexArray.length-1);
		
		Renderable[] objects = new Renderable[renderables.size()];
		for(int i = 0; i<objects.length; i++)
		{
			objects[i] = renderables.get(indexArray[i]);
		}
		
		for(int i = 0; i<objects.length; i++)
		{
			//Render objects in correct Z-Order
			objects[i].draw(gl);
		}
		
		for(int i = renderables.size()-1; i>=0; i--)
		{
			//Remove from queue.
			renderables.remove(i);
		}
	}
	
	/**
	 * This function is primarily intended to be used for removing an item from the 
	 * queue to prevent it from being rendered.
	 * @param index - the index of the object to be removed from the queue
	 */
	public void removeFromQueue(int index)
	{
		renderables.remove(index);
	}
	
	/*
	 * Quick sort methods below.
	 */
	private int partition(int[] array, int left, int right)
	{
		int i = left, j = right;
		int tmp;
		Double pivot = map.get(array[(left+right)/2]);
		
		while(i<=j)
		{
			while(map.get(array[i])<pivot)
			{
				i++;
			}
			while(map.get(array[j])>pivot)
			{
				j--;
			}
			
			if(i<=j)
			{
				tmp = array[j];
				array[j] = array[i];
				array[i] = tmp;
				i++;
				j--;
			}
		}
		return i;
	}
	private void sort(int[] array, int left, int right)
	{
		int middle = partition(array, left, right);
		if(left < middle-1)
		{
			sort(array, left, middle-1);
		}
		if(middle < right)
		{
			sort(array, middle, right);
		}
	}
}
