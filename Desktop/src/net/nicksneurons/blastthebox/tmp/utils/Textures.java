package net.nicksneurons.blastthebox.tmp.utils;

import javax.microedition.khronos.opengles.GL10;

import android.app.Activity;
import android.content.res.Resources;

import com.millerni456.BlastTheBox.R;

public class Textures
{
	public static int block_one, block_two, block_three, block_four, floor, floor_transl, floor_transr;
	public static int powerup_heart, powerup_stopwatch, powerup_nuke, powerup_shield, powerup_triplefire, powerup_ammo;
	public static int powerup_strength1, powerup_strength2, powerup_pierce;
	public static int health_on, health_off, cursor;
	public static int missile, missile2, missile3;
	public static int ammo_counter, piercing_counter;
	public static int menu_main, menu_highscores;
	public static int arrow_up, arrow_down, arrow_left, arrow_right;
	public static int button_easymode, button_mediummode, button_hardmode, button_back, button_exit, button_options, button_achievements, button_continue, button_fire, button_pause, button_play, button_pause_exit, button_howtoplay, button_prev_on, button_prev_off, button_next_on, button_next_off;
	public static int button_easy_on, button_easy_off, button_medium_on, button_medium_off, button_hard_on, button_hard_off;
	public static int logo, trans, shield_cover;
	public static int slider_base, slider_thumb, music_on, music_off, soundfx_on, soundfx_off, controller_on, controller_off, button_reset;
	public static int text_TM, text_tolerance, text_music, text_soundfx, text_controller;
	public static int[] guide = new int[4];
	
