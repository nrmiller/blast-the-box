package net.nicksneurons.blastthebox.tmp.io;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import android.content.Context;
import android.util.Log;

import com.millerni456.BlastTheBox.GameSettings;
import com.millerni456.BlastTheBox.Score;

public class HighScores
{
	public static Score[] scoresE = new Score[9];
	public static Score[] scoresM = new Score[9];
	public static Score[] scoresH = new Score[9];
	private final static String FILE_NAME_E = "scores_easy.dat";
	private final static String FILE_NAME_M = "scores_medium.dat";
	private final static String FILE_NAME_H = "scores_hard.dat";
	
	public HighScores()
	{
		//Default scores.
		scoresE[0] = new Score('J', 'E', 'B', 4, GameSettings.EASY);
		scoresE[1] = new Score('M', 'C', 'J', 10, GameSettings.EASY);
		scoresE[2] = new Score('B', 'J', 'M', 612, GameSettings.EASY);
		scoresE[3] = new Score('J', 'E', 'B', 708, GameSettings.EASY);
		scoresE[4] = new Score('B', 'J', 'M', 804, GameSettings.EASY);
		scoresE[5] = new Score('B', 'J', 'M', 920, GameSettings.EASY);
		scoresE[6] = new Score('B', 'J', 'M', 1184, GameSettings.EASY);
		scoresE[7] = new Score('D', 'J', 'M', 1568, GameSettings.EASY);
		scoresE[8] = new Score('B', 'J', 'M', 2689, GameSettings.EASY);
		
		scoresM[0] = new Score('D', 'J', 'M', 50, GameSettings.MEDIUM);
		scoresM[1] = new Score('J', 'E', 'B', 93, GameSettings.MEDIUM);
		scoresM[2] = new Score('M', 'C', 'J', 190, GameSettings.MEDIUM);
		scoresM[3] = new Score('D', 'J', 'M', 204, GameSettings.MEDIUM);
		scoresM[4] = new Score('D', 'J', 'M', 358, GameSettings.MEDIUM);
		scoresM[5] = new Score('D', 'J', 'M', 484, GameSettings.MEDIUM);
		scoresM[6] = new Score('J', 'P', 'M', 804, GameSettings.MEDIUM);
		scoresM[7] = new Score('D', 'J', 'M', 4851, GameSettings.MEDIUM);
		scoresM[8] = new Score('D', 'J', 'M', 5047, GameSettings.MEDIUM);
		
		scoresH[0] = new Score('R', 'J', 'M', 40, GameSettings.HARD);
		scoresH[1] = new Score('C', 'A', 'L', 32, GameSettings.HARD);
		scoresH[2] = new Score('M', 'C', 'J', 323, GameSettings.HARD);
		scoresH[3] = new Score('N', 'R', 'M', 468, GameSettings.HARD);
		scoresH[4] = new Score('D', 'J', 'M', 574, GameSettings.HARD);
		scoresH[5] = new Score('J', 'E', 'B', 803, GameSettings.HARD);
		scoresH[6] = new Score('N', 'R', 'M', 1420, GameSettings.HARD);
		scoresH[7] = new Score('N', 'R', 'M', 6828, GameSettings.HARD);
		scoresH[8] = new Score('N', 'R', 'M', 17087, GameSettings.HARD);
	}
	
	/**
	 * Reads the scores from the three files.
	 */
	public static void loadScores(Context context)
	{
		boolean a = false, b = false, c = false;
		String[] list = context.fileList();
		for(int i = 0; i<list.length; i++)
		{
			Log.v("FileIO", "File found: " + list[i]);
			if(list[i].equals(FILE_NAME_E))
			{
				a = true;
			}
			else if(list[i].equals(FILE_NAME_M))
			{
				b = true;
			}
			else if(list[i].equals(FILE_NAME_H))
			{
				c = true;
			}
		}
		
		if(!a || !b || !c)
		{//One or more of the files does not exist.  Write/create them all.
			writeScores(context);
		}
		
		scoresE = readFromFile(context, FILE_NAME_E, 9);
		scoresM = readFromFile(context, FILE_NAME_M, 9);
		scoresH = readFromFile(context, FILE_NAME_H, 9);
	}
	
