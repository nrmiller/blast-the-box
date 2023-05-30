package net.nicksneurons.blastthebox.utils;

import miller.opengl.Point3d;

public interface Renderable
{
	void init();
	void draw();
	Point3d getLocation();
}
