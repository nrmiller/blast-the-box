package miller.util;

public class MathTools 
{
	
	/**
	 * Returns the largest of the two. If they are the same, returns a.
	 * @param a
	 * @param b
	 * @return
	 */
	public static float largest(float a, float b)
	{
		if(a>b)
		{
			return a;
		}
		else if(b>a)
		{
			return b;
		}
		return a;
	}
	
	public static float smallest(float a, float b)
	{
		if(a<b)
		{
			return a;
		}
		else if(b<a)
		{
			return b;
		}
		return a;
	}

	public static long nanosToMillis(long nanos)
	{
		long millis = (long) (nanos * 10e-6);
		return millis;
	}
}