	/**
	 * Reads a scores file and returns an array with the results.
	 * @param context - application's context
	 * @param src_file - name of scores source file
	 * @param array_length - number of elements in the returned array
	 * @return an array filled with the read scores
	 */
	private static Score[] readFromFile(Context context, String src_file, int array_length)
	{
		Score[] scores_return = new Score[array_length];
		for(int i = 0; i<array_length; i++)
		{
			scores_return[i] = new Score((new String("AAA")).toCharArray(), 0, GameSettings.EASY);
		}
		try//Read EASY scores
		{
			FileInputStream fis = context.openFileInput(src_file);
			BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
			
			String line;
			int index = 0;
			while((line = reader.readLine()) != null)
			{
				Log.v("FileIO", src_file + ": " + line);
				int initialsIndex = line.indexOf("initials=\"") + 10; //Index of first initial
				String initials = line.substring(initialsIndex, initialsIndex+3);
				int scoreIndex = line.indexOf("value=\"") + 7; //Index of first digit
				int scoreIndex2 = line.indexOf("\"mode");
				int score = Integer.valueOf(line.substring(scoreIndex, scoreIndex2));
				int modeIndex = line.indexOf("mode=\"") + 6; //Index of mode
				int mode = Integer.valueOf(line.substring(modeIndex, modeIndex+1));
				
				if(index<array_length)
				{
					scores_return[index] = new Score(initials.toCharArray(), score, mode);
				}
				else
				{
					break;
				}
				index++;
			}
			
			reader.close();
			fis.close();
		}
		catch (FileNotFoundException e)
		{
			Log.e("FileIO", e.getMessage());
		}
		catch (IOException e)
		{
			Log.e("FileIO", e.getMessage());
		}
		catch (NumberFormatException e)
		{
			Log.e("FileIO", e.getMessage());
		}

		return scores_return;
	}
	
	/**
	 * Writes the scores to a file. Scores must be loaded before they can be written.
	 * @param context - application's context
	 */
	public static void writeScores(Context context)
	{
		writeScoreArray(context, FILE_NAME_E, scoresE);
		writeScoreArray(context, FILE_NAME_M, scoresM);
		writeScoreArray(context, FILE_NAME_H, scoresH);
	}
	private static void writeScoreArray(Context context, String dst_file, Score[] data)
	{
		try
		{
			FileOutputStream fos = context.openFileOutput(dst_file, Context.MODE_PRIVATE);
			PrintWriter pw = new PrintWriter(fos);
			
			for(int i = 0; i<data.length; i++)
			{
				pw.write("Score{");
				pw.write("initials=\"" + data[i].initialsAsString() + "\"");
				pw.write("value=\"" + data[i].score + "\"");
				pw.write("mode=\"" + data[i].mode + "\"}\n");
			}
			
			pw.close();
			fos.close();
		}
		catch (FileNotFoundException e)
		{
			Log.e("FileIO", e.getMessage());
		}
		catch (IOException e)
		{
			Log.e("FileIO", e.getMessage());
		}
	}
	
	public static void addScore(Score s)
	{
		Score[] temp = new Score[10];
		
		switch(s.mode)
		{
			case GameSettings.EASY:
			{
				for(int i = 0; i<scoresE.length; i++)
				{
					temp[i] = scoresE[i];
				}
				break;
			}
			case GameSettings.MEDIUM:
			{
				for(int i = 0; i<scoresM.length; i++)
				{
					temp[i] = scoresM[i];
				}
				break;
			}
			case GameSettings.HARD:
			{
				for(int i = 0; i<scoresH.length; i++)
				{
					temp[i] = scoresH[i];
				}
				break;
			}
			default:
			{
				Log.e("FileIO", "Error adding score, invalid mode set!");
				return;
			}
		}
		
		temp[9] = s; //Put the new score in.
		temp = sort(temp);//Sorts the 10 scores from highest to lowest.
		
		//Now take the first 9 elements in temp, and put into correct array.
		switch(s.mode)
		{
			case GameSettings.EASY:
			{
				for(int i = 0; i<scoresE.length; i++)
				{
					scoresE[i] = temp[i];
				}
				break;
			}
			case GameSettings.MEDIUM:
			{
				for(int i = 0; i<scoresM.length; i++)
				{
					scoresM[i] = temp[i];
				}
				break;
			}
			case GameSettings.HARD:
			{
				for(int i = 0; i<scoresH.length; i++)
				{
					scoresH[i] = temp[i];
				}
				break;
			}
			default:
				break;
		}
		
	}
	
	/**
	 * Sorts the given scores, by their values from largest to smallest.
	 * @param scores - the given scores
	 * @return - sorted array
	 */
	public static Score[] sort(Score[] scores)
	{
		int i, j;
		Score temp;
		
		for(i=0; i<scores.length-1; i++)
		{
			for(j=i+1; j<scores.length; j++)
			{
				if(scores[i].score<scores[j].score)
				{
					temp = scores[i];
					scores[i] = scores[j];
					scores[j] = temp;
				}
			}
		}
		return scores;
	}
}
