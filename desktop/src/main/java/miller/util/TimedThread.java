package miller.util;

public abstract class TimedThread implements Runnable
{
	private boolean running = false;
	private Thread thread;
	private boolean paused = false;
	private long targetFPS = 60L, currentFPS = 0;
	private boolean limitFPS = false;
	public abstract void update();
	
	public TimedThread()
	{
		this(false);
	}
	public TimedThread(boolean limitFPS)
	{
		setLimitFPS(limitFPS);
	}
	public TimedThread(long fps_millis)
	{
		targetFPS = fps_millis;
		setLimitFPS(true);
	}
	
	public void start()
	{
		running = true;
		thread = new Thread(this);
		thread.start();
	}
	public void setPaused(boolean paused)
	{
		this.paused = paused;
	}
	public boolean isPaused()
	{
		return paused;
	}
	public void stop()
	{
		running = false;
		thread = null;
	}
	
	public void setTargetFPS(long fps_millis)
	{
		targetFPS = fps_millis;
		setLimitFPS(true);
	}
	public long getCurrentFPS()
	{
		return currentFPS;
	}
	public void setLimitFPS(boolean onOff)
	{
		limitFPS  = onOff;
	}
	public boolean isLimitingFPS()
	{
		return limitFPS;
	}
	public void run()
	{	
		long beforeTime, afterTime, elapsedTime, elapsedMillis, sleepTime = 0, overSleepTime = 0;
		long targetUpdateTime = 1000/targetFPS;//How long each iteration should take. (in millis)
		beforeTime = System.nanoTime();
		while(running)
		{
			if(!isPaused())
			{
				update();
			}
			
			if(!limitFPS)
			{//skip to next iteration if we are not concerned about limiting FPS
			 //this will slightly improved performance.
				beforeTime = System.nanoTime();//Measure in case FPS mode is switched.
				continue;
			}
			
			//Calculate update time. (elapsedMillis)
			afterTime = System.nanoTime();
			elapsedTime = afterTime - beforeTime; //Time took to call update()
			elapsedMillis = MathTools.nanosToMillis(elapsedTime);//Elapsed time in millis;
			
			sleepTime = targetUpdateTime - elapsedMillis - overSleepTime;
			if(sleepTime!=0)
			{
				currentFPS = (long) (1000/(sleepTime));//Calculates which FPS the CPU acheieved.
			}
			
			//Calculate difference from targetUpdateTime
			if(sleepTime > 0)
			{//We have extra time, sleep to maintain FPS.
				try
				{
					Thread.sleep(sleepTime);
				}
				catch(InterruptedException e){}
				overSleepTime =(System.nanoTime() - afterTime) - sleepTime;//If slept too long.
			}
			else if(sleepTime <= 0)
			{//Took too long! Skip to next iteration
				overSleepTime = 0L;
				beforeTime = System.nanoTime();
				continue;
			}
			
			beforeTime = System.nanoTime();
		}
	}
}
