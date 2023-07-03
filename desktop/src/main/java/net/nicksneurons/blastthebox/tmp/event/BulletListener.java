package net.nicksneurons.blastthebox.tmp.event;

public interface BulletListener
{
	/**
	 * Called when a Bullet collides with a cube.
	 * @param e
	 */
	public void onBulletImpact(BulletImpactEvent e);
}
