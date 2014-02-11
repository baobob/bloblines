package org.bloblines.data.life;

import org.bloblines.data.world.Area;

/**
 * Represents a living thing in BlobWorld
 * 
 * @author mrflibble
 * 
 */
public abstract class LivingThing {

	public Area area;

	/**
	 * Creates a LivingThing in a starting area.
	 * 
	 * @param startingArea This is where the newly created living thing will
	 *            appear
	 */
	public LivingThing(Area startingArea) {
		area = startingArea;
		area.register(this);
	}

	public abstract void live();

	public void die() {
		area.unregister(this);
	}
}
