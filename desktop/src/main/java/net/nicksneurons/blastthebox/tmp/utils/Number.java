package net.nicksneurons.blastthebox.tmp.utils;

import javax.microedition.khronos.opengles.GL10;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;

import com.millerni456.BlastTheBox.R;

public class Number
{
	/**
	 * The values of these static variables correspond to their OpenGL texture object id.
	 */
	public static int ZERO, ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE;
	public static int ZEROy, ONEy, TWOy, THREEy, FOURy, FIVEy, SIXy, SEVENy, EIGHTy, NINEy, PLUSy;
	public static int PERCENT;
	
	public static void loadNumbers(Activity app, GL10 gl)
	{
		Resources res = app.getResources();
		//Load first set of numbers.
		Bitmap nums = Texture.getBitmapFromDrawable(res, R.drawable.numbers);
		
		Bitmap[] a = new Bitmap[11];
		int w = (int) (nums.getWidth() / 6);
		a[0] = Bitmap.createBitmap(nums, 0, 0, w, w);
		a[1] = Bitmap.createBitmap(nums, w, 0, w, w);
		a[2] = Bitmap.createBitmap(nums, w*2, 0, w, w);
		a[3] = Bitmap.createBitmap(nums, w*3, 0, w, w);
		a[4] = Bitmap.createBitmap(nums, w*4, 0, w, w);
		
		a[5] = Bitmap.createBitmap(nums, 0, w, w, w);
		a[6]= Bitmap.createBitmap(nums, w, w, w, w);
		a[7] = Bitmap.createBitmap(nums, w*2, w, w, w);
		a[8] = Bitmap.createBitmap(nums, w*3, w, w, w);
		a[9] = Bitmap.createBitmap(nums, w*4, w, w, w);
		a[10] = Bitmap.createBitmap(nums, w*5, w, w, w);
		
		int[] ids = new int[11];
		for(int i = 0; i<a.length; i++)
		{
			Texture t = new Texture(gl, a[i]);
			ids[i] = t.textureId;
		}
		
		ZERO = ids[0];
		ONE = ids[1];
		TWO = ids[2];
		THREE = ids[3];
		FOUR = ids[4];
		FIVE = ids[5];
		SIX = ids[6];
		SEVEN = ids[7];
		EIGHT = ids[8];
		NINE = ids[9];
		PERCENT = ids[10];
		
		for(int i = 0; i<a.length; i++)
		{
			a[i].recycle();
		}
		nums.recycle();
		
		//Load yellow numbers and plus sign
		Bitmap numsy = Texture.getBitmapFromDrawable(res, R.drawable.numbers_yellow);
		
		Bitmap[] ay = new Bitmap[11];
		ay[0] = Bitmap.createBitmap(numsy, 0, 0, w, w);
		ay[1] = Bitmap.createBitmap(numsy, w, 0, w, w);
		ay[2] = Bitmap.createBitmap(numsy, w*2, 0, w, w);
		ay[3] = Bitmap.createBitmap(numsy, w*3, 0, w, w);
		ay[4] = Bitmap.createBitmap(numsy, w*4, 0, w, w);
		
		ay[5] = Bitmap.createBitmap(numsy, 0, w, w, w);
		ay[6]= Bitmap.createBitmap(numsy, w, w, w, w);
		ay[7] = Bitmap.createBitmap(numsy, w*2, w, w, w);
		ay[8] = Bitmap.createBitmap(numsy, w*3, w, w, w);
		ay[9] = Bitmap.createBitmap(numsy, w*4, w, w, w);
		
		ay[10] = Bitmap.createBitmap(numsy, w*5, 0, w, w);
		
		int[] idsy = new int[11];
		for(int i = 0; i<ay.length; i++)
		{
			Texture t = new Texture(gl, ay[i]);
			idsy[i] = t.textureId;
		}
		
		ZEROy = idsy[0];
		ONEy = idsy[1];
		TWOy = idsy[2];
		THREEy = idsy[3];
		FOURy = idsy[4];
		FIVEy = idsy[5];
		SIXy = idsy[6];
		SEVENy = idsy[7];
		EIGHTy = idsy[8];
		NINEy = idsy[9];
		PLUSy = idsy[10];
		
		for(int i = 0; i<ay.length; i++)
		{
			ay[i].recycle();
		}
		numsy.recycle();
	}
	
	public static int getNumberTexture(int n)
	{
		switch(n)
		{
			case 0:
				return (Number.ZERO);
			case 1:
				return (Number.ONE);
			case 2:
				return (Number.TWO);
			case 3:
				return (Number.THREE);
			case 4:
				return (Number.FOUR);
			case 5:
				return (Number.FIVE);
			case 6:
				return (Number.SIX);
			case 7:
				return (Number.SEVEN);
			case 8:
				return (Number.EIGHT);
			case 9:
				return (Number.NINE);
			default:
				break;
		}
		return 0;
	}
	
	public static int getNumberTextureFromChar(char c)
	{
		switch(c)
		{
			case '0':
				return (Number.ZERO);
			case '1':
				return (Number.ONE);
			case '2':
				return (Number.TWO);
			case '3':
				return (Number.THREE);
			case '4':
				return (Number.FOUR);
			case '5':
				return (Number.FIVE);
			case '6':
				return (Number.SIX);
			case '7':
				return (Number.SEVEN);
			case '8':
				return (Number.EIGHT);
			case '9':
				return (Number.NINE);
			default:
				break;
		}
		return 0;
	}

	public static int getYellowNumberTexture(int i)
	{
		switch(i)
		{
			case 0:
				return (Number.ZEROy);
			case 1:
				return (Number.ONEy);
			case 2:
				return (Number.TWOy);
			case 3:
				return (Number.THREEy);
			case 4:
				return (Number.FOURy);
			case 5:
				return (Number.FIVEy);
			case 6:
				return (Number.SIXy);
			case 7:
				return (Number.SEVENy);
			case 8:
				return (Number.EIGHTy);
			case 9:
				return (Number.NINEy);
			default:
				break;
		}
		return 0;
	}
}

