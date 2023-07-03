package net.nicksneurons.blastthebox.game;

import net.nicksneurons.blastthebox.game.entities.Row;
import net.nicksneurons.blastthebox.graphics.geometry.Square;
import net.nicksneurons.blastthebox.utils.S;
import org.joml.Vector2d;
import org.joml.Vector3d;

public class CubePopulator
{
	/**
	 * The maximum number of cubes that stretch from left to right.
	 */
	public static int FIELD_WIDTH = 20;
	/**
	 * The multiple specifying how many cubes should appear in a row. (on average)
	 * 100 being the highest, 0 being the lowest.
	 */
	public static int DENSITY = 10;
	
	private static int seq_width = 0;
	private static int seq_index = 0;
	private static boolean seq_first = true; 
	private static boolean sequencing = false;
	private static int cooldown = 0;
	
	private static Row[] rows = new Row[100];
	private static boolean[] used = new boolean[100];
	
	public static void createRows()
	{
		for(int i = 0; i<rows.length; i++)
		{
//			rows[i] = new Row();
		}
	}
	public synchronized static Row getRow()
	{
		for(int i = 0; i<rows.length; i++)
		{
			if(!used[i])
			{
				used[i] = true;
				return rows[i];
			}
		}
		return null;
	}
	public synchronized static void giveRow(Row r)
	{
		for(int i = 0; i<rows.length; i++)
		{
			if(r==rows[i])
			{
				used[i] = false;
				break;
			}
		}
	}
	
	/**
	 * Fills Row from the stock with data.
	 * @param settings - the current game settings
	 * @return a Row object with newly generated values.
	 */
	public synchronized static Row initializeRow(GameSettings settings)
	{
		int seq = S.ran.nextInt(1000);
		if(seq > 992 && !sequencing && cooldown <= 0) //Every now and then sequencing will start.
		{//cooldown prevents sequencing from happening directly after a sequence.
			cooldown = 100;
			sequencing = true;

			seq_width = S.ran.nextInt(3) + 8;
			seq_index = S.ran.nextInt(FIELD_WIDTH);
			if(seq_index>FIELD_WIDTH-seq_width)//If index is beyond right side of field, change it.
			{
				seq_index = FIELD_WIDTH-seq_width;
			}
		}
		
		Row r = null;
		
		if(sequencing)//Blocks are now in an orderly format. (Paths created within them)
		{
			r = getRow();
//			r.init(seq_width, seq_index, seq_first);
			if(seq_first)
			{
				seq_first = false;
			}
			
			if(S.ran.nextInt(50)>=30 && seq_width>2)
			{
				seq_width -= 1;
			}
			int n = S.ran.nextInt(100);
			if(n>=80 && seq_width>2)
			{
				seq_index +=1;
			}
			else if(n>=60 && n < 80 && seq_width>2)
			{
				seq_index -=1;
			}
			
			if(seq_width<=3)//If width becomes small enough, create opportunity to stop sequencing.
			{
				if(S.ran.nextInt(100)>60)
				{
					sequencing = false;
					seq_first = true;
				}
			}
			
			if(seq_index < 0)//Make sure that the index and width stay in bounds. (Left-side)
			{
				seq_index = 0;
				if(seq_width <= 1)
				{
					seq_width = 2;
				}
			}
			
		}
		else
		{
			cooldown -= 1;
			if(cooldown < 0){cooldown = 0;}
			r = getRow();
//			r.init(settings);
		}	
		return r;
		
	}

	@Deprecated
	public static FloorRow createFloorRow()
	{
		FloorRow fr = new FloorRow();
		return fr;
	}
}

class FloorRow
{
	public float distance = 50;
	public Square[] squares;
	
	public FloorRow()
	{
		int numSquares = CubePopulator.FIELD_WIDTH;
		squares = new Square[numSquares];
		int xLoc;
		for(int i = 0; i<numSquares; i++)
		{
			xLoc = i - CubePopulator.FIELD_WIDTH/2;
			squares[i] = new Square(new Vector3d(xLoc, 0, 1), new Vector2d(1, 1));
			squares[i].init();
		}
	}
	
	public void render()
	{//draws the cubes.
		// todo replace with a transform
//		glPushMatrix();
//		glTranslatef(0, 1, -distance);
//		glRotatef(90, 1, 0, 0);
		for(int i = 0; i<squares.length; i++)
		{
			squares[i].draw();
		}
//		glPopMatrix();
	}
	
	public void moveCloser(float amount)
	{
		distance -= amount;
	}
}
