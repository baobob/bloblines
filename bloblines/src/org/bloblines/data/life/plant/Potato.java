package org.bloblines.data.life.plant;

import org.bloblines.data.life.LivingThing;
import org.bloblines.data.world.Area;

/**
 * Potatoes are peaceful plants. Blob usually take them into breeding farms.
 * They're tasteful with iron !
 * 
 * Potatoes don't breed, they reproduce themselves in a mitosis process when
 * they get old enough.
 * 
 * @author mrflibble
 * 
 */
public class Potato extends LivingThing {

	public int age = 0;
	public int hunger = 0;

	/**
	 * When age is above this limit, potato may die of old age
	 */
	private int ageLimit = 50;

	/**
	 * When hunger is above this limit, potato may starve to death
	 */
	private int hungerLimit = 30;

	public Potato(Area startingArea) {
		super(startingArea);
	}

	@Override
	public void live() {
		age++;
		hunger++;

		if (random(age - ageLimit)) {
			say("Potato died of old age");
			die();
		} else if (random(hunger - hungerLimit)) {
			say("Potato starved to death");
			die();
		}

		if (!alive) {
			return;
		}

		if (age > 20) {
			if (random(5)) {
				say("Mitosis process");
				// Mitosis process
				//				new Potato(area);
				//				new Potato(area);
				die();
			}
		}

	}
}
