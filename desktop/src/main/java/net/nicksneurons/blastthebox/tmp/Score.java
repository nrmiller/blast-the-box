package net.nicksneurons.blastthebox.tmp;

public class Score
{
	public char[] initials = new char[3];
	public int score = 0;
	public int mode = 0;
	
	public Score(char[] inits, int score, int mode)
	{
		initials = inits;
		this.score = score;
		this.mode = mode;
	}
	public Score(char c1, char c2, char c3, int score, int mode)
	{
		initials[0] = c1;
		initials[1] = c2;
		initials[2] = c3;
		this.score = score;
		this.mode = mode;
	}
	
	public String initialsAsString()
	{
		String s = String.valueOf(initials);
		return s;
	}
}