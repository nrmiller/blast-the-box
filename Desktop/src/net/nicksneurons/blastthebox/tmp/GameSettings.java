package net.nicksneurons.blastthebox.tmp;

import com.millerni456.BlastTheBox.geometry.Cube;
import com.millerni456.BlastTheBox.utils.Textures;

public class GameSettings
{
	public final static int EASY = 0, MEDIUM = 1, HARD = 2;
	//Commented values describe the scenario under easy game-play.
	public double indestructible_chance = .02; //2% Chance.
	
	public double gray_cube_chance = .75; //75% Chance.
	public double green_cube_chance = .15; //15% Chance.
	public double blue_cube_chance = .10; //10% Chance.
	
	public double nuke_chance = .01; //1% Chance.
	public double stopwatch_chance = .03; //3% Chance.
	public double triplefire_chance = .06; //6% Chance.
	public double pierce_chance = .02; //2% Chance.
	public double shield_chance = .10; //10% Chance.
	public double heart_chance = .10; //10% Chance.
	public double ammo_chance = .32; //32% Chance.
	public double strength1_chance = .09; //9% Chance.
	public double strength2_chance = .04; //4% Chance.
	
	public double score_speed = .5; //50% speed
	public double cube_speed = .5; //50% speed
	public double cube_density = .5; //50% density
	
	public boolean arcade_mode = false;
	public boolean show_controller = true;
	
	public int game_mode = EASY;
	
	public GameSettings()
	{
		
	}
	
	public Integer[] getPowerups()
	{
		Integer[] i = new Integer[9];
		i[0] = Textures.powerup_nuke;
		i[1] = Textures.powerup_stopwatch;
		i[2] = Textures.powerup_triplefire;
		i[3] = Textures.powerup_pierce;
		i[4] = Textures.powerup_shield;
		i[5] = Textures.powerup_heart;
		i[6] = Textures.powerup_ammo;
		i[7] = Textures.powerup_strength1;
		i[8] = Textures.powerup_strength2;
		
		return i;
	}
	public double[] getPowerupRarities()
	{
		double[] rarities = 
		{
			nuke_chance, stopwatch_chance,
			triplefire_chance, pierce_chance,
			shield_chance, heart_chance,
			ammo_chance, strength1_chance,
			strength2_chance
		};
		
		return rarities;
	}
	
	public Integer[] getCubeHealths()
	{
		Integer[] cubeTypes = new Integer[3];
		cubeTypes[0] = 1;
		cubeTypes[1] = 2;
		cubeTypes[2] = 3;
		
		return cubeTypes;
	}
	
	public double[] getCubeHealthRarities()
	{
		double[] rarities = 
		{
			gray_cube_chance,
			green_cube_chance,
			blue_cube_chance
		};
		return rarities;
	}
	
	public Integer[] getIndestructible()
	{
		Integer[] i = new Integer[1];
		i[0] = Cube.INDESTRUCTIBLE;
		return i;
	}
	public double[] getIndestructibleRarity()
	{
		double[] rarities = 
		{
			indestructible_chance	
		};
		return rarities;
	}
}
