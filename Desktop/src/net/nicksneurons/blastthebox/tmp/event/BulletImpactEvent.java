package net.nicksneurons.blastthebox.tmp.event;

import miller.opengl.Point3d;

import com.millerni456.BlastTheBox.Bullet;
import com.millerni456.BlastTheBox.geometry.Cube;

public class BulletImpactEvent
{
	public Point3d cubeLocation;
	public Point3d bulletLocation;
	public Bullet bullet;
	public Cube cube;
	public boolean isDead;
	
	public BulletImpactEvent(Bullet bullet, Cube c)
	{
		this.cube = c;
		this.bullet = bullet;
		bulletLocation = bullet.location.toPoint3d();
		cubeLocation = c.loc;
		this.isDead = c.isDead();
	}
}
