package org.bloblines.data.life;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.bloblines.data.world.Area;

/**
 * Represents a living thing in BlobWorld
 * 
 * @author mrflibble
 * 
 */
public abstract class LivingThing {

	/**
	 * Replaces the dice.
	 */
	public static Random r = new Random();

	/**
	 * The area where the livingThing currently is.
	 */
	public Area area;

	/**
	 * Is this living thing alive ? Set to true on instantiation. die() method
	 * will set it to false.
	 */
	public boolean alive = true;

	/**
	 * Unique identifier.
	 */
	public String name = id();

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

	/**
	 * Get a life and do something !
	 */
	public abstract void live();

	/**
	 * Bye !
	 */
	public void die() {
		alive = false;
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

	/**
	 * Say something to standard output. Like in a prayer.
	 * @param message The things you want to say
	 */
	public void say(String message) {
		System.out.println(name + "> " + message);
	}

	private static Map<String, Integer> ids = new HashMap<>();

	public synchronized String id() {
		String className = this.getClass().getSimpleName();
		Integer id = LivingThing.ids.get(className);
		if (id == null) {
			id = Integer.valueOf(0);
		}
		LivingThing.ids.put(className, Integer.valueOf(id.intValue() + 1));
		return className + "_" + id;
	}
}
