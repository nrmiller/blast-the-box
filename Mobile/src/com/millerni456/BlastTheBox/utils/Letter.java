package com.millerni456.BlastTheBox.utils;

import javax.microedition.khronos.opengles.GL10;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;

public class Letter
{
	public static int A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z;
	public static int EXCLAMATION, SPACE;
	
	public static void loadLetters(Activity app, GL10 gl)
	{
		Resources res = app.getResources();
		//Load first set of numbers.
		Bitmap letters = Texture.getBitmapFromDrawable(res, com.millerni456.BlastTheBox.R.drawable.alphabet);
		
		Bitmap[] a = new Bitmap[28];
		
		int w = (int)(letters.getWidth() / 6);	
		a[0] = Bitmap.createBitmap(letters, 0, 0, w, w);//A
		a[1] = Bitmap.createBitmap(letters, w, 0, w, w);//B
		a[2] = Bitmap.createBitmap(letters, w*2, 0, w, w);//C
		a[3] = Bitmap.createBitmap(letters, w*3, 0, w, w);//D
		a[4] = Bitmap.createBitmap(letters, w*4, 0, w, w);//E
		a[5] = Bitmap.createBitmap(letters, w*5, 0, w, w);//F
		
		a[6] = Bitmap.createBitmap(letters, 0, w, w, w);//G
		a[7] = Bitmap.createBitmap(letters, w, w, w, w);//H
		a[8] = Bitmap.createBitmap(letters, w*2, w, w, w);//I
		a[9] = Bitmap.createBitmap(letters, w*3, w, w, w);//J
		a[10] = Bitmap.createBitmap(letters, w*4, w, w, w);//K
		a[11] = Bitmap.createBitmap(letters, w*5, w, w, w);//L
		
		a[12] = Bitmap.createBitmap(letters, 0, w*2, w, w);//M
		a[13] = Bitmap.createBitmap(letters, w, w*2, w, w);//N
		a[14] = Bitmap.createBitmap(letters, w*2, w*2, w, w);//O
		a[15] = Bitmap.createBitmap(letters, w*3, w*2, w, w);//P
		a[16] = Bitmap.createBitmap(letters, w*4, w*2, w, w);//Q
		a[17] = Bitmap.createBitmap(letters, w*5, w*2, w, w);//R
		
		a[18] = Bitmap.createBitmap(letters, 0, w*3, w, w);//S
		a[19] = Bitmap.createBitmap(letters, w, w*3, w, w);//T
		a[20] = Bitmap.createBitmap(letters, w*2, w*3, w, w);//U
		a[21] = Bitmap.createBitmap(letters, w*3, w*3, w, w);//V
		a[22] = Bitmap.createBitmap(letters, w*4, w*3, w, w);//W
		a[23] = Bitmap.createBitmap(letters, w*5, w*3, w, w);//X
		
		a[24] = Bitmap.createBitmap(letters, 0, w*4, w, w);//Y
		a[25] = Bitmap.createBitmap(letters, w, w*4, w, w);//Z
		a[26] = Bitmap.createBitmap(letters, w*2, w*4, w, w);//!
		a[27] = Bitmap.createBitmap(letters, w*3, w*4, w, w);//[SPACE]
		
		int[] ids = new int[28];
		for(int i = 0; i<a.length; i++)
		{
			Texture t = new Texture(gl, a[i]);
			ids[i] = t.textureId;
		}
		
		A = ids[0];
		B = ids[1];
		C = ids[2];
		D = ids[3];
		E = ids[4];
		F = ids[5];
		G = ids[6];
		H = ids[7];
		I = ids[8];
		J = ids[9];
		K = ids[10];
		L = ids[11];
		M = ids[12];
		N = ids[13];
		O = ids[14];
		P = ids[15];
		Q = ids[16];
		R = ids[17];
		S = ids[18];
		T = ids[19];
		U = ids[20];
		V = ids[21];
		W = ids[22];
		X = ids[23];
		Y = ids[24];
		Z = ids[25];
		EXCLAMATION = ids[26];
		SPACE = ids[27];
		
		for(int i = 0; i<a.length; i++)
		{
			a[i].recycle();
		}
		letters.recycle();
	}
	
	public static int getLetter(char c)
	{
		Character ch = Character.toLowerCase(c);
		switch(ch)
		{
			case 'a':
				return A;
			case 'b':
				return B;
			case 'c':
				return C;
			case 'd':
				return D;
			case 'e':
				return E;
			case 'f':
				return F;
			case 'g':
				return G;
			case 'h':
				return H;
			case 'i':
				return I;
			case 'j':
				return J;
			case 'k':
				return K;
			case 'l':
				return L;
			case 'm':
				return M;
			case 'n':
				return N;
			case 'o':
				return O;
			case 'p':
				return P;
			case 'q':
				return Q;
			case 'r':
				return R;
			case 's':
				return S;
			case 't':
				return T;
			case 'u':
				return U;
			case 'v':
				return V;
			case 'w':
				return W;
			case 'x':
				return X;
			case 'y':
				return Y;
			case 'z':
				return Z;
			case '!':
				return EXCLAMATION;
			case ' ':
				return SPACE;
			default:
				break;
		}
		
		return 0;
	}
}
