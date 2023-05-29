package com.millerni456.BlastTheBox.event;

public interface BulletListener
{
	/**
	 * Called when a Bullet collides with a cube.
	 * @param e
	 */
	public void onBulletImpact(BulletImpactEvent e);
}
