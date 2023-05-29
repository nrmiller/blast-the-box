package net.nicksneurons.blastthebox.tmp.utils;

import javax.microedition.khronos.opengles.GL10;

import miller.opengl.Point3d;

public interface Renderable
{
	public void draw(GL10 gl);
	public Point3d getLocation();
}