	public static void loadTextures(Activity app, GL10 gl)
	{
		Resources res = app.getResources();
		
		Texture trans_tex = new Texture(gl, res.openRawResource(R.drawable.trans), "");
		trans = trans_tex.textureId;
		
		Texture cursor_tex = new Texture(gl, res.openRawResource(R.drawable.cursor), "");
		cursor = cursor_tex.textureId;
		
		Texture block_one_tex = new Texture(gl, res.openRawResource(R.drawable.block_gray), "");
		Texture block_two_tex = new Texture(gl, res.openRawResource(R.drawable.block_green), "");
		Texture block_three_tex = new Texture(gl, res.openRawResource(R.drawable.block_blue), "");
		Texture block_four_tex = new Texture(gl, res.openRawResource(R.drawable.block_red), "");
		Texture floor_tex = new Texture(gl, res.openRawResource(R.drawable.floor), "");
		Texture floor_transl_tex = new Texture(gl, res.openRawResource(R.drawable.floor_transl), "");
		Texture floor_transr_tex = new Texture(gl, res.openRawResource(R.drawable.floor_transr), "");
		block_one = block_one_tex.textureId;
		block_two = block_two_tex.textureId;
		block_three = block_three_tex.textureId;
		block_four = block_four_tex.textureId;
		floor = floor_tex.textureId;
		floor_transl = floor_transl_tex.textureId;
		floor_transr = floor_transr_tex.textureId;
		floor_tex.setRepeating(gl, Texture.REPEAT_S|Texture.REPEAT_T);
		floor_transl_tex.setRepeating(gl, Texture.REPEAT_T);
		floor_transr_tex.setRepeating(gl, Texture.REPEAT_T);
		
		Texture arrow_left_tex = new Texture(gl, res.openRawResource(R.drawable.arrow_left), "");
		Texture arrow_right_tex = new Texture(gl, res.openRawResource(R.drawable.arrow_right), "");
		Texture button_fire_tex = new Texture(gl, res.openRawResource(R.drawable.button_fire), "");
		arrow_left = arrow_left_tex.textureId;
		arrow_right = arrow_right_tex.textureId;
		button_fire = button_fire_tex.textureId;
		
		Texture powerup_heart_tex = new Texture(gl, res.openRawResource(R.drawable.powerup_heart), "");
		Texture powerup_stopwatch_tex = new Texture(gl, res.openRawResource(R.drawable.powerup_stopwatch), "");
		Texture powerup_nuke_tex = new Texture(gl, res.openRawResource(R.drawable.powerup_nuke), "");
		Texture powerup_shield_tex = new Texture(gl, res.openRawResource(R.drawable.powerup_shield), "");
		Texture powerup_triplefire_tex = new Texture(gl, res.openRawResource(R.drawable.powerup_triplefire), "");
		Texture powerup_ammo_tex = new Texture(gl, res.openRawResource(R.drawable.powerup_ammo), "");
		Texture powerup_strength1_tex = new Texture(gl, res.openRawResource(R.drawable.powerup_strength1), "");
		Texture powerup_strength2_tex = new Texture(gl, res.openRawResource(R.drawable.powerup_strength2), "");
		Texture powerup_pierce_tex = new Texture(gl, res.openRawResource(R.drawable.powerup_pierce), "");
		powerup_heart = powerup_heart_tex.textureId;
		powerup_stopwatch = powerup_stopwatch_tex.textureId;
		powerup_nuke = powerup_nuke_tex.textureId;
		powerup_shield = powerup_shield_tex.textureId;
		powerup_triplefire = powerup_triplefire_tex.textureId;
		powerup_ammo = powerup_ammo_tex.textureId;
		powerup_strength1 = powerup_strength1_tex.textureId;
		powerup_strength2 = powerup_strength2_tex.textureId;
		powerup_pierce = powerup_pierce_tex.textureId;
		
		Texture health_on_tex = new Texture(gl, res.openRawResource(R.drawable.health_on), "");
		Texture health_off_tex = new Texture(gl, res.openRawResource(R.drawable.health_off), "");
		Texture shield_cover_tex = new Texture(gl, res.openRawResource(R.drawable.shield), "");
		health_on = health_on_tex.textureId;
		health_off = health_off_tex.textureId;
		shield_cover = shield_cover_tex.textureId;
		
		Texture ammo_counter_tex = new Texture(gl, res.openRawResource(R.drawable.ammo_counter), "");
		Texture piercing_counter_tex = new Texture(gl, res.openRawResource(R.drawable.piercing_counter), "");
		ammo_counter = ammo_counter_tex.textureId;
		piercing_counter = piercing_counter_tex.textureId;
		
		Texture missile_tex = new Texture(gl, res.openRawResource(R.drawable.missile), "");
		Texture missile2_tex = new Texture(gl, res.openRawResource(R.drawable.missile2), "");
		Texture missile3_tex = new Texture(gl, res.openRawResource(R.drawable.missile3), "");
		missile = missile_tex.textureId;
		missile2 = missile2_tex.textureId;
		missile3 = missile3_tex.textureId;
		
		Texture menu_main_tex = new Texture(gl, res.openRawResource(R.drawable.menu_main), "");
		Texture menu_highscores_tex = new Texture(gl, res.openRawResource(R.drawable.menu_highscores), "");
		Texture arrow_up_tex = new Texture(gl, res.openRawResource(R.drawable.arrow_up), "");
		Texture arrow_down_tex = new Texture(gl, res.openRawResource(R.drawable.arrow_down), "");
		menu_main = menu_main_tex.textureId;
		menu_highscores = menu_highscores_tex.textureId;
		arrow_up = arrow_up_tex.textureId;
		arrow_down = arrow_down_tex.textureId;
		
		Texture button_easymode_tex = new Texture(gl, res.openRawResource(R.drawable.button_easymode3), "");
		Texture button_mediummode_tex = new Texture(gl, res.openRawResource(R.drawable.button_mediummode3), "");
		Texture button_hardmode_tex = new Texture(gl, res.openRawResource(R.drawable.button_hardmode3), "");
		Texture button_back_tex = new Texture(gl, res.openRawResource(R.drawable.button_back), "");
		Texture button_exit_tex = new Texture(gl, res.openRawResource(R.drawable.button_exit), "");
		Texture button_options_tex = new Texture(gl, res.openRawResource(R.drawable.button_options), "");
		Texture button_achievements_tex = new Texture(gl, res.openRawResource(R.drawable.button_achievements), "");
		Texture button_continue_tex = new Texture(gl, res.openRawResource(R.drawable.button_continue), "");
		Texture button_pause_tex = new Texture(gl, res.openRawResource(R.drawable.button_pause), "");
		Texture button_play_tex = new Texture(gl, res.openRawResource(R.drawable.button_play), "");
		Texture button_pause_exit_tex = new Texture(gl, res.openRawResource(R.drawable.button_pause_exit), "");
		Texture button_howtoplay_tex = new Texture(gl, res.openRawResource(R.drawable.button_howtoplay), "");
		Texture button_prev_on_tex = new Texture(gl, res.openRawResource(R.drawable.button_prev_on), "");
		Texture button_next_on_tex = new Texture(gl, res.openRawResource(R.drawable.button_next_on), "");
		Texture button_prev_off_tex = new Texture(gl, res.openRawResource(R.drawable.button_prev_off), "");
		Texture button_next_off_tex = new Texture(gl, res.openRawResource(R.drawable.button_next_off), "");
		Texture slider_base_tex = new Texture(gl, res.openRawResource(R.drawable.slider_base), "");
		Texture slider_thumb_tex = new Texture(gl, res.openRawResource(R.drawable.slider_thumb), "");
		Texture music_on_tex = new Texture(gl, res.openRawResource(R.drawable.music_on), "");
		Texture music_off_tex = new Texture(gl, res.openRawResource(R.drawable.music_off), "");
		Texture soundfx_on_tex = new Texture(gl, res.openRawResource(R.drawable.soundfx_on), "");
		Texture soundfx_off_tex = new Texture(gl, res.openRawResource(R.drawable.soundfx_off), "");
		Texture controller_on_tex = new Texture(gl, res.openRawResource(R.drawable.controller_on), "");
		Texture controller_off_tex = new Texture(gl, res.openRawResource(R.drawable.controller_off), "");
		Texture button_reset_tex = new Texture(gl, res.openRawResource(R.drawable.button_reset), "");
		button_easymode = button_easymode_tex.textureId;
		button_mediummode = button_mediummode_tex.textureId;
		button_hardmode = button_hardmode_tex.textureId;
		button_back = button_back_tex.textureId;
		button_exit = button_exit_tex.textureId;
		button_options = button_options_tex.textureId;
		button_achievements = button_achievements_tex.textureId;
		button_continue = button_continue_tex.textureId;
		button_pause = button_pause_tex.textureId;
		button_play = button_play_tex.textureId;
		button_pause_exit = button_pause_exit_tex.textureId;
		button_howtoplay = button_howtoplay_tex.textureId;
		button_prev_on = button_prev_on_tex.textureId;
		button_next_on = button_next_on_tex.textureId;
		button_prev_off = button_prev_off_tex.textureId;
		button_next_off = button_next_off_tex.textureId;
		slider_base = slider_base_tex.textureId;
		slider_thumb = slider_thumb_tex.textureId;
		music_on = music_on_tex.textureId;
		music_off = music_off_tex.textureId;
		soundfx_on = soundfx_on_tex.textureId;
		soundfx_off = soundfx_off_tex.textureId;
		controller_on = controller_on_tex.textureId;
		controller_off = controller_off_tex.textureId;
		button_reset = button_reset_tex.textureId;
		
		Texture button_easy_on_tex = new Texture(gl, res.openRawResource(R.drawable.button_easyon), "");
		Texture button_easy_off_tex = new Texture(gl, res.openRawResource(R.drawable.button_easyoff), "");
		Texture button_medium_on_tex = new Texture(gl, res.openRawResource(R.drawable.button_mediumon), "");
		Texture button_medium_off_tex = new Texture(gl, res.openRawResource(R.drawable.button_mediumoff), "");
		Texture button_hard_on_tex = new Texture(gl, res.openRawResource(R.drawable.button_hardon), "");
		Texture button_hard_off_tex = new Texture(gl, res.openRawResource(R.drawable.button_hardoff), "");
		button_easy_on = button_easy_on_tex.textureId;
		button_easy_off = button_easy_off_tex.textureId;
		button_medium_on = button_medium_on_tex.textureId;
		button_medium_off = button_medium_off_tex.textureId;
		button_hard_on = button_hard_on_tex.textureId;
		button_hard_off = button_hard_off_tex.textureId;
		
		Texture text_TM_tex = new Texture(gl, res.openRawResource(R.drawable.text_tm), "");
		Texture text_tolerance_tex = new Texture(gl, res.openRawResource(R.drawable.text_tolerance), "");
		Texture text_controller_tex = new Texture(gl, res.openRawResource(R.drawable.text_controller), "");
		Texture text_music_tex = new Texture(gl, res.openRawResource(R.drawable.text_music), "");
		Texture text_soundfx_tex = new Texture(gl, res.openRawResource(R.drawable.text_soundfx), "");
		text_TM = text_TM_tex.textureId;
		text_tolerance = text_tolerance_tex.textureId;
		text_controller = text_controller_tex.textureId;
		text_music = text_music_tex.textureId;
		text_soundfx = text_soundfx_tex.textureId;
		
		Texture guide_tex0 = new Texture(gl, res.openRawResource(R.drawable.guide_0), "");
		Texture guide_tex1 = new Texture(gl, res.openRawResource(R.drawable.guide_1), "");
		Texture guide_tex2 = new Texture(gl, res.openRawResource(R.drawable.guide_2), "");
		Texture guide_tex3 = new Texture(gl, res.openRawResource(R.drawable.guide_3), "");
		guide[0] = guide_tex0.textureId;
		guide[1] = guide_tex1.textureId;
		guide[2] = guide_tex2.textureId;
		guide[3] = guide_tex3.textureId;
	}
}
