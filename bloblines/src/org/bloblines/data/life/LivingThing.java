package org.bloblines.data.life;

import java.util.Random;

import org.bloblines.data.world.Area;

/**
 * Represents a living thing in BlobWorld
 * 
 * @author mrflibble
 * 
 */
public abstract class LivingThing {

	public static Random r = new Random();

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

	/**
	 * Tests if a random int between 0 and 100 is under percent value. <br/>
	 * For instance, random(80) will return true about 80% of the time.
	 * random(101) will return true all the time, random(0) will return false
	 * all the time.
	 * @param percent Percent chance of returning true
	 * @return boolean true if a random int is lower than percent
	 */
	protected boolean random(int percent) {
		return r.nextInt(100) < percent;
	}
}
